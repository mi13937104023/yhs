package com.ddzn.dd.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用boolean枚举
 *
 * @author xgn
 * @date 2021/12/01
 **/
@Getter
public enum BooleanEnum {

    TRUE(1, "true"),
    FALSE(0, "false");

    private final Integer value;
    private final String desc;


    BooleanEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
