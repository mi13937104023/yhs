package com.ddzn.dd.framework.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 经纬度坐标地表直线距离计算（基于Haversine公式）
 */
public class GeoDistanceCalculator {
    // 地球平均半径，单位：公里
    private static final double EARTH_RADIUS = 6371.0;

    /**
     * 角度转弧度
     */
    private static double degreesToRadians(double degrees) {
        return degrees * Math.PI / 180.0;
    }

    /**
     * 计算两个经纬度点的地表直线距离
     *
     * @param lat1 第一个点的纬度
     * @param lon1 第一个点的经度
     * @param lat2 第二个点的纬度
     * @param lon2 第二个点的经度
     * @return 两点间地表距离（公里，保留2位小数）
     */
    public static double calculateGeoDistance(double lat1, double lon1, double lat2, double lon2) {
        // 转换为弧度
        double radLat1 = degreesToRadians(lat1);
        double radLon1 = degreesToRadians(lon1);
        double radLat2 = degreesToRadians(lat2);
        double radLon2 = degreesToRadians(lon2);

        // 计算纬度差和经度差
        double diffLat = radLat2 - radLat1;
        double diffLon = radLon2 - radLon1;

        // 应用Haversine公式
        double a = Math.sin(diffLat / 2) * Math.sin(diffLat / 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.sin(diffLon / 2) * Math.sin(diffLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 计算距离并保留2位小数
        double distance = EARTH_RADIUS * c;
        return new BigDecimal(distance * 1000)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}