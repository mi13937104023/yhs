package com.ddzn.dd.framework.common.aop;

import com.ddzn.dd.model.annotation.LogRecordPoint;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("@annotation(com.ddzn.dd.model.annotation.LogRecordPoint)")
    public void pointcut() {
    }

    @AfterReturning(pointcut = "pointcut()", returning = "jsonResponse")
    public void doReturningLogRecord(JoinPoint joinPoint, Object jsonResponse) {
        recordLog(joinPoint, null, jsonResponse);
    }

    @AfterThrowing(value = "pointcut()", throwing = "e")
    public void doThrowingLogRecord(JoinPoint joinPoint, Exception e) {
        recordLog(joinPoint, e, null);
    }

    private void recordLog(JoinPoint joinPoint, Exception e, Object jsonResponse) {
        LogRecordPoint logAnnotation = getAnnotation(joinPoint);
        if (logAnnotation != null) {
            //TODO: 记录日志的具体逻辑 如：获取各种信息，写入数据库等操作...
        }
    }

    /**
     * 通过反射获取注解
     */
    private LogRecordPoint getAnnotation(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            return method.getAnnotation(LogRecordPoint.class);
        }
        return null;
    }

}

