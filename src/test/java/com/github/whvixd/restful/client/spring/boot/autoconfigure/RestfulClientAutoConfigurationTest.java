package com.github.whvixd.restful.client.spring.boot.autoconfigure;

import com.github.whvixd.restful.client.annotation.RestfulClientScan;
import com.github.whvixd.restful.client.proxy.RestfulClientActuator;
import com.github.whvixd.restful.client.proxy.RestfulClientDispatcher;
import com.github.whvixd.restful.client.proxy.RestfulClientProxy;
import com.github.whvixd.restful.client.spring.boot.testclient.HelloResutfulClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wangzhixiang on 2022/02/22.
 */
public class RestfulClientAutoConfigurationTest {
    private AnnotationConfigApplicationContext context;

    @Before
    public void init() {
        this.context = new AnnotationConfigApplicationContext();
        TestPropertyValues.of("spring.restful.client.retryOnConnectionFailure:true").applyTo(this.context);
        TestPropertyValues.of("spring.restful.client.connectTimeout:10").applyTo(this.context);
        TestPropertyValues.of("spring.restful.client.readTimeout:10").applyTo(this.context);
        TestPropertyValues.of("spring.restful.client.connectionPoolMaxIdleConnections:10").applyTo(this.context);
        TestPropertyValues.of("spring.restful.client.connectionPoolKeepAliveDuration:10").applyTo(this.context);

    }

    @Configuration
    @EnableAutoConfiguration
    @RestfulClientScan(basePackages = "com.github.whvixd.restful.client.spring.boot.testclient")
    static class MybatisScanMapperConfiguration {
    }

    @Test
    public void testRestfulClientAutoConfiguration() {
        this.context.register(RestfulClientAutoConfiguration.class);
        this.context.refresh();
        Assert.assertNotNull(this.context.getBean(RestfulClientProperties.class));
        Assert.assertNotNull(this.context.getBean(RestfulClientActuator.class));
        Assert.assertNotNull(this.context.getBean(RestfulClientDispatcher.class));
        Assert.assertNotNull(this.context.getBean(RestfulClientProxy.class));
    }

    @Test
    public void testClient() {
        this.context.register(RestfulClientAutoConfiguration.class,MybatisScanMapperConfiguration.class);
        this.context.refresh();
        Assert.assertNotNull(this.context.getBean(HelloResutfulClient.class));

    }


}
