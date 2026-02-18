package com.ddzn.dd.model.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * token里存储的用户信息
 */
@Data
@ApiModel("token里存储的用户信息")
public class UserTokenResult {
    /**
     * 用户编号
     */
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    private String nickName;
    /**
     * 用户真实名字
     */
    @ApiModelProperty(value = "用户真实名字")
    private String realName;

    /**
     * 简称
     */
    @ApiModelProperty("简称")
    private String shortName;
    /**
     * 用户手机号
     */
    @ApiModelProperty(value = "用户手机号")
    private String mobile;
    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "sys:后台管理 mini:小程序", hidden = true)
    private String source;


}
