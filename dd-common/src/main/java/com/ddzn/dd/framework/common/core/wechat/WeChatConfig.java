package com.ddzn.dd.framework.common.core.wechat;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author dax
 * @since 10:50
 **/
@Configuration
public class WeChatConfig {
    @Resource
    private WxProperties properties;


    @Bean("wxMaConfig")
    public WxMaConfig wxMaConfig() {
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(properties.getAppId());
        config.setSecret(properties.getAppSecret());
        return config;
    }


    @Bean("wxMaService")
    public WxMaService wxMaService(@Qualifier(value = "wxMaConfig") WxMaConfig wxMaConfig) {
        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(wxMaConfig);
        return service;
    }

}
