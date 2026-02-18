package com.ddzn.dd.framework.common.aop;

import com.ddzn.dd.model.enums.BusinessTypeEnum;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {
    String title() default "";

    BusinessTypeEnum businessType() default BusinessTypeEnum.OTHER;
}