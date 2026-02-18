package com.ddzn.dd.framework.common.core.wechat;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 微信配置
 *
 * @author dax
 */
@Configuration
@ConfigurationProperties(prefix = "wechat")
@Data
public class WxProperties {

    private String appId;

    private String appSecret;

    private String mchId;

    private String mchKey;

    private String notifyUrl;

    private String keyPath;

    private String refundNotifyUrl;

    private String state;

    private Wx wx;

    public static class Wx {
        private boolean enable;
        private List<Map<String, String>> template = new ArrayList<>();

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public List<Map<String, String>> getTemplate() {
            return template;
        }

        public void setTemplate(List<Map<String, String>> template) {
            this.template = template;
        }
    }

    public static final String SUCCESS = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
            + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";

    public static final String FAIL = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
            + "<return_msg><![CDATA[]]></return_msg>" + "</xml> ";

}
