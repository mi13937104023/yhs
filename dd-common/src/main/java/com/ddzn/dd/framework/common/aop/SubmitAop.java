package com.ddzn.dd.framework.common.aop;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.ddzn.dd.model.base.ResponseResult;
import com.ddzn.dd.model.constant.AuthConstants;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 防止重复点击
 *
 * @author DaiJuzheng
 * @date 2022/6/21 10:16
 **/
@Slf4j
@Aspect
@Configuration
@Order(1)
public class SubmitAop {

    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    private HttpServletRequest request;

    /**
     * 缓存时间：秒
     **/
    private static final int SECOND = 10;

    @Around("execution(public * *(..)) && @annotation(com.ddzn.dd.model.annotation.PreventDuplicateSubmission)")
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {

        if (request == null) {
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        }
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        String key = getCacheKey(method, pjp.getArgs());
        ValueOperations<String, Integer> opsForValue = redisTemplate.opsForValue();
        // 如果缓存中有这个url视为重复提交
        if (Boolean.TRUE.equals(opsForValue.setIfAbsent(key, 0, SECOND, TimeUnit.SECONDS))) {
            Object resObj;
            try {
                resObj = pjp.proceed();
            } finally {
                // 执行完之后清除token
                redisTemplate.delete(key);
            }
            return resObj;
        } else {
            log.warn("重复提交");
            ResponseResult<String> restBody = new ResponseResult<>();
            restBody.setMsg("请勿重复请求");
            restBody.setData("");
            restBody.setCode(HttpStatus.METHOD_NOT_ALLOWED.value());
            return restBody;
        }

    }

    /**
     * 组装缓存key
     *
     * @author DaiJuzheng
     * @date 2022/6/21 10:13
     **/
    private String getCacheKey(Method method, Object[] args) {

        String userId = request.getHeader(AuthConstants.CUSTOM_HEADER_KEY);
        if (StrUtil.isBlank(userId)) {
            return method.getName() + JSON.toJSONString(args);
        } else {
            return method.getName() + userId + JSON.toJSONString(args);
        }

    }

}