package com.ddzn.dd.framework.common.util.date;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.ddzn.dd.model.base.BusinessException;
import com.ddzn.dd.model.enums.DateIntervalEnum;
import org.apache.commons.lang3.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static cn.hutool.core.date.DatePattern.UTC_MS_WITH_XXX_OFFSET_PATTERN;
import static cn.hutool.core.date.DatePattern.createFormatter;

/**
 * 时间工具类，用于 {@link LocalDateTime}
 *
 * @author 芋道源码
 */
public class LocalDateTimeUtils {

    /**
     * 空的 LocalDateTime 对象，主要用于 DB 唯一索引的默认值
     */
    public static LocalDateTime EMPTY = buildTime(1970, 1, 1);

    public static DateTimeFormatter UTC_MS_WITH_XXX_OFFSET_FORMATTER = createFormatter(UTC_MS_WITH_XXX_OFFSET_PATTERN);


    public static LocalDateTime addTime(Duration duration) {
        return LocalDateTime.now().plus(duration);
    }

    public static LocalDateTime minusTime(Duration duration) {
        return LocalDateTime.now().minus(duration);
    }

    public static boolean beforeNow(LocalDateTime date) {
        return date.isBefore(LocalDateTime.now());
    }

    public static boolean afterNow(LocalDateTime date) {
        return date.isAfter(LocalDateTime.now());
    }

    /**
     * 创建指定时间
     *
     * @param year  年
     * @param mouth 月
     * @param day   日
     * @return 指定时间
     */
    public static LocalDateTime buildTime(int year, int mouth, int day) {
        return LocalDateTime.of(year, mouth, day, 0, 0, 0);
    }

    public static LocalDateTime[] buildBetweenTime(int year1, int mouth1, int day1, int year2, int mouth2, int day2) {
        return new LocalDateTime[]{buildTime(year1, mouth1, day1), buildTime(year2, mouth2, day2)};
    }

    /**
     * 判指定断时间，是否在该时间范围内
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param time      指定时间
     * @return 是否
     */
    public static boolean isBetween(LocalDateTime startTime, LocalDateTime endTime, String time) {
        if (startTime == null || endTime == null || time == null) {
            return false;
        }
        return LocalDateTimeUtil.isIn(parse(time), startTime, endTime);
    }

