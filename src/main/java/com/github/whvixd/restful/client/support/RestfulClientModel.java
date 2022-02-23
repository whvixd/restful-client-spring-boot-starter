package com.github.whvixd.restful.client.support;

import com.github.whvixd.restful.client.annotation.RequestBody;
import com.github.whvixd.restful.client.annotation.RequestHeader;
import com.github.whvixd.restful.client.annotation.RequestPathParam;
import com.github.whvixd.restful.client.annotation.RequestQueryParam;
import lombok.Data;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

/**
 * RestfulClient请求的数据结构
 * Created by whvixd on 2022/02/21.
 */
@Data
public class RestfulClientModel {
    /**
     * 请求地址
     */
    private String url;

    /**
     * 请求类型
     */
    private RequestType requestType;

    /**
     * 请求头
     */
    private Map<String, String> headers;

    /**
     * 请求路径参数
     */
    private Map<String, String> pathParam;

    /**
     * query路径参数
     */
    private Map<String, String> queryParam;

    /**
     * 请求体
     */
    private Object body;

    /**
     * 返回值类型
     */
    private Type resultType;

    /**
     * 编码解析器
     */
    private CodeResolver codeResolver;

    /**
     * 1. 从method中获取params
     * 2. 从param中获取注解
     * 3. 被修饰的注解参数set到RestfulClientModel中
     */
    @SuppressWarnings("all")
    public void fillArgs(Method method, Object[] args) {
        Parameter[] parameters = method.getParameters();
        if (parameters.length == 0 || args.length == 0) {
            return;
        }
        for (int i = 0; i < parameters.length; i++) {
            RequestHeader requestHeader = parameters[i].getAnnotation(RequestHeader.class);
            RequestPathParam requestPathParam = parameters[i].getAnnotation(RequestPathParam.class);
            RequestQueryParam requestQueryParam = parameters[i].getAnnotation(RequestQueryParam.class);
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);

            if (Objects.nonNull(requestHeader) && Objects.nonNull(args[i]) && args[i] instanceof Map) {
                this.setHeaders((Map<String, String>) args[i]);
            } else if (Objects.nonNull(requestPathParam) && Objects.nonNull(args[i]) && args[i] instanceof Map) {
                this.setPathParam((Map<String, String>) args[i]);
            } else if (Objects.nonNull(requestQueryParam) && Objects.nonNull(args[i]) && args[i] instanceof Map) {
                this.setQueryParam((Map<String, String>) args[i]);
            } else if (Objects.nonNull(requestBody) && Objects.nonNull(args[i])) {
                this.setBody(args[i]);
            }
        }
    }


}
