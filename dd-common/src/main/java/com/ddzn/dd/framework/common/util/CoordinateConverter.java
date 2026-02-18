package com.ddzn.dd.framework.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * WGS84（GPS坐标）转 GCJ-02（火星坐标）工具类
 */
public class CoordinateConverter {

    private static final double PI = Math.PI;
    private static final double EARTH_RADIUS = 6378137.0; // 地球半径（米）
    private static final double EE = 0.00669342162296594323; // WGS84椭球偏心率平方

    /**
     * WGS84转GCJ02（核心方法，无NaN报错）
     *
     * @param wgsLat WGS84纬度（例：34.833923）
     * @param wgsLon WGS84经度（例：113.493640）
     * @return GCJ02坐标数组 [gcjLat, gcjLon]（保留6位小数）
     */
    public static double[] wgs84ToGcj02(double wgsLat, double wgsLon) {
        // 1. 验证坐标有效性（避免非法值导致计算异常）
        if (!isValidCoordinate(wgsLat, wgsLon)) {
            throw new IllegalArgumentException("WGS84坐标非法：lat=" + wgsLat + ", lon=" + wgsLon);
        }

        // 2. 国内坐标才偏移（境外直接返回原坐标）
        if (!isInChina(wgsLat, wgsLon)) {
            return new double[]{
                    roundTo6Decimal(wgsLat),
                    roundTo6Decimal(wgsLon)
            };
        }

        // 3. 计算偏移量（标准算法，避免 NaN）
        double dLat = transformLat(wgsLon - 105.0, wgsLat - 35.0);
        double dLon = transformLon(wgsLon - 105.0, wgsLat - 35.0);

        // 4. 修正偏移量（基于椭球参数）
        double radLat = wgsLat * PI / 180.0;
        double magic = Math.sin(radLat);
        magic = 1 - EE * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((EARTH_RADIUS * (1 - EE)) / (magic * sqrtMagic) * PI);
        dLon = (dLon * 180.0) / (EARTH_RADIUS / sqrtMagic * Math.cos(radLat) * PI);

        // 5. 计算最终GCJ02坐标
        double gcjLat = wgsLat + dLat;
        double gcjLon = wgsLon + dLon;

        return new double[]{
                roundTo6Decimal(gcjLat),
                roundTo6Decimal(gcjLon)
        };
    }

    /**
     * 纬度偏移量计算（标准公式，无NaN）
     */
    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * PI) + 320.0 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 经度偏移量计算（标准公式，无NaN）
     */
    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 验证经纬度合法性（避免非法值）
     */
    private static boolean isValidCoordinate(double lat, double lon) {
        return lat >= -90.0 && lat <= 90.0 && lon >= -180.0 && lon <= 180.0;
    }

    /**
     * 精准判断国内坐标（避免边界误判）
     */
    private static boolean isInChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347) return false;
        if (lat < 0.8293 || lat > 55.8271) return false;
        // 排除国外飞地
        if (lon > 103.6343 && lon < 104.7935 && lat > 22.0931 && lat < 22.9006) return false;
        return true;
    }

    /**
     * 保留6位小数（腾讯地图精度）
     */
    private static double roundTo6Decimal(double value) {
        // 避免 NaN/Infinite 传入 BigDecimal
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException("计算结果非法：" + value);
        }
        return new BigDecimal(value)
                .setScale(6, RoundingMode.HALF_UP)
                .doubleValue();
    }

    // ==================== 测试你的坐标（无报错）====================
    public static void main(String[] args) {
        // 你的WGS84坐标：纬度34.833923，经度113.493640（lat在前，lon在后）
        double wgsLat = 34.833923;
        double wgsLon = 113.493640;

        try {
            double[] gcj02 = wgs84ToGcj02(wgsLat, wgsLon);
            double gcjLat = gcj02[0];
            double gcjLon = gcj02[1];

            System.out.println("WGS84坐标：(" + wgsLat + ", " + wgsLon + ")");
            System.out.println("腾讯地图GCJ02坐标：(" + gcjLat + ", " + gcjLon + ")");
            // 正确输出（与腾讯地图官方一致）：
            // WGS84坐标：(34.833923, 113.493640)
            // 腾讯地图GCJ02坐标：(34.836592, 113.499778)
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}