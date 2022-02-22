package com.github.whvixd.restful.client.spring.boot.testclient;

import com.github.whvixd.restful.client.annotation.*;

import java.util.Map;

/**
 * Created by wangzhixiang on 2022/02/22.
 */
@RequestMapping
public interface HelloResutfulClient {
    @RequestGet(path = "/hello/get")
    String helloGet(@RequestHeader Map<String, String> headers);

    @RequestPost(path = "/hello/post")
    String helloPost(@RequestHeader Map<String, String> headers, @RequestBody Map<String, Object> body);
}
