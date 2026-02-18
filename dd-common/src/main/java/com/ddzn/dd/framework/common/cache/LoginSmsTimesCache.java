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
public class LoginSmsTimesCache {

    private static final String TOKEN_CACHE = "phone_sms_times";


    @CachePut(value = TOKEN_CACHE, key = "#phone")
    public String put(String phone) {
        return phone;
    }


    @CacheEvict(value = TOKEN_CACHE, key = "#phone")
    public void expire(String phone) {
    }

    @Cacheable(value = TOKEN_CACHE, key = "#phone")
    public String getToken(String phone) {
        return null;
    }


}
