package com.ddzn.dd.model.constant;

public class DeviceLocationServiceFieldConstants {


    public static final String SERVICE_ID = "LocationServices";
    /**
     * 事件上报时间
     */
    public static final String EVENT_TIME = "event_time";
    /**
     * 设备运动状态（1静止，2运动，3落水）
     */
    public static final String PET_STATUS = "petStatus";

    /**
     * 今日运动步数
     */
    public static final String TODAYS_EXERCISE = "todaysExercise";

    /**
     * 今日运动步数
     */
    public static final String TODAYS_TOTAL_EXERCISE = "todaysTotalExercise";

    /**
     * 定位方式（0-GPS定位 1-北斗定位 2-GLONASS 3-Galileo 4-LBS定位 5-Wi-Fi定位 6-混合定位）
     */
    public static final String LOCATE_MODE = "locateMode";

    /**
     * 定位时间
     */
    public static final String TIME = "time";

    /**
     * GPS信息
     */
    public static final String GPS_INFO = "gpsInfo";

    /**
     * 基站信息
     */
    public static final String CELL_INFO = "cellInfo";

    /**
     * WiFi信息
     */
    public static final String WIFI_INFO = "wifiInfo";

    /**
     * 腾讯定位解析信息
     */
    public static final String Tencent_Location_Info = "tencentLocation";

    /**
     * 静止开始时间
     */
    public static final String DEVICE_STATIC_LAST_TIME_KEY = "last_static_time";

    /**
     * 静止持续时间
     */
    public static final String DEVICE_STATIC_MESSAGE_STATUS = "static_message_status";

    /**
     * 静止持续时间
     */
    public static final String DEVICE_LAST_STATUS = "device_last_status";

    public static final String DEVICE_STATIC_DURATION_TIME = "device_static_duration_time";
    public static final String DEVICE_STATIC_LAST_TIME = "device_static_last_time";

    public static final String DEVICE_SPORT_DURATION_TIME = "device_sport_duration_time";
    public static final String DEVICE_SPORT_LAST_TIME = "device_sport_last_time";

    public static final String DEVICE_WATER_DURATION_TIME = "device_water_duration_time";
    public static final String DEVICE_WATER_LAST_TIME = "device_water_last_time";
}
