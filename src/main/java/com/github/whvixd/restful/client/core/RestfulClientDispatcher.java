package com.github.whvixd.restful.client.core;

import com.github.whvixd.restful.client.exception.RestfulClientException;
import com.github.whvixd.restful.client.support.CodeResolver;
import com.github.whvixd.restful.client.support.RequestType;
import com.github.whvixd.restful.client.support.RestfulClientModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * RestfulClient请求的分发器
 * Created by whvixd on 2022/02/18.
 */
@Slf4j
public class RestfulClientDispatcher {
    @Autowired
    private RestfulClientActuator actuator;


    public <T> T doInvoke(RestfulClientModel requestParam) {
        CodeResolver coderHandler = requestParam.getCodeResolver();
        RequestType requestType = requestParam.getRequestType();
        String url = requestParam.getUrl();
        Map<String, String> headers = requestParam.getHeaders();
        Object body = requestParam.getBody();
        /**
         * 编码
         */
        byte[] encode = coderHandler.encode(body);

        /**
         * 请求分发
         */
        byte[] dispatchRes = dispatch(requestType, url, headers, encode);

        /**
         * 解码
         */
        T result = coderHandler.decode(dispatchRes, requestParam.getResultType());
        if (log.isDebugEnabled()) {
            log.debug("requestType:{},url:{},headers:{},body:{},result:{}", requestType, url, headers, body, result);
        }
        return result;

    }

    private byte[] dispatch(RequestType requestType, String url, Map<String, String> headers, byte[] encode) {
        switch (requestType) {
            case Get:
                return actuator.doGet(url, headers);
            case Post:
                return actuator.doPost(url, headers, encode);
            case Put:
                return actuator.doPut(url, headers, encode);
            case Delete:
                return actuator.doDelete(url, headers, encode);
            default:
                throw new RestfulClientException(String.format("%s method does not support.", requestType));
        }

    }
}
