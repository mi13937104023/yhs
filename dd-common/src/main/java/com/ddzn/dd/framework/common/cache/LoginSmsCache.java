package com.ddzn.dd.framework.common.cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * 记录登录手机验证码
 * @author DaiJuzheng
 * @date 2023-05-06 17:25
 **/
@Component
public class LoginSmsCache {

    private static final String TOKEN_CACHE = "phone_sms";


    @CachePut(value = TOKEN_CACHE, key = "#phone")
    public String putToken(String phone, String code) {
        return code;
    }


    @CacheEvict(value = TOKEN_CACHE, key = "#phone")
    public void expire(String phone) {
    }

    @Cacheable(value = TOKEN_CACHE, key = "#phone")
    public String getCode(String phone) {
        return null;
    }


}
