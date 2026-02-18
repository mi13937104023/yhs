package com.ddzn.dd.model.entity.user;


import com.baomidou.mybatisplus.annotation.TableName;
import com.ddzn.dd.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ApiModel("用户实体")
@TableName("user")
public class UserEntity extends BaseEntity {

    @ApiModelProperty(value = "手机号", required = true)
    private String mobile;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "生日")
    private LocalDate birthday;

    @ApiModelProperty(value = "性别（1男，2女，0未知）", example = "0")
    private Integer gender;

    @ApiModelProperty(value = "IP")
    private String ip;

    @ApiModelProperty(value = "最后登录时间")
    private LocalDateTime lastLoginTime;

    @ApiModelProperty(value = "微信unionid")
    private String wechatUuid;

    @ApiModelProperty(value = "小程序openid")
    private String wechatOpenid;

    @ApiModelProperty(value = "IP属地")
    private String ipCity;

    @ApiModelProperty(value = "当前家庭id")
    private Long familyId;

    @ApiModelProperty(value = "达点ID")
    private String ddId;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "状态（1:正常 2:禁用 3:拉黑 4:注销）", example = "1")
    private Integer status;

    @ApiModelProperty(value = "异常时间")
    private LocalDateTime exTime;

    @ApiModelProperty(value = "拉黑时间")
    private LocalDateTime blockTime;

    @ApiModelProperty(value = "注销时间")
    private LocalDateTime cancelTime;

    @ApiModelProperty(value = "注销原因")
    private String cancelReason;

    @ApiModelProperty(value = "拉黑原因")
    private String blockReason;

    @ApiModelProperty(value = "渠道（10:微信小程序 20:抖音小程序 30:支付宝小程序 40:安卓 50:ios 60:鸿蒙）")
    private Integer source;

    @ApiModelProperty("加密盐")
    private String salt;

}