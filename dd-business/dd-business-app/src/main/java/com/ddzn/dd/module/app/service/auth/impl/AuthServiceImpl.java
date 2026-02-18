package com.ddzn.dd.module.app.service.auth.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ddzn.dd.framework.common.util.TokenUtils;
import com.ddzn.dd.framework.common.util.encrypt.AesUtils;
import com.ddzn.dd.model.base.BusinessException;
import com.ddzn.dd.model.base.UserTokenResult;
import com.ddzn.dd.model.constant.BoolConstants;
import com.ddzn.dd.model.constant.CommonConstants;
import com.ddzn.dd.model.dto.auth.WechatLoginDTO;
import com.ddzn.dd.model.entity.user.UserEntity;
import com.ddzn.dd.model.vo.auth.AppAuthLoginVO;
import com.ddzn.dd.module.app.service.auth.IAuthService;
import com.ddzn.dd.module.app.service.user.IUserService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author : jmking
 * create at:  20-03-18  16:14
 * @description: 授权服务实现类
 */
@Service
@Slf4j
public class AuthServiceImpl implements IAuthService {

    @Resource
    private WxMaService wxMaService;
    @Resource
    private IUserService userService;
    @Resource
    private TokenUtils tokenUtils;
    @Resource
    private RedisTemplate redisTemplate;


    /**
     * 根据code换取openid
     *
     * @param code
     * @return
     */
    @Override
    public WxMaJscode2SessionResult swap(String code) {
        try {
            WxMaJscode2SessionResult result = wxMaService.getUserService().getSessionInfo(code);
            return result;
        } catch (WxErrorException e) {
            log.error("获取微信session失败");
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 微信登陆
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppAuthLoginVO wechatLogin(WechatLoginDTO dto) throws Exception {
        AppAuthLoginVO result = new AppAuthLoginVO();
        WxMaPhoneNumberInfo phoneNoInfo;
        try {
            phoneNoInfo = wxMaService.getUserService().getPhoneNumber(dto.getCode());
        } catch (Exception e) {
            log.error("获取手机号失败-wechatLogin:{}", e.getMessage(), e);
            throw new BusinessException("获取用户手机号信息失败");
        }
        LambdaQueryWrapper<UserEntity> query = Wrappers.lambdaQuery();
        query.eq(UserEntity::getDeleted, BoolConstants.FALSE);
        query.eq(UserEntity::getWechatOpenid, dto.getOpenid());
        query.eq(UserEntity::getWechatUuid, dto.getUnionid());
        UserEntity user = userService.getOne(query);
        if (user != null) {
            String salt = phoneNoInfo.getPhoneNumber().substring(phoneNoInfo.getPhoneNumber().length() - 4);
            if (!AesUtils.encryptToHex(phoneNoInfo.getPhoneNumber(), salt).equals(user.getMobile())) {
                result.setCode("1");
                return result;
            }
        } else {
           //处理插入用户数据
        }
        String token = handleLogin(user);
        BeanUtils.copyProperties(user, result);
        if (phoneNoInfo.getPhoneNumber() == null || phoneNoInfo.getPhoneNumber().length() != 11) {
            //不处理
        } else {
            result.setMobile(phoneNoInfo.getPhoneNumber().substring(0, 3) + "****" + phoneNoInfo.getPhoneNumber().substring(7));
        }
        result.setToken(token);
        result.setCode("0");
        return result;
    }

    /**
     * 处理登陆
     *
     * @param user
     * @return
     */
    private String handleLogin(UserEntity user) {
        //生成token
        String token = "";
        try {
            UserTokenResult userTokenResult = new UserTokenResult();
            BeanUtils.copyProperties(user, userTokenResult);
            userTokenResult.setSource(CommonConstants.MINI_LABEL);
            userTokenResult.setUserId(user.getId());
            token = tokenUtils.generateToken(userTokenResult);
        } catch (Exception e) {
            log.error("微信登录生成JWT失败", e);
            throw new BusinessException("登录失败");
        }
        return token;
    }

    /**
     * 退出登录
     *
     * @param userId
     */
    @Override
    public void loginOut(Long userId) {
        tokenUtils.invalid(userId, CommonConstants.MINI_LABEL);
    }


}
