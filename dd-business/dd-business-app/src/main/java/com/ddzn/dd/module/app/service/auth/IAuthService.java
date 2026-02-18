package com.ddzn.dd.module.app.service.auth;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.ddzn.dd.model.dto.auth.WechatLoginDTO;
import com.ddzn.dd.model.vo.auth.AppAuthLoginVO;

import java.util.List;
import java.util.Map;

/**
 * 授权服务
 *
 * @author tjc
 */
public interface IAuthService {
    /**
     * 根据code换取openid
     *
     * @param code
     * @return
     */
    WxMaJscode2SessionResult swap(String code);

    /**
     * 微信登录
     *
     * @param dto
     * @return
     */
    AppAuthLoginVO wechatLogin(WechatLoginDTO dto) throws Exception;


    /**
     * 退出登录
     *
     * @param userId
     */
    void loginOut(Long userId);

}
