package com.ddzn.dd.module.app.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ddzn.dd.model.entity.user.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户信息
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
}
