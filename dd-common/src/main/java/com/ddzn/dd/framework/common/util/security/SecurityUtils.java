package com.ddzn.dd.framework.common.util.security;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.ddzn.dd.model.base.ResponseResult;
import lombok.experimental.UtilityClass;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**

 * @Version 1.0
 */
@UtilityClass
public class SecurityUtils {

    public void writeJavaScript(ResponseResult commonResult, HttpServletResponse response) throws IOException {
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.write(JSON.toJSONString(commonResult));
        printWriter.flush();
    }

    /**
     * 获取request
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = getRequestAttributes();
        if(ObjectUtil.isNotNull(requestAttributes)) {
            return getRequestAttributes().getRequest();
        }
        return null;
    }

    /**
     * 获取response
     */
    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    public static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }
}
