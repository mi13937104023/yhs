package com.ddzn.dd.framework.common.util.date;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

@Slf4j
public class TimeConverter {

    // 输入格式，比如 20250806T085217Z
    private static final DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssX");

    // 输出格式，比如 2025-08-06 16:52:17
    private static final DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String convertToBeijingTime(String utcTimeStr) {
        try {
            Instant instant = Instant.from(inputFormatter.parse(utcTimeStr));
            ZonedDateTime beijingTime = instant.atZone(ZoneId.of("Asia/Shanghai"));
            return beijingTime.format(outputFormatter);
        } catch (Exception e) {
            e.printStackTrace();
            return utcTimeStr; // 解析失败返回原串
        }
    }


    public static String convertUtcToBeijingTime(String utcTime) {
        if (StringUtils.isBlank(utcTime)) {
            return null;
        }
        try {
            // 假设传入时间格式为：2025-09-20T01:05:00Z
            Instant instant = Instant.parse(utcTime);
            ZonedDateTime beijingTime = instant.atZone(ZoneId.of("Asia/Shanghai"));
            return beijingTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            log.warn("时间格式转换失败: {}", utcTime, e);
            return utcTime; // 转换失败时保留原值
        }
    }

    /**
     * 将多种格式的时间对象转换为 LocalDateTime。
     * 支持：
     * - LocalDateTime 类型直接返回
     * - java.util.Date 类型转换
     * - ISO标准格式字符串，如 "2025-08-06T08:52:16"
     * - 带有毫秒的字符串，如 "2025-08-06T08:52:16.928"
     * - 纯数字时间字符串（yyyyMMddHHmmss），例如 "20250806085216"
     * - 其他常见格式可继续添加
     */
    public static LocalDateTime toLocalDateTime(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof LocalDateTime) {
            return (LocalDateTime) o;
        }
        if (o instanceof Date) {
            return LocalDateTime.ofInstant(((Date) o).toInstant(), ZoneId.systemDefault());
        }
        if (o instanceof String) {
            String str = ((String) o).trim();
            if (str.isEmpty()) {
                return null;
            }
            // 尝试多种格式解析
            DateTimeFormatter[] formatters = new DateTimeFormatter[]{
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                    DateTimeFormatter.ofPattern("yyyyMMddHHmmss"),
                    DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'"), // 形如20250806T085216Z
                    DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss.SSS'Z'") // 形如20250806T085216.928Z
            };
            for (DateTimeFormatter formatter : formatters) {
                try {
                    return LocalDateTime.parse(str, formatter);
                } catch (DateTimeParseException ignored) {
                }
            }
            // 解析失败时返回null或抛异常根据需求
        }
        return null;
    }
}
