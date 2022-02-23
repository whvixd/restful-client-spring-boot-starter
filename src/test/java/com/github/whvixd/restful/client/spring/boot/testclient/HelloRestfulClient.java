package com.github.whvixd.restful.client.spring.boot.testclient;

import com.github.whvixd.restful.client.annotation.*;
import com.github.whvixd.restful.client.spring.boot.autoconfigure.RestfulClientAutoConfigurationTest;

import java.util.Map;

/**
 * Created by whvixd on 2022/02/22.
 */
@RequestMapping(path = "127.0.0.1:8080",message = "hello")
public interface HelloRestfulClient {
    @RequestGet(path = "/hello/get")
    String helloGet(@RequestHeader Map<String, String> headers);

    @RequestGet(path = "/hello/get/{type}/{id}")
    String helloGetPath(@RequestHeader Map<String, String> headers, @RequestPathParam Map<String, String> pathParam);

    @RequestGet(path = "/hello/get/{type}?id={id}&name={name}")
    String helloGetQuery(@RequestHeader Map<String, String> headers, @RequestPathParam Map<String, String> pathParam, @RequestQueryParam Map<String, String> queryParam);

    @RequestPost(path = "/hello/post")
    String helloPost(@RequestHeader Map<String, String> headers, @RequestBody Map<String, Object> body);

    @RequestPost(path = "/hello/post/serialize")
    RestfulClientAutoConfigurationTest.HelloPostRes helloPostSerialize(@RequestHeader Map<String, String> headers, @RequestBody RestfulClientAutoConfigurationTest.HelloPostBody body);
}
