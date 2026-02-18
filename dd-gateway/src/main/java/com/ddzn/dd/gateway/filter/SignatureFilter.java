package com.ddzn.dd.gateway.filter;

import com.alibaba.nacos.common.utils.JacksonUtils;
import com.ddzn.dd.framework.common.config.MyGatewayProperties;
import com.ddzn.dd.gateway.auth.SignatureAuthFactory;
import com.ddzn.dd.gateway.service.BaseAppService;
import com.ddzn.dd.model.app.AppEntity;
import com.ddzn.dd.model.base.BusinessException;
import com.ddzn.dd.model.base.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @Author:zhaopeng
 * @Date 2020/06/08 11：11
 * 验签过滤器
 */
@Component
@Slf4j
public class SignatureFilter implements GlobalFilter, Ordered {

    @Resource
    SignatureAuthFactory signatureAuthFactory;
    @Resource
    private MyGatewayProperties myGatewayProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String appkey = request.getHeaders().getFirst("appkey");
        String currPath = request.getURI().getPath();
        String version = request.getHeaders().getFirst("version");
        String referer = request.getHeaders().getFirst("Referer");
        String method = request.getMethodValue();
        String contentType = request.getHeaders().getFirst("Content-Type");
        if (doPass(currPath)) {
            return chain.filter(exchange);
        }
        if (referer != null && !referer.contains(myGatewayProperties.getDomain())) {
            //throw new BusinessException("对不起，请稍后重试");
        }

        if ("POST".equals(method) || "PUT".equals(method)) {
            DefaultDataBufferFactory defaultDataBufferFactory = new DefaultDataBufferFactory();
            DefaultDataBuffer defaultDataBuffer = defaultDataBufferFactory.allocateBuffer(0);
            return DataBufferUtils.join(request.getBody().defaultIfEmpty(defaultDataBuffer))
                    .flatMap(dataBuffer -> {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        String bodyString = null;
                        bodyString = new String(bytes, StandardCharsets.UTF_8);
                        exchange.getAttributes().put("POST_BODY", bodyString);
                        DataBufferUtils.release(dataBuffer);
                        Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
                            DataBuffer buffer = exchange.getResponse().bufferFactory()
                                    .wrap(bytes);
                            return Mono.just(buffer);
                        });
                        String clientIp = request.getRemoteAddress().getAddress().getHostAddress();

                        ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(request) {
                            @Override
                            public Flux<DataBuffer> getBody() {
                                return cachedFlux;
                            }

                            @Override
                            public HttpHeaders getHeaders() {
                                // 创建一个新的 HttpHeaders 对象，基于原始请求头
                                HttpHeaders headers = new HttpHeaders();
                                headers.putAll(super.getHeaders());
                                // 添加或替换 X-Real-IP 请求头
                                headers.set("X-Real-IP", clientIp);
                                return headers;
                            }
                        };
                        ResponseResult result = checkSign(exchange, method, contentType, version, bodyString, appkey);
                        if (result == null) {
                            // 3. 将修改后的请求传递给下一个过滤器
                            return chain.filter(exchange.mutate().request(mutatedRequest).build());
                        }
                        //不合法(响应未登录的异常)
                        return noAuth(exchange, result);
                    });
        } else {
            ResponseResult result = checkSign(exchange, method, contentType, version, "", appkey);
            if (result == null) {
                //身份验证成功，放行
                return chain.filter(exchange);
            }
            //不合法(响应未登录的异常)
            return noAuth(exchange, result);
        }
    }


    @Override
    public int getOrder() {
        return 0;
    }

    private Mono<Void> noAuth(ServerWebExchange exchange, ResponseResult result) {
        ServerHttpResponse response = exchange.getResponse();
        //设置headers
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        String warningStr = null;
        warningStr = JacksonUtils.toJson(result);
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(warningStr.getBytes());
        return response.writeWith(Mono.just(bodyDataBuffer));
    }

    @Resource
    BaseAppService baseAppService;

    private ResponseResult checkSign(ServerWebExchange exchange, String method, String contentType, String version, String bodyString, String appkey) {
        if (myGatewayProperties.getTestAppkey() != null && myGatewayProperties.getTestAppkey().contains(appkey)) {
            return null;
        }
        String authClass = "judgeAuth";
        //1.校验参数
        ResponseResult valid = signatureAuthFactory.validParam(exchange, authClass);
        if (valid != null) {
            return valid;
        }
        //2.获取用户信息
        AppEntity appEntity = baseAppService.queryByAppKey(appkey, version);
        if (appEntity == null) {
            throw new BusinessException("请升级到最新版本");
        }
        //3.验证身份，是否匹配
        exchange.getAttributes().put("appName", appEntity.getAppName());
        ResponseResult result = signatureAuthFactory.checkSignature(exchange, appEntity, authClass, method, contentType, bodyString);
        return result;
    }

    private final String[] urls = {
            "swagger-resources",
            "webjars",
            "api-docs",
            "swagger-ui.html",
            "swagger-ui",
            "doc.html"
    };

    public boolean containsSpecialUrl(String path) {
        if (path == null || path.isEmpty()) {
            return false;
        }
        for (String url : urls) {
            if (path.contains(url)) {
                return true;  // 只要包含任意一个，就返回 true
            }
        }
        return false; // 全部都不包含，返回 false
    }

    public boolean containsPassUrl(String path) {
        if (path == null || path.isEmpty()) {
            return false;
        }
        for (String url : myGatewayProperties.getPass()) {
            if (path.contains(url)) {
                return true;  // 只要包含任意一个，就返回 true
            }
        }
        return false; // 全部都不包含，返回 false
    }

    /*
    过滤url
     */
    private boolean doPass(String uri) {
        if (containsSpecialUrl(uri)) {
            return true;
        }
        if (containsPassUrl(uri)) {
            return true;
        }
        return false;
    }

}
