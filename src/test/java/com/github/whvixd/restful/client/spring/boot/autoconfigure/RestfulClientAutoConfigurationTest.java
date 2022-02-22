package com.github.whvixd.restful.client.spring.boot.autoconfigure;

import com.github.whvixd.restful.client.annotation.RestfulClientScan;
import com.github.whvixd.restful.client.proxy.RestfulClientActuator;
import com.github.whvixd.restful.client.proxy.RestfulClientDispatcher;
import com.github.whvixd.restful.client.proxy.RestfulClientProxy;
import com.github.whvixd.restful.client.spring.boot.testclient.HelloRestfulClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

import static spark.Spark.*;

/**
 * Created by whvixd on 2022/02/22.
 */
public class RestfulClientAutoConfigurationTest {
    private AnnotationConfigApplicationContext context;
    private MockServer mockServer;

    @Before
    public void init() {
        this.context = new AnnotationConfigApplicationContext();
        intProperties();
        this.mockServer = new MockServer();
    }

    private void intProperties() {
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
    public void testClientBean() {
        this.context.register(RestfulClientAutoConfiguration.class, MybatisScanMapperConfiguration.class);
        this.context.refresh();
        Assert.assertNotNull(this.context.getBean(HelloRestfulClient.class));

    }

    @Test
    public void testClient() {
        this.context.register(RestfulClientAutoConfiguration.class, MybatisScanMapperConfiguration.class);
        this.context.refresh();
        mockServer.setupSpec();
        HelloRestfulClient helloRestfulClient = this.context.getBean(HelloRestfulClient.class);
        HashMap<String, String> mockReq = new HashMap<>();
        String helloGetRes = helloRestfulClient.helloGet(mockReq);
        Assert.assertEquals(helloGetRes, "{\"message\":\"Hello Get\"}");
        HashMap<String, Object> mockBody = new HashMap<>();
        String helloPostRes = helloRestfulClient.helloPost(mockReq, mockBody);
        Assert.assertEquals(helloPostRes, "{\"message\":\"Hello Post\"}");
        mockServer.cleanupSpec();
    }

    static class MockServer {
        void setupSpec() {
            port(8080);
            get("/hello/get", (req, res) -> "{\"message\":\"Hello Get\"}");
            post("/hello/post", (req, res) -> {
                String body = req.body();
                System.out.println(body);
                return "{\"message\":\"Hello Post\"}";
            });
            try {
                // 确保8080启动
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        void cleanupSpec() {
            stop();
        }
    }


}
