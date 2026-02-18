package com.ddzn.dd.gateway.model;

import lombok.Data;

@Data
public class AppRulesInfo {
    private Integer id;
    private String version;
    private String appkey;
    private String secret;
    private String h5;
    private String name;
    private String encrypt;
}