    /**
     * 判断当前时间是否在该时间范围内
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 是否
     */
    public static boolean isBetween(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            return false;
        }
        return LocalDateTimeUtil.isIn(LocalDateTime.now(), startTime, endTime);
    }

    /**
     * 判断当前时间是否在该时间范围内
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 是否
     */
    public static boolean isBetween(String startTime, String endTime) {
        if (startTime == null || endTime == null) {
            return false;
        }
        LocalDate nowDate = LocalDate.now();
        return LocalDateTimeUtil.isIn(LocalDateTime.now(), LocalDateTime.of(nowDate, LocalTime.parse(startTime)), LocalDateTime.of(nowDate, LocalTime.parse(endTime)));
    }

    /**
     * 判断时间段是否重叠
     *
     * @param startTime1 开始 time1
     * @param endTime1   结束 time1
     * @param startTime2 开始 time2
     * @param endTime2   结束 time2
     * @return 重叠：true 不重叠：false
     */
    public static boolean isOverlap(LocalTime startTime1, LocalTime endTime1, LocalTime startTime2, LocalTime endTime2) {
        LocalDate nowDate = LocalDate.now();
        return LocalDateTimeUtil.isOverlap(LocalDateTime.of(nowDate, startTime1), LocalDateTime.of(nowDate, endTime1), LocalDateTime.of(nowDate, startTime2), LocalDateTime.of(nowDate, endTime2));
    }

    /**
     * 获取指定日期所在的月份的开始时间
     * 例如：2023-09-30 00:00:00,000
     *
     * @param date 日期
     * @return 月份的开始时间
     */
    public static LocalDateTime beginOfMonth(LocalDateTime date) {
        return date.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
    }

    /**
     * 获取指定日期所在的月份的最后时间
     * 例如：2023-09-30 23:59:59,999
     *
     * @param date 日期
     * @return 月份的结束时间
     */
    public static LocalDateTime endOfMonth(LocalDateTime date) {
        return date.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
    }

    /**
     * 获得指定日期所在季度
     *
     * @param date 日期
     * @return 所在季度
     */
    public static int getQuarterOfYear(LocalDateTime date) {
        return (date.getMonthValue() - 1) / 3 + 1;
    }

    /**
     * 获取指定日期到现在过了几天，如果指定日期在当前日期之后，获取结果为负
     *
     * @param dateTime 日期
     * @return 相差天数
     */
    public static Long between(LocalDateTime dateTime) {
        return LocalDateTimeUtil.between(dateTime, LocalDateTime.now(), ChronoUnit.DAYS);
    }

    /**
     * 获取指定日期到现在多少秒，如果指定日期在当前日期之后，获取结果为负
     *
     * @param dateTime 日期
     * @return 相差天数
     */
    public static Long betweenSecond(LocalDateTime dateTime) {
        return LocalDateTimeUtil.between(dateTime, LocalDateTime.now(), ChronoUnit.SECONDS);
    }


    /**
     * 获取今天的开始时间
     *
     * @return 今天
     */
    public static LocalDateTime getToday() {
        return LocalDateTimeUtil.beginOfDay(LocalDateTime.now());
    }

    /**
     * 获取昨天的开始时间
     *
     * @return 昨天
     */
    public static LocalDateTime getYesterday() {
        return LocalDateTimeUtil.beginOfDay(LocalDateTime.now().minusDays(1));
    }

    /**
     * 获取本月的开始时间
     *
     * @return 本月
     */
    public static LocalDateTime getMonth() {
        return beginOfMonth(LocalDateTime.now());
    }

    /**
     * 获取本年的开始时间
     *
     * @return 本年
     */
    public static LocalDateTime getYear() {
        return LocalDateTime.now().with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN);
    }

    public static List<LocalDateTime[]> getDateRangeList(LocalDateTime startTime, LocalDateTime endTime, Integer interval) {
        // 1.1 找到枚举
        DateIntervalEnum intervalEnum = DateIntervalEnum.valueOf(interval);
        Assert.notNull(intervalEnum, "interval({}} 找不到对应的枚举", interval);
        // 1.2 将时间对齐
        startTime = LocalDateTimeUtil.beginOfDay(startTime);
        endTime = LocalDateTimeUtil.endOfDay(endTime);

        // 2. 循环，生成时间范围
        List<LocalDateTime[]> timeRanges = new ArrayList<>();
        switch (intervalEnum) {
            case DAY:
                while (startTime.isBefore(endTime)) {
                    timeRanges.add(new LocalDateTime[]{startTime, startTime.plusDays(1).minusNanos(1)});
                    startTime = startTime.plusDays(1);
                }
                break;
            case WEEK:
                while (startTime.isBefore(endTime)) {
                    LocalDateTime endOfWeek = startTime.with(DayOfWeek.SUNDAY).plusDays(1).minusNanos(1);
                    timeRanges.add(new LocalDateTime[]{startTime, endOfWeek});
                    startTime = endOfWeek.plusNanos(1);
                }
                break;
            case MONTH:
                while (startTime.isBefore(endTime)) {
                    LocalDateTime endOfMonth = startTime.with(TemporalAdjusters.lastDayOfMonth()).plusDays(1).minusNanos(1);
                    timeRanges.add(new LocalDateTime[]{startTime, endOfMonth});
                    startTime = endOfMonth.plusNanos(1);
                }
                break;
            case QUARTER:
                while (startTime.isBefore(endTime)) {
                    int quarterOfYear = getQuarterOfYear(startTime);
                    LocalDateTime quarterEnd = quarterOfYear == 4 ? startTime.with(TemporalAdjusters.lastDayOfYear()).plusDays(1).minusNanos(1) : startTime.withMonth(quarterOfYear * 3 + 1).withDayOfMonth(1).minusNanos(1);
                    timeRanges.add(new LocalDateTime[]{startTime, quarterEnd});
                    startTime = quarterEnd.plusNanos(1);
                }
                break;
            case YEAR:
                while (startTime.isBefore(endTime)) {
                    LocalDateTime endOfYear = startTime.with(TemporalAdjusters.lastDayOfYear()).plusDays(1).minusNanos(1);
                    timeRanges.add(new LocalDateTime[]{startTime, endOfYear});
                    startTime = endOfYear.plusNanos(1);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid interval: " + interval);
        }
        // 3. 兜底，最后一个时间，需要保持在 endTime 之前
        LocalDateTime[] lastTimeRange = CollUtil.getLast(timeRanges);
        if (lastTimeRange != null) {
            lastTimeRange[1] = endTime;
        }
        return timeRanges;
    }

    /**
     * 格式化时间范围
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param interval  时间间隔
     * @return 时间范围
     */
    public static String formatDateRange(LocalDateTime startTime, LocalDateTime endTime, Integer interval) {
        // 1. 找到枚举
        DateIntervalEnum intervalEnum = DateIntervalEnum.valueOf(interval);
        Assert.notNull(intervalEnum, "interval({}} 找不到对应的枚举", interval);

        // 2. 循环，生成时间范围
        switch (intervalEnum) {
            case DAY:
                return LocalDateTimeUtil.format(startTime, DatePattern.NORM_DATE_PATTERN);
            case WEEK:
                return LocalDateTimeUtil.format(startTime, DatePattern.NORM_DATE_PATTERN) + StrUtil.format("(第 {} 周)", LocalDateTimeUtil.weekOfYear(startTime));
            case MONTH:
                return LocalDateTimeUtil.format(startTime, DatePattern.NORM_MONTH_PATTERN);
            case QUARTER:
                return StrUtil.format("{}-Q{}", startTime.getYear(), getQuarterOfYear(startTime));
            case YEAR:
                return LocalDateTimeUtil.format(startTime, DatePattern.NORM_YEAR_PATTERN);
            default:
                throw new IllegalArgumentException("Invalid interval: " + interval);
        }
    }

    /**
     * 格式化 LocalDateTime 为字符串（默认格式 yyyy-MM-dd HH:mm:ss）
     *
     * @param dateTime 时间
     * @return 格式化后的字符串；如果为空返回 null
     */
    public static String format(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 格式化 LocalDateTime 为指定格式的字符串
     *
     * @param dateTime 时间
     * @param pattern  格式，例如：yyyy/MM/dd HH:mm
     * @return 格式化后的字符串；如果为空返回 null
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 将字符串解析为 LocalDateTime（默认格式 yyyy-MM-dd HH:mm:ss）
     *
     * @param timeStr 时间字符串
     * @return LocalDateTime，如果为空或格式错误返回 null
     */
    public static LocalDateTime parse(String timeStr) {
        if (StrUtil.isBlank(timeStr)) {
            return null;
        }
        try {
            return LocalDateTime.parse(timeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (DateTimeParseException e) {
            // 尝试解析日期格式（yyyy-MM-dd）
            try {
                LocalDate date = LocalDate.parse(timeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                return date.atStartOfDay();
            } catch (DateTimeParseException ex) {
                return null;
            }
        }
    }

    /**
     * 将字符串解析为 LocalDateTime（指定格式）
     *
     * @param timeStr 时间字符串
     * @param pattern 格式，例如 yyyy/MM/dd HH:mm
     * @return LocalDateTime，如果为空或格式错误返回 null
     */
    public static LocalDateTime parse(String timeStr, String pattern) {
        if (StrUtil.isBlank(timeStr)) {
            return null;
        }
        try {
            return LocalDateTime.parse(timeStr, DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * 将 LocalDateTime 格式化后再解析回来（去除毫秒、纳秒部分）
     * <p>
     * 结果类似：2025-10-11 09:40:00
     *
     * @param dateTime 原始 LocalDateTime
     * @return 格式化后再解析回的 LocalDateTime；如果为空返回 null
     */
    public static LocalDateTime formatToLocalDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        try {
            String formatted = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return LocalDateTime.parse(formatted, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 比较 expTime 和 realExpTime 的大小
     *
     * @return 整数结果：
     * 1 - expTime 晚于 realExpTime
     * 0 - 两个时间相等（或其中一个/两个为空）
     * -1 - expTime 早于 realExpTime
     */
    public static int compareTime(String timeStr1, String timeStr2) {
        // 1. 非空校验：若任一时间为空，默认返回0（可根据业务调整兜底逻辑）
        if (StringUtils.isBlank(timeStr1) || StringUtils.isBlank(timeStr2)) {
            return 0;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            // 2. 将时间字符串解析为 LocalDateTime 对象（核心步骤：先解析，后比较）
            LocalDateTime time1 = LocalDateTime.parse(timeStr1, formatter);
            LocalDateTime time2 = LocalDateTime.parse(timeStr2, formatter);

            // 3. 三种比较方式（任选其一，效果一致）
            // 方式1：使用 compareTo 方法（推荐，返回int类型：负数=time1<time2；0=相等；正数=time1>time2）
            int compareResult = time1.compareTo(time2);
            return compareResult;
        } catch (Exception ex) {
            throw new BusinessException("compareTime出现异常:{}", ex.getMessage(), ex);
        }
    }
}
