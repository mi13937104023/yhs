package com.ddzn.dd.module.app.aop;

import com.ddzn.dd.framework.common.util.http.RequestContextHolderUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Slf4j
public class OperationLogAspect {

    @Pointcut("@annotation(com.ddzn.dd.framework.common.aop.OperationLog)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object result = null;
        Exception exp = null;
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Exception e) {
            exp = e;
            throw e;
        } finally {
            long cost = System.currentTimeMillis() - start;
            HttpServletRequest request = RequestContextHolderUtils.getRequest();
            //处理日志记录
        }
    }
}
