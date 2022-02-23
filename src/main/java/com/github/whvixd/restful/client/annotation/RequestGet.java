package com.github.whvixd.restful.client.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识为get请求方式
 * Created by whvixd on 2022/2/22.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RequestGet {
    String path();

    String message() default "";
}
