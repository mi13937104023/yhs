package com.ddzn.dd.gateway.auth;

import com.ddzn.dd.model.app.AppEntity;
import com.ddzn.dd.model.base.ResponseResult;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;

/**
 * @Author: zhaopeng
 * @Date: 2020/06/08 11:11
 */
public interface SignatureAuth {
    /**
     * 获取用户唯一标识
     *
     * @param request
     * @return
     */
    String getAppKey(ServerHttpRequest request);


    ResponseResult validParam(ServerHttpRequest request);

    /**
     * 签名校验3.0
     *
     * @param request
     * @param appRulesInfo
     * @param method
     * @param contentType
     * @param bodyString
     * @return
     */
    ResponseResult checkSignature(ServerHttpRequest request, AppEntity appRulesInfo, String method, String contentType, String bodyString);
}
