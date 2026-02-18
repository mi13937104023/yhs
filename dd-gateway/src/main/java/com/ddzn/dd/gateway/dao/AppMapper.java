package com.ddzn.dd.gateway.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ddzn.dd.model.app.AppEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * app
 */
@Mapper
public interface AppMapper extends BaseMapper<AppEntity> {

}
