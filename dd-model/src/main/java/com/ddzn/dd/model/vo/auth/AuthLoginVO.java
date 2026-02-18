package com.ddzn.dd.model.vo.auth;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/***
 * 登录后返回token
 * @author chh
 * @date 2022/5/7  下午 14:24
 */
@Data
public class AuthLoginVO {

    @ApiModelProperty(value = "token")
    private String token;

    @ApiModelProperty(value = "用户名")
    private String userName;

}
