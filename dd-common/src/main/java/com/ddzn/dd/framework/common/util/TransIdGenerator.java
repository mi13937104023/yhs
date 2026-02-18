package com.ddzn.dd.framework.common.util;


import java.util.concurrent.atomic.AtomicInteger;

public class TransIdGenerator {
    // 自增序列（原子类保证线程安全）
    private static final AtomicInteger SEQUENCE = new AtomicInteger(0);

    public static String generate8BitTransId() {
        long timestamp = System.currentTimeMillis();
        // 取时间戳后7位
        String timePart = String.format("%07d", timestamp % 10000000);

        // 同一毫秒内自增，取个位数（0-9循环）
        int sequence = SEQUENCE.getAndIncrement() % 10;

        // 拼接成8位
        return timePart + sequence;
    }
}
