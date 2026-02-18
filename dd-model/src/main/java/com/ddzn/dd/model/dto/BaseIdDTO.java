package com.ddzn.dd.model.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/***
 * 用户id单独dto
 * @date 2022/5/7  下午 15:43
 */
@Data
public class BaseIdDTO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty("创建/修改人")
    private Long userId;

}
