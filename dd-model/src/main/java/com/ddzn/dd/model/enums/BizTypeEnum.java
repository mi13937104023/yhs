package com.ddzn.dd.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 验证码发送模板
 *
 * @author Daijuzheng
 * @date 2022/5/19 11:12
 */
@Getter
public enum BizTypeEnum {

    FAQ(10, "FAQ管理"),
    ;

    private final Integer type;
    private final String desc;

    BizTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static String getTitleByType(Integer type) {
        for (BizTypeEnum item : BizTypeEnum.values()) {
            if (item.getType().equals(type)) {
                return item.getDesc();
            }
        }
        return "";
    }
}