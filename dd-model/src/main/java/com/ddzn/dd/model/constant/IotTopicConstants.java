package com.ddzn.dd.model.constant;

/**
 * 物联网平台topic
 */
public class IotTopicConstants {

    /**
     * 平台设置设备属性
     */
    public static final String setProperties = "$oc/devices/{device_id}/sys/properties/set/request_id={request_id}";

    /**
     * 平台查询设备属性
     */
    public static final String getProperties = "$oc/devices/{device_id}/sys/properties/get/request_id={request_id}";

    /**
     * 平台下发命令给设备
     */
    public static final String pushCommands = "$oc/devices/{device_id}/sys/properties/get/request_id={request_id}";

    /**
     * 平台事件下发
     */
    public static final String pushEvents = "$oc/devices/{device_id}/sys/events/down";

    /**
     * 平台下发消息
     */
    public static final String sendMsg = "$oc/devices/{device_id}/sys/messages/down";


}
