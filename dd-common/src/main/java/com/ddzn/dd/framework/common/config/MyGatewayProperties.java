package com.ddzn.dd.framework.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RefreshScope
@ConfigurationProperties(prefix = "dd.gateway")
@Data
public class MyGatewayProperties {

    private String domain;

    private List<String> pass;

    private List<String> testAppkey;

    private List<String> excludeUrl;

    private String whiteHost;

}
