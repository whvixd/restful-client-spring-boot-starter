package com.github.whvixd.restful.client.annotation;

import com.github.whvixd.restful.client.spring.override.RestfulClientScannerRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 扫描RestfulClient的接口
 * Created by whvixd on 2022/02/18.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(RestfulClientScannerRegister.class)
public @interface RestfulClientScan {
    String[] value() default {};

    String[] basePackages() default {};

    // TODO
    Class<?>[] basePackageClasses() default {};
}
