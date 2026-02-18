package com.ddzn.dd.framework.common.util.security;


import cn.hutool.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import static cn.hutool.core.net.NetUtil.getMultistageReverseProxyIp;

/**
 * The type Ip util.
 *
 * @author dax
 */
@Slf4j
public class IpUtils {


    private IpUtils() {
    }


    /**
     * 获取客户端IP<br>
     * 默认检测的Header：<br>
     * 1、X-Forwarded-For<br>
     * 2、X-Real-IP<br>
     * 3、Proxy-Client-IP<br>
     * 4、WL-Proxy-Client-IP<br>
     * otherHeaderNames参数用于自定义检测的Header
     *
     * @param request          请求对象
     * @param otherHeaderNames 其他自定义头文件
     * @return IP地址 string
     */
    public static String obtainClientIp(HttpServletRequest request, String... otherHeaderNames) {

        String[] headers = {"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
        if (ArrayUtil.isNotEmpty(otherHeaderNames)) {
            headers = ArrayUtil.addAll(headers, otherHeaderNames);
        }

        String ip;
        for (String header : headers) {
            ip = request.getHeader(header);
            if (!StringUtils.isEmpty(ip)) {
                return getMultistageReverseProxyIp(ip);
            }
        }
        ip = request.getRemoteAddr();
        return getMultistageReverseProxyIp(ip);

    }

    /**
     * 获取本地IP地址
     */
    public static String getLocalIP() {
        if (isWindowsOS()) {
            try {
                return InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                log.error("获取IP地址异常！");
                e.printStackTrace();
                return null;
            }
        } else {
            return getLinuxLocalIp();
        }
    }

    /**
     * 判断操作系统是否是Windows
     */
    public static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }

    /**
     * 获取本地Host名称
     */
    public static String getLocalHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取Linux下的IP地址
     *
     * @return IP地址
     * @throws
     */
    private static String getLinuxLocalIp() {
        String ip = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                String name = intf.getName();
                if (!name.contains("docker") && !name.contains("lo")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress().toString();
                            if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80")) {
                                ip = ipaddress;
                            }
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            log.error("获取IP地址异常！");
            ip = null;
            ex.printStackTrace();
        }
        return ip;
    }

    /**
     * 获取用户真实IP地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    public static String getClientIp(HttpServletRequest request) {
        // 1. 优先读取由网关传递过来的 X-Real-IP 头
        String ip = request.getHeader("X-Real-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        // 2. 作为备选，也可以读取标准的 X-Forwarded-For 头
        ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.contains(",")) {
                ip = ip.split(",")[0].trim();
            }
            return ip;
        }

        // 3. 如果以上都没有，则获取原始请求地址（这通常是网关的IP）
        ip = request.getRemoteAddr();

        // 处理本地和局域网IP的显示
        if ("0:0:0:0:0:0:0:1".equals(ip) || "127.0.0.1".equals(ip)) {
            return "本地网络";
        }

        return ip;
    }


}
