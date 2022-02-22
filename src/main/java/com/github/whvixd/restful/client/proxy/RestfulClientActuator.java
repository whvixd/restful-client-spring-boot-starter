package com.github.whvixd.restful.client.proxy;

import com.github.whvixd.restful.client.exception.RestfulClientException;
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
 * RestfulClient的执行器，提供最基础的请求方式
 * Created by whvixd on 2022/02/18.
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
                ResponseBody body = response.body();
                return Objects.isNull(body) ? null : body.bytes();
            } else {
                log.error("doGet fails:code:{},message:{},url:{},headers:{}",
                        response.code(), response.message(), url, headers.toString());
                throw new ServerException(String.format("doGet fails,errorCode:%s,errorMessage:%s",
                        response.code(), response.message()));
            }

        } catch (IOException e) {
            log.error("doGet fails,url:{},headers:{}", url, headers.toString());
            throw new RestfulClientException(e);
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
                ResponseBody resBody = response.body();
                return Objects.isNull(resBody) ? null : resBody.bytes();
            } else {
                log.error("doPost fails:code:{},message:{},url:{},headers:{},body:{}",
                        response.code(), response.message(), url, headers.toString(), content);
                throw new ServerException(String.format("doPost fails,errorCode:%s,errorMessage:%s",
                        response.code(), response.message()));
            }

        } catch (IOException e) {
            log.error("doPost fails,url:{},headers:{}", url, headers.toString());
            throw new RestfulClientException(e);
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
                log.error("doPut fails:code:{},message:{},url:{},headers:{},body:{}",
                        response.code(), response.message(), url, headers.toString(), content);
                throw new ServerException(String.format("doPut fails,errorCode:%s,errorMessage:%s",
                        response.code(), response.message()));
            }
        } catch (IOException e) {
            log.error("doPut fails,url:{},headers:{}", url, headers.toString());
            throw new RestfulClientException(e);
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
                ResponseBody resBody = response.body();
                return Objects.isNull(resBody) ? null : resBody.bytes();
            } else {
                log.error("doDelete fails:code:{},message:{},url:{},headers:{},body:{}",
                        response.code(), response.message(), url, headers.toString(), content);
                throw new ServerException(String.format("doDelete fails,errorCode:%s,errorMessage:%s",
                        response.code(), response.message()));
            }
        } catch (IOException e) {
            log.error("doDelete fails,url:{},headers:{}", url, headers.toString());
            throw new RestfulClientException(e);
        }
    }

    private void checkNull(String url, byte[] content) {
        if (StringUtils.isEmpty(url) || content.length == 0) {
            log.error("request url or content is blank.");
            throw new IllegalArgumentException("request url or content is blank.");
        }
    }

    private static void checkNull(String url) {
        if (StringUtils.isEmpty(url)) {
            log.error("request url is blank.");
            throw new IllegalArgumentException("request url is blank.");
        }
    }
}
