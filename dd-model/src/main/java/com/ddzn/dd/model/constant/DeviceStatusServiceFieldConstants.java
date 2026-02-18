package com.ddzn.dd.model.constant;

public class DeviceStatusServiceFieldConstants {

    /**
     * 设备状态字段名
     * 存储设备当前状态，如 ONLINE、OFFLINE 等
     */
    public static final String STATUS = "status";

    /**
     * 设备上次在线时间字段名
     * 存储设备最后一次上线时间，格式一般为北京时间字符串
     */
    public static final String LAST_ONLINE_TIME = "last_online_time";

    /**
     * 设备心跳时间字段名
     * 存储设备状态更新时间（心跳时间），格式一般为北京时间字符串
     */
    public static final String HEART_TIME = "status_update_time";

}
