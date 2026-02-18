package com.ddzn.dd.framework.common.util.http;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class UserAgentUtils {

    public static String detectClientOS() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return detectClientOS(request);
    }

    public static String detectClientOS(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) return "Unknown";
        userAgent = userAgent.toLowerCase();

        if (userAgent.contains("android")) {
            return "Android";
        } else if (userAgent.contains("iphone") || userAgent.contains("ipad")) {
            return "iOS";
        } else if (userAgent.contains("windows")) {
            return "Windows";
        } else if (userAgent.contains("mac os")) {
            return "Mac";
        } else if (userAgent.contains("linux")) {
            return "Linux";
        } else {
            return "Other";
        }
    }
}
