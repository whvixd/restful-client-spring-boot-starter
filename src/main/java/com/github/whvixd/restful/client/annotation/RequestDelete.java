package com.github.whvixd.restful.client.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * delete请求方式
 * <p>
 * Created by wangzhx on 2018/11/6.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RequestDelete {
    String path();

    String message() default "";
}