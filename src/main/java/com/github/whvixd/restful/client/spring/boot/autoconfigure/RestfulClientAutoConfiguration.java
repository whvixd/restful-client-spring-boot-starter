package com.github.whvixd.restful.client.spring.boot.autoconfigure;

import com.github.whvixd.restful.client.core.RestfulClientActuator;
import com.github.whvixd.restful.client.core.RestfulClientDispatcher;
import com.github.whvixd.restful.client.core.RestfulClientProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RestfulClient的spring-boot配置器
 * Created by whvixd on 2022/02/18.
 */
@Configuration
@EnableConfigurationProperties(RestfulClientProperties.class)
public class RestfulClientAutoConfiguration {
    private final RestfulClientProperties restfulClientProperties;

    @Autowired
    public RestfulClientAutoConfiguration(RestfulClientProperties restfulClientProperties) {
        this.restfulClientProperties = restfulClientProperties;
    }

    @Bean
    public RestfulClientActuator restfulClientActuator() {
        return new RestfulClientActuator(restfulClientProperties);
    }

    @Bean
    public RestfulClientDispatcher restfulClientDispatcher() {
        return new RestfulClientDispatcher();
    }

    @Bean
    public RestfulClientProxy restfulClientProxy() {
        return new RestfulClientProxy();
    }

}
