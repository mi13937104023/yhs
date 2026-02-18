package com.ddzn.dd.model.constant;

/**
 * @author tjc
 * @date 2020-07-14 18:07
 */
public class CommonConstants {
    private CommonConstants() {
    }

    /**
     * token 在http头的字段名
     */
    public static final String TOKEN_KEY = "Authorization";

    /**
     * 根目录Id
     */
    public static final Long ROOT_RESOURCE_ID = 1L;
    /**
     * 根目录Id
     */
    public static final Long MINI_ROOT_RESOURCE_ID = 2L;

    /**
     * 系统管理员角色ID
     */
    public static final Long ADMIN_ROLE_ID = 1L;

    /**
     * 新建用户默认密码
     */
    public static final String DEFAULT_PWD = "123456";


    /***
     * 11位手机号
     */
    public static final String PHONE_ONLY_MATCH_11_NUM = "^1\\d{10}$";

    /**
     * 验证码登录最大错误数
     **/
    public static final Integer SMS_LOGIN_FAIL_MAX = 5;

    /**
     * 用户默认头像
     */
    public static final String DEFAULT_AVATAR_URL = "";

    /**
     * 家庭默认头像
     */
    public static final String DEFAULT_FAMILY_AVATAR_URL = "";

    /**
     * 后台管理系统
     */
    public static final String SYS_LABEL = "sys";

    /**
     * 小程序
     */
    public static final String MINI_LABEL = "mini";


}
