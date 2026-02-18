package com.ddzn.dd.model.vo.auth;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 用户已有的资源（树形）
 *
 * @author DaiJuZheng
 * @date 2022/5/9 17:30
 **/
@Data
public class UserResourcesTreeVO {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "资源名称")
    private String resourceName;

    @ApiModelProperty(value = "父级ID")
    private String parentId;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "路由地址")
    private String path;

    @ApiModelProperty(value = "组件路径")
    private String component;

    @ApiModelProperty(value = "路由参数")
    private String router;

    @ApiModelProperty(value = "是否为外链（ 0是，1 否 ）")
    private Integer isFrame;

    @ApiModelProperty(value = "是否缓存（ 0 缓存，1 不缓存 ）")
    private Integer isCache;

    @ApiModelProperty(value = "菜单类型（ 1 菜单，2 按钮 ）")
    private Integer menuType;

    @ApiModelProperty(value = "菜单是否显示（  0 隐藏,，1 显示）")
    private Integer isShow;

    @ApiModelProperty(value = "是否禁用（ 0 正常，1 停用 ）")
    private Integer isDisabled;

    @ApiModelProperty(value = "权限标识")
    private String perms;

    @ApiModelProperty(value = "菜单图标")
    private String icon;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "子集")
    private List<UserResourcesTreeVO> childrenList;

}
