package com.ddzn.dd.model.enums;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 枚举反序列化
 * Created by zhaopeng on 2020/06/29.
 */
@Slf4j
public class BaseCodeEnumDeserializer extends JsonDeserializer<BaseCodeEnum> {

    private static Map<String, BaseCodeEnum[]> enumCache = new HashMap<>();

    @Override
    public BaseCodeEnum deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String currFieldName = jsonParser.getParsingContext().getCurrentName();
        Class<?> aClass = jsonParser.getParsingContext().getCurrentValue().getClass();
        String className = aClass.getName();
        BaseCodeEnum[] baseCodeEnums = getEnumValues(className, currFieldName);

        if (baseCodeEnums == null) {
            try {
                baseCodeEnums = (BaseCodeEnum[]) aClass.getDeclaredField(currFieldName).getType().getEnumConstants();
                putEnumValues(className, currFieldName, baseCodeEnums);
            } catch (Exception e) {
                //throw e;
            }
        }

        for (BaseCodeEnum baseCodeEnum : baseCodeEnums) {
            if (baseCodeEnum.getCode() == jsonParser.getByteValue()) {
                return baseCodeEnum;
            }
        }

        return null;
    }

    private BaseCodeEnum[] putEnumValues(String className, String fieldName, BaseCodeEnum[] baseCodeEnum) {
        String key = className + ":" + fieldName;
        return enumCache.put(key, baseCodeEnum);
    }

    private BaseCodeEnum[] getEnumValues(String className, String fieldName) {
        return enumCache.get(className + ":" + fieldName);
    }
}
