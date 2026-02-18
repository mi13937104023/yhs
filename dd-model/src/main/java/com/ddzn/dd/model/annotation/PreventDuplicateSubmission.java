package com.ddzn.dd.model.annotation;

import java.lang.annotation.*;

/**
 * 防止重复点击
 *
 * @author DaiJuzheng
 * 2022/6/21 9:55
 **/
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PreventDuplicateSubmission {

}
