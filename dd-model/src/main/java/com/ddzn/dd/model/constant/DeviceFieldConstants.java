package com.ddzn.dd.model.constant;

/**
 * Redis Hash 字段名常量类
 * <p>
 * 用于统一维护 Redis 存储的 Hash 结构中字段的 key，
 * 避免硬编码字符串，方便统一管理和调用。
 * <p>
 * 注意：该类仅包含常量，不可实例化。
 */
public final class DeviceFieldConstants {

    /**
     * 设备ID
     */
    public static final String DEVICE_ID = "deviceId";

    /**
     * 设备IMEI号
     */
    public static final String IMEI = "imei";

    /**
     * 开机时间
     */
    public static final String ONLINE_TIME = "onlineTime";

    /**
     * 离线时间
     */
    public static final String OFFLINE_TIME = "offlineTime";

    /**
     * 激活状态
     */
    public static final String ACTIVE_STATUS = "activeStatus";


    /**
     * 激活状态
     */
    public static final String ACTIVE_TIME = "activeTime";

}
