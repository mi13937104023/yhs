package com.ddzn.dd.gateway.auth;


import com.ddzn.dd.model.app.AppEntity;
import com.ddzn.dd.model.base.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zhaopeng
 * @Date: 2020/06/08 11:11
 */
@Component
@Slf4j
public class SignatureAuthFactory {
    @Autowired
    private ApplicationContext applicationContext;

    private Map<String, SignatureAuth> signatureAuthMap;

    /**
     * 获取认证身份的方式
     *
     * @param exchange
     * @param signClass
     * @return
     */
    public String getAppKey(ServerWebExchange exchange, String signClass) {
        SignatureAuth signatureAuth = signatureAuthMap.get(signClass);
        String appkey = signatureAuth.getAppKey(exchange.getRequest());
        log.info("getAppKey-appkey:{}", appkey);
        return appkey;
    }

    /**
     * 校验参数
     *
     * @return
     */
    public ResponseResult validParam(ServerWebExchange exchange, String signClass) {
        SignatureAuth signatureAuth = signatureAuthMap.get(signClass);
        return signatureAuth.validParam(exchange.getRequest());
    }

    @PostConstruct
    private Map<String, SignatureAuth> signatureAuthMap() {
        this.signatureAuthMap = new HashMap<>();
        String[] result = applicationContext.getBeanNamesForType(SignatureAuth.class);
        for (String clazz : result) {
            SignatureAuth bean = (SignatureAuth) applicationContext.getBean(clazz);
            signatureAuthMap.put(clazz, bean);
        }
        return signatureAuthMap;
    }

    /**
     * 校验签名3.0
     * @param exchange
     * @param appRulesInfo
     * @param authClass
     * @param method
     * @param contentType
     * @param bodyString
     * @return
     */
    public ResponseResult checkSignature(ServerWebExchange exchange, AppEntity appRulesInfo, String authClass, String method, String contentType, String bodyString) {
        SignatureAuth signatureAuth = signatureAuthMap.get(authClass);
        return signatureAuth.checkSignature(exchange.getRequest(), appRulesInfo, method, contentType, bodyString);
    }
}
