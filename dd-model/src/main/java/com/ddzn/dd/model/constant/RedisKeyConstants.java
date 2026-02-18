package com.ddzn.dd.model.constant;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * @author tjc
 * @date 2020-07-14 18:07
 */
public class RedisKeyConstants {
    /**
     * 后台管理登录token
     */
    public static final String sys_user_login_token = "dd::sys::user::token::";
    /**
     * 小程序登录token
     */
    public static final String mini_user_login_token = "dd::mini::user::token::";

    /**
     * 物联网卡token
     */
    public static final String iot_sim_token = "dd::mini::iot::sim::token";


    /**
     * 设备状态信息 Key
     * 示例：dd::device::status::{deviceId}
     */
    public static final String device_status_key = "dd::device::status::";
    /**
     * 设备属性-定位信息 Key
     * 示例：dd::device::location::{deviceId}
     */
    public static final String device_location_key = "dd::device::location::";

    /**
     * 设备属性-电池信息 Key
     * 示例：dd::device::battery::{deviceId}
     */
    public static final String device_battery_key = "dd::device::battery::";
    /**
     * 设备属性-网络信息 Key
     * 示例：dd::device::network::{deviceId}
     */
    public static final String device_network_key = "dd::device::network::";
    /**
     * 设备属性-用户服务信息 Key
     * 示例：dd::device::user::{deviceId}
     */
    public static final String device_user_key = "dd::device::user::";

    /**
     * Redis存储的键前缀
     */
    public static final String PET_STATUS_PREFIX = "dd::device::pet::status::";

    /**
     * 切换家庭
     */
    public static final String CURR_FAMILY_PREFIX = "dd::user::family::";

    /**
     * 设备高温告警IMEI集合
     */
    public static final String DEVICE_HIGH_TEMP_IMEI_SET = "dd::device::battery::temp";

}
