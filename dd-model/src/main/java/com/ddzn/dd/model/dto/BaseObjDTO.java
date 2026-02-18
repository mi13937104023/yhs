package com.ddzn.dd.model.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/***
 * 用户id单独dto
 * @date 2022/5/7  下午 15:43
 */
@Data
public class BaseObjDTO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty("1:启用 0:禁用")
    private Integer status;

}
