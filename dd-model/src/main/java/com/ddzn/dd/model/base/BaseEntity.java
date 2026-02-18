package com.ddzn.dd.model.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 */
@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键")
    private Long id;

    /*
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /*
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    /*
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createBy;

    /*
     * 更新人
     */
    @ApiModelProperty("更新人")
    private Long updateBy;

    /*
     * 状态 0 有效 1 无效
     */
    @ApiModelProperty("状态 0 有效 1 无效")
    private Integer disabled;

    /*
     * 状态 0 未删除 1 删除
     */
    @ApiModelProperty("状态 0 未删除 1 删除")
    private Integer deleted;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 租户id
     */
    @ApiModelProperty("租户id")
    private String tenantId;

    @ApiModelProperty("备注")
    private String remark;
}