package com.github.whvixd.restful.client.proxy;

import com.github.whvixd.restful.client.exception.ResutfulClientException;
import com.github.whvixd.restful.client.spring.boot.autoconfigure.RestfulClientProperties;
import com.github.whvixd.restful.client.support.RestfulClientConstants;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangzhixiang on 2022/02/18.
 */
@Slf4j
public class RestfulClientActuator {

    private OkHttpClient okHttpClient;

    @Autowired
    public RestfulClientActuator(RestfulClientProperties properties) {
        if (properties == null) {
            this.okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(RestfulClientConstants.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(RestfulClientConstants.WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(RestfulClientConstants.READ_TIMEOUT, TimeUnit.SECONDS)
                    .connectionPool(new ConnectionPool())
                    .build();
            return;
        }

        this.okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(properties.getConnectTimeout(), TimeUnit.SECONDS)
                .writeTimeout(properties.getWriteTimeout(), TimeUnit.SECONDS)
                .readTimeout(properties.getReadTimeout(), TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(properties.getConnectionPoolMaxIdleConnections(),
                        properties.getConnectionPoolKeepAliveDuration(), TimeUnit.SECONDS))
                .build();
    }

    public byte[] doGet(String url, Map<String, String> headers) {
        checkNull(url);
        Call call = okHttpClient.newCall(new Request.Builder()
                .url(url)
                .headers(Headers.of(headers))
                .build());
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                return Objects.isNull(response.body()) ? null : response.body().bytes();
            } else {
                log.error("doGet调用失败:code:{},message:{},url:{},headers:{}",
                        response.code(), response.message(), url, headers.toString());
                throw new ServerException("doGet调用失败");
            }

        } catch (IOException e) {
            log.error("doGet调用失败:url:{},headers:{}", url, headers.toString());
            throw new ResutfulClientException("doGet调用失败");
        }
    }

    public byte[] doPost(String url, Map<String, String> headers, byte[] content) {
        checkNull(url, content);
        RequestBody body = RequestBody.create(MediaType.parse(RestfulClientConstants.APPLICATION_JSON), content);
        Call call = okHttpClient.newCall(new Request.Builder()
                .url(url)
                .headers(Headers.of(headers))
                .post(body)
                .build());
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                return Objects.isNull(response.body()) ? null : response.body().bytes();
            } else {
                log.error("doPost调用失败:code:{},message:{},url:{},headers:{},body:{}",
                        response.code(), response.message(), url, headers.toString(), content);
                throw new ServerException("doPost调用失败");
            }

        } catch (IOException e) {
            log.error("doPost调用失败,url:{},headers:{},body:{}", url, headers.toString(), content);
            throw new ResutfulClientException("doPost调用失败");
        }
    }

    public byte[] doPut(String url, Map<String, String> headers, byte[] content) {
        checkNull(url, content);
        RequestBody body = RequestBody.create(MediaType.parse(RestfulClientConstants.APPLICATION_JSON), content);
        Call call = okHttpClient.newCall(new Request.Builder()
                .url(url)
                .headers(Headers.of(headers))
                .put(body)
                .build());
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                return Objects.isNull(response.body()) ? null : response.body().bytes();
            } else {
                log.error("doPut调用失败:code:{},message:{},url:{},headers:{},body:{}",
                        response.code(), response.message(), url, headers.toString(), content);
                throw new ServerException("doPut调用失败");
            }
        } catch (IOException e) {
            log.error("doPut调用失败,url:{},headers:{},body:{}", url, headers.toString(), content);
            throw new ResutfulClientException("doPut调用失败");
        }
    }

    public byte[] doDelete(String url, Map<String, String> headers, byte[] content) {
        checkNull(url, content);
        RequestBody body = RequestBody.create(MediaType.parse(RestfulClientConstants.APPLICATION_JSON), content);
        Call call = okHttpClient.newCall(new Request.Builder()
                .url(url)
                .headers(Headers.of(headers))
                .delete(body)
                .build());
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                return Objects.isNull(response.body()) ? null : response.body().bytes();
            } else {
                log.error("doDelete调用失败:code:{},message:{},url:{},headers:{},body:{}",
                        response.code(), response.message(), url, headers.toString(), content);
                throw new ServerException("doDelete调用失败");
            }
        } catch (IOException e) {
            log.error("doDelete调用失败,url:{},headers:{},body:{}", url, headers.toString(), content);
            throw new ResutfulClientException("doDelete调用失败");
        }
    }

    private void checkNull(String url, byte[] content) {
        if (StringUtils.isEmpty(url) || content.length == 0) {
            log.error("请求url:{}、body:{}不能为空", url, content);
            throw new IllegalArgumentException("url||body为空");
        }
    }

    private static void checkNull(String url) {
        if (StringUtils.isEmpty(url)) {
            log.error("请求url:{},不能为空", url);
            throw new IllegalArgumentException("url|");
        }
    }
}
