package com.ddzn.dd.model.dto.auth;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WechatLoginDTO {
    @ApiModelProperty(value = "code")
    private String code;

    @ApiModelProperty(value = "用户的openId")
    private String openid;

    @ApiModelProperty("微信唯一uuid")
    private String unionid;

    @ApiModelProperty(value = "ip", hidden = true)
    private String ip;
}
