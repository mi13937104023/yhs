package com.ddzn.dd.module.app.controller.auth;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.ddzn.dd.framework.common.util.security.IpUtils;
import com.ddzn.dd.model.base.ResponseResult;
import com.ddzn.dd.model.dto.auth.WechatLoginDTO;
import com.ddzn.dd.model.vo.auth.AppAuthLoginVO;
import com.ddzn.dd.module.app.base.BaseController;
import com.ddzn.dd.module.app.service.auth.IAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 登录
 */
@Api(tags = "登录")
@RestController
@RequestMapping("auth")
public class AuthController extends BaseController {

    @Resource
    private IAuthService authService;

    @ApiOperation("根据code换取openid")
    @GetMapping("/swap")
    public ResponseResult swap(String code) {
        WxMaJscode2SessionResult sessionInfo = authService.swap(code);
        return ResponseResult.success(sessionInfo);
    }


    @ApiOperation("微信登录")
    @PostMapping("/wechat-login")
    public ResponseResult<?> wechatLogin(@RequestBody WechatLoginDTO dto) throws Exception {
        String ip = IpUtils.getClientIp(request);
        dto.setIp(ip);
        AppAuthLoginVO result = authService.wechatLogin(dto);
        if (result != null && result.getCode().equals("1")) {
            return ResponseResult.failed(1001, "登录手机号和系统手机号不一致");
        }
        return ResponseResult.success("登录成功", result);
    }


    @ApiOperation("退出登录")
    @PostMapping("/login-out")
    public ResponseResult<String> loginOut() {
        authService.loginOut(currentUserId());
        return ResponseResult.success("退出成功");
    }


}
