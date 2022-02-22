package com.github.whvixd.restful.client.spring.boot.autoconfigure;

import com.alibaba.fastjson.JSONObject;
import com.github.whvixd.restful.client.annotation.RestfulClientScan;
import com.github.whvixd.restful.client.proxy.RestfulClientActuator;
import com.github.whvixd.restful.client.proxy.RestfulClientDispatcher;
import com.github.whvixd.restful.client.proxy.RestfulClientProxy;
import com.github.whvixd.restful.client.spring.boot.testclient.HelloRestfulClient;
import com.github.whvixd.restful.client.toolkit.FastJsonUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import spark.QueryParamsMap;

import java.util.HashMap;
import java.util.Map;

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
    static class RestfulClientScanConfiguration {
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
        this.context.register(RestfulClientAutoConfiguration.class, RestfulClientScanConfiguration.class);
        this.context.refresh();
        Assert.assertNotNull(this.context.getBean(HelloRestfulClient.class));

    }

    @Test
    public void testClientGet() {
        this.context.register(RestfulClientAutoConfiguration.class, RestfulClientScanConfiguration.class);
        this.context.refresh();
        mockServer.setupGetSpec();
        HelloRestfulClient helloRestfulClient = this.context.getBean(HelloRestfulClient.class);
        HashMap<String, String> mockReq = new HashMap<>();
        mockReq.put("get", "hello");
        String helloGetRes = helloRestfulClient.helloGet(mockReq);
        Assert.assertEquals(helloGetRes, "{\"message\":\"Hello Get\"}");

        Map<String, String> pathParam = new HashMap<>();
        pathParam.put("id", "1234");
        pathParam.put("type", "path");
        String helloGetPathRes = helloRestfulClient.helloGetPath(mockReq, pathParam);
        Assert.assertEquals(helloGetPathRes, "{\"message\":\"Hello Get Path\"}");

        Map<String, String> queryParam = new HashMap<>();
        queryParam.put("id", "1234");
        queryParam.put("name", "query");
        pathParam.put("type", "query");
        String helloGetQueryRes = helloRestfulClient.helloGetQuery(mockReq, pathParam, queryParam);
        Assert.assertEquals(helloGetQueryRes, "{\"message\":\"Hello Get Query\"}");
        mockServer.cleanupSpec();
    }

    @Test
    public void testClientPost() {
        this.context.register(RestfulClientAutoConfiguration.class, RestfulClientScanConfiguration.class);
        this.context.refresh();
        mockServer.setupPostSpec();
        HelloRestfulClient helloRestfulClient = this.context.getBean(HelloRestfulClient.class);

        HashMap<String, String> mockReq = new HashMap<>();
        mockReq.put("post", "hello");

        HashMap<String, Object> mockBody = new HashMap<>();
        mockBody.put("id", 1234L);
        mockBody.put("type", "post");
        String helloPostRes = helloRestfulClient.helloPost(mockReq, mockBody);
        Assert.assertEquals(helloPostRes, "{\"message\":\"Hello Post\"}");

        HelloPostBody body = new HelloPostBody();
        body.setId(1234L);
        body.setName("postSerialize");
        HelloPostRes helloPostSerializeRes = helloRestfulClient.helloPostSerialize(mockReq, body);
        Assert.assertEquals(helloPostSerializeRes.getMessage(), "Hello Post Serialize");

    }

    static class MockServer {
        void setupGetSpec() {
            port(8080);
            get("/hello/get", (req, res) -> "{\"message\":\"Hello Get\"}");
            get("/hello/get/path/1234", (req, res) -> "{\"message\":\"Hello Get Path\"}");
            get("/hello/get/query", (req, res) -> {
                String get = req.headers("get");
                if (!"hello".equals(get)) {
                    return "";
                }
                QueryParamsMap queryParamsMap = req.queryMap();
                String type = queryParamsMap.toMap().get("name")[0];
                if (!"query".equals(type)) {
                    return "";
                }
                return "{\"message\":\"Hello Get Query\"}";
            });
            try {
                // 确保8080启动
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        void setupPostSpec() {
            port(8080);
            post("/hello/post", (req, res) -> {
                String body = req.body();
                JSONObject jsonObject = FastJsonUtil.fromJson(body);
                String type = jsonObject.getString("type");
                Long id = jsonObject.getLong("id");
                if (id != 1234 || !"post".equals(type)) {
                    return "";
                }
                return "{\"message\":\"Hello Post\"}";
            });
            post("/hello/post/serialize", (req, res) -> {
                String body = req.body();
                HelloPostBody helloPostBody = FastJsonUtil.fromJson(body, HelloPostBody.class);
                Long id = helloPostBody.getId();
                String name = helloPostBody.getName();
                if (id != 1234 || !"postSerialize".equals(name)) {
                    return "";
                }
                return "{\"message\":\"Hello Post Serialize\"}";
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

    @Data
    public static class HelloPostBody {
        private Long id;
        private String name;
    }

    @Data
    public static class HelloPostRes {
        private String message;
    }


}
