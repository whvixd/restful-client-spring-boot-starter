package com.github.whvixd.restful.client.spring.boot.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wangzhixiang on 2022/02/18.
 */
@Configuration
@EnableConfigurationProperties(RestfulClientProperties.class)
public class RestfulClientAutoConfiguration {
    private final RestfulClientProperties restfulClientProperties;

    @Autowired
    public RestfulClientAutoConfiguration(RestfulClientProperties restfulClientProperties) {
        this.restfulClientProperties = restfulClientProperties;
    }

}
