package com.ddzn.dd.module.app.service.user.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ddzn.dd.model.entity.user.UserEntity;
import com.ddzn.dd.module.app.mapper.user.UserMapper;
import com.ddzn.dd.module.app.service.user.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * 用户信息
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements IUserService {

}