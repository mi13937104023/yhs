package com.ddzn.dd.model.constant;

/**
 * @author DaiJuZheng
 * @date 2023/05/08 09:47:15
 **/
public class LoginCacheKeyConstant {

    private final static String prefix = "system::login::";

    private static final String SMS_LOGIN_FAIL_NUMS_KEY = "sms_fail::nums";
    private static final String SMS_LOGIN_LOCK_KEY = "sms_fail::lock";

    public static String getLockKey(String phone) {
        return prefix + "::" + SMS_LOGIN_LOCK_KEY + "::" + phone;
    }

    public static String getFailNumsKey(String phone) {
        return prefix + "::" + SMS_LOGIN_FAIL_NUMS_KEY + "::" + phone;
    }

}
