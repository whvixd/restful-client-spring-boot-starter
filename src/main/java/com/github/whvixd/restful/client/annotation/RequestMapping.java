package com.github.whvixd.restful.client.annotation;

import com.github.whvixd.restful.client.support.CodeResolver;
import com.github.whvixd.restful.client.support.DefaultCodeResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识该接口为客户端
 * Created by whvixd on 2022/2/18.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RequestMapping {
    String path() default "127.0.0.1:8080";

    String message() default "";

    /**
     * 解码，编码解析器，默认为{@link DefaultCodeResolver}
     */
    Class<? extends CodeResolver> coder() default DefaultCodeResolver.class;
}
