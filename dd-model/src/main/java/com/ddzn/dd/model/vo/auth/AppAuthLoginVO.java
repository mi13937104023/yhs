package com.ddzn.dd.model.vo.auth;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/***
 * 登录后返回token
 * @author chh
 * @date 2022/5/7  下午 14:24
 */
@Data
public class AppAuthLoginVO {

    @ApiModelProperty(value = "token")
    private String token;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("生日")
    private LocalDate birthday;

    @ApiModelProperty("性别（1男，2女，0未知）")
    private Integer gender;

    @ApiModelProperty("IP属地")
    private String ipCity;

    @ApiModelProperty(value = "错误码", hidden = true)
    private String code;


}
