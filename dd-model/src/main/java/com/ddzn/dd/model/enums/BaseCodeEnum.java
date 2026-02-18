package com.ddzn.dd.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * 所有枚举必须实现的接口
 */
@JsonDeserialize(using = BaseCodeEnumDeserializer.class)
public interface BaseCodeEnum {

    @JsonValue
    byte getCode();
}
