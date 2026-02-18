package com.ddzn.dd.model.app;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ddzn.dd.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 */
@Data
@ApiModel(description = "app ", value = "app ")
@TableName("base_app")
public class AppEntity extends BaseEntity {
    /**
     * 应用名字
     */
    @ApiModelProperty("应用名字")
    private String appName;

    /**
     * 应用简介
     */
    @ApiModelProperty("应用简介")
    private String appDesc;

    /**
     * 应用key
     */
    @ApiModelProperty("应用key")
    private String appKey;

    /**
     * 应用密钥
     */
    @ApiModelProperty("应用密钥")
    private String appSecret;
    /**
     * 状态（1：启用 2：禁用 -1：删除）
     */
    @ApiModelProperty("状态（1：启用 2：禁用 -1：删除）")
    private Integer status = 1;
    /**
     * 版本号
     */
    private String version;

}
