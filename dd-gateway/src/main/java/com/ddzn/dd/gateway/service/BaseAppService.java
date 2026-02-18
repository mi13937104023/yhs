package com.ddzn.dd.gateway.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ddzn.dd.gateway.dao.AppMapper;
import com.ddzn.dd.model.app.AppEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Author: zhaopeng
 * @Date: 2020/06/10 17:24
 */
@Service
@Slf4j
public class BaseAppService {
    @Autowired
    AppMapper appMapper;

    public AppEntity queryByAppKey(String appkey, String version) {
        //根据appId读取密钥
        AppEntity baseApp = null;
        try {
            baseApp = appMapper.selectOne(new LambdaQueryWrapper<AppEntity>()
                    .eq(AppEntity::getAppKey, appkey)
                    .like(AppEntity::getVersion, "," + version + ",")
                    .orderByDesc(AppEntity::getCreateTime)
                    .last("limit 1")
            );
        } catch (Exception e) {
            log.error("baseApp解析失败:{}", e.getStackTrace());
        }
        return baseApp;
    }

}
