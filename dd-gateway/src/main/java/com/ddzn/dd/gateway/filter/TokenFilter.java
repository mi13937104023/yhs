package com.ddzn.dd.gateway.filter;

import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ddzn.dd.framework.common.config.MyGatewayProperties;
import com.ddzn.dd.framework.common.util.TokenUtils;
import com.ddzn.dd.gateway.service.GetService;
import com.ddzn.dd.model.base.BusinessException;
import com.ddzn.dd.model.constant.AuthConstants;
import com.ddzn.dd.model.constant.CommonConstants;
import com.ddzn.dd.model.constant.HttpStatusCodeConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class TokenFilter implements GlobalFilter, Ordered {

    @Resource
    private TokenUtils tokenUtils;
    @Resource
    private MyGatewayProperties myGatewayProperties;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        //是否需要token校验
        if (doPass(request.getURI().getPath(), myGatewayProperties.getExcludeUrl())) {
            return chain.filter(exchange);
        }
        //校验token
        String authorization = request.getHeaders().getFirst("Authorization");
        if (StringUtils.isBlank(authorization)) {
            throw new BusinessException(HttpStatusCodeConstants.NO_LOGIN.toString(), "token 为空");
        }
        log.info("authorization:{}", authorization);
        String[] split = authorization.split(" ");
        if (!authorization.startsWith("Bearer ") || split.length != 2) {
            throw new BusinessException(HttpStatusCodeConstants.NO_LOGIN.toString(), "token 格式不正确");
        }
        String source = "";
        try {
            String originalPath = exchange.getRequest().getURI().getPath(); // /system-business/user/info
            String servicePrefix = originalPath.split("/")[1]; // 获取 服务名
            log.info("servicePrefix:{},originalPath:{}", servicePrefix, originalPath);
            if (servicePrefix.equals("system-business")) {
                source = CommonConstants.MINI_LABEL;
            } else if (servicePrefix.equals("system-admin")) {
                source = CommonConstants.SYS_LABEL;
            } else {
                throw new BusinessException("非法请求");
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        if (!tokenUtils.isTokenValid(split[1], source)) {
            throw new BusinessException(HttpStatusCodeConstants.NO_LOGIN.toString(), "token 非法");
        }
        //用户信息的json放在header里面
        exchange.getRequest().mutate().header(AuthConstants.Authorization, authorization).build();
        return chain.filter(exchange);
    }

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 判断请求URI是否在白名单列表里
     *
     * @param uri            请求的完整URI，比如 /system-admin/auth/login-by-password
     * @param excludeUrlList 白名单列表，可以是具体路径或通配符路径，如 /system-admin/*, /system-admin/auth/**
     * @return 是否匹配白名单
     */
    public static boolean doPass(String uri, List<String> excludeUrlList) {
        if (containsSpecialUrl(uri)) {
            return true;
        }
        if (containsPassUrl(uri, excludeUrlList)) {
            return true;
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 1;
    }


    private static final String[] urls = {"swagger-resources", "webjars", "api-docs", "swagger-ui.html", "swagger-ui", "doc.html"};

    public static boolean containsSpecialUrl(String path) {
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


    public static boolean containsPassUrl(String path, List<String> excludeUrlList) {
        if (path == null || path.isEmpty()) {
            return false;
        }
        if (excludeUrlList == null || excludeUrlList.isEmpty()) {
            return false;
        }
        for (String url : excludeUrlList) {
            if (path.contains(url)) {
                return true;  // 只要包含任意一个，就返回 true
            }
        }
        return false; // 全部都不包含，返回 false
    }

}
