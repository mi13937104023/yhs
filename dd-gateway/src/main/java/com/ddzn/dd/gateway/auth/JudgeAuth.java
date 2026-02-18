package com.ddzn.dd.gateway.auth;


import com.ddzn.dd.framework.common.config.MyGatewayProperties;
import com.ddzn.dd.framework.common.util.encrypt.Md5Utils;
import com.ddzn.dd.model.app.AppEntity;
import com.ddzn.dd.model.base.ResponseResult;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Objects;


/**
 * @Author: zhaopeng
 * @Date: 2020/06/08 11:11
 */
@Component
@Slf4j
public class JudgeAuth implements SignatureAuth {
    /**
     * 用户身份标识
     */
    static final String APP_KEY_SIGN = "appkey";
    static final String SIGN_PARAM = "sign";
    static final String TIMESTAMP_PARAM = "timestamp";
    static final String NONCESTR = "noncestr";
    static final String PARAM_ERROR = "参数不正确";

    @Resource
    private MyGatewayProperties myGatewayProperties;


    @Override
    public String getAppKey(ServerHttpRequest request) {
        return request.getHeaders().getFirst(APP_KEY_SIGN);
    }


    @Override
    public ResponseResult validParam(ServerHttpRequest request) {
        MultiValueMap<String, String> headQueryParams = request.getHeaders();
        if (!headQueryParams.containsKey(APP_KEY_SIGN)) {
            return ResponseResult.failed(PARAM_ERROR);
        }
        if (!headQueryParams.containsKey(SIGN_PARAM)) {
            return ResponseResult.failed(PARAM_ERROR);
        }
        if (!headQueryParams.containsKey(TIMESTAMP_PARAM)) {
            return ResponseResult.failed(PARAM_ERROR);
        }
        if (!headQueryParams.containsKey(NONCESTR)) {
            return ResponseResult.failed(PARAM_ERROR);
        }
        return null;
    }


    private static final ObjectMapper mapper = new ObjectMapper()
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

    /**
     * 提取参数（排除 lon/lat，可自定义）
     */
    public static MultiValueMap<String, String> getParamMap(ServerHttpRequest request, String method, String contentType, String bodyString) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        try {
            if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method)) {
                if (org.springframework.util.StringUtils.hasText(contentType)) {
                    if (contentType.contains("application/json")) {
                        parseJsonBody(bodyString, queryParams);
                    } else if (contentType.contains("application/x-www-form-urlencoded")) {
                        parseFormBody(bodyString, queryParams);
                    }
                }
            } else {
                request.getQueryParams().forEach((key, value) -> {
                    queryParams.add(key, value.get(0));
                });
            }
        } catch (Exception e) {
            log.error("解析请求参数异常: {}", e.getMessage());
        }

        return queryParams;
    }

    private static void parseJsonBody(String bodyString, MultiValueMap<String, String> queryParams) {
        try {
            JsonNode rootNode = mapper.readTree(bodyString);
            Iterator<String> fieldNames = rootNode.fieldNames();
            while (fieldNames.hasNext()) {
                String field = fieldNames.next();
                JsonNode value = rootNode.get(field);
                if (value.isFloatingPointNumber()) {
                    BigDecimal decimal = value.decimalValue();
                    queryParams.add(field, decimal.toPlainString());
                } else if (!value.isObject() && !value.isArray()) {
                    queryParams.add(field, value.asText());
                }
            }
        } catch (Exception e) {
            log.error("解析 JSON 参数出错: {}", e.getMessage());
        }
    }

    public static void parseFormBody(String body, MultiValueMap<String, String> queryParams) {
        if (body != null && !body.trim().isEmpty()) {
            String[] pairs = body.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=", 2);
                if (keyValue.length == 2) {
                    try {
                        String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8.name());
                        String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8.name());
                        queryParams.add(key, value);
                    } catch (Exception e) {
                    }
                }
            }
        }
    }


    /**
     * 校验签名3.0
     *
     * @param request
     * @param baseApp
     * @param method
     * @param contentType
     * @param bodyString
     * @return
     */
    @Override
    public ResponseResult checkSignature(ServerHttpRequest request, AppEntity baseApp, String method, String
            contentType, String bodyString) {
        String version = request.getHeaders().getFirst("version");
        String sign = Objects.requireNonNull(request.getHeaders().getFirst(SIGN_PARAM)).toLowerCase();
        String noncestr = request.getHeaders().getFirst(NONCESTR);
        String referer = request.getHeaders().getFirst("Referer");
        if (!myGatewayProperties.getWhiteHost().contains(request.getURI().getHost())) {
            if (referer != null && !referer.contains(myGatewayProperties.getDomain())) {
                //return ResponseResult.failed("非法请求");
            }
        }
        if (baseApp == null) {
            return ResponseResult.failed("身份验证失败");
        }
        long timestamp = Long.parseLong(Objects.requireNonNull(request.getHeaders().getFirst(TIMESTAMP_PARAM)));
        long currentTime = java.time.Instant.now().getEpochSecond();
        //将参数中的时间戳，与当前时间比较
        long diff = Math.abs(currentTime - timestamp);
        if (diff > 10) {
            return ResponseResult.failed("请求不在有效期内,过期时间：" + diff + "s");
        }
        StringBuilder stringBuilder = new StringBuilder();
        MultiValueMap<String, String> queryParams = getParamMap(request, method, contentType, bodyString);
        //除sign参数,按照参数名排序，然后key和value拼接起来
        queryParams.keySet().stream().sorted().forEach(x -> {
            stringBuilder.append(x).append(queryParams.getFirst(x));
        });
        //首尾拼接appkey
        stringBuilder.insert(0, baseApp.getAppKey());
        stringBuilder.append(baseApp.getAppSecret());
        stringBuilder.append(noncestr);
        stringBuilder.append(version);
        String signMd5;
        char[] newChar;
        signMd5 = Md5Utils.encode(Md5Utils.encode(stringBuilder.toString())).toLowerCase();
        char[] chars = signMd5.toCharArray();
        newChar = new char[]{chars[2], chars[4], chars[5], chars[7], chars[8], chars[10], chars[13], chars[14], chars[16], chars[17], chars[20], chars[23], chars[25], chars[27], chars[30], chars[31]};
        signMd5 = new String(newChar);

        if (!sign.equals(signMd5)) {
            //身份校验失败
            return ResponseResult.failed("签名不通过");
        }
        return null;
    }

}
