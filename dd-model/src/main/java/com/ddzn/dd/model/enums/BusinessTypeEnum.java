package com.ddzn.dd.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public enum BusinessTypeEnum {

    OTHER(0, "其它"),
    INSERT(1, "新增"),
    UPDATE(2, "修改"),
    DELETE(3, "删除"),
    SELECT(4, "查询"),
    EXPORT(5, "导出"),
    IMPORT(6, "导入"),
    FORCE_LOGOUT(7, "强退"),
    CLEAN(8, "清空数据"),
    LOGIN(9, "登录"),
    LOGINOUT(10, "退出登录");

    private final Integer value;
    private final String desc;

    BusinessTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
