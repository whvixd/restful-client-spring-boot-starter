package com.github.whvixd.restful.client.proxy;

import com.github.whvixd.restful.client.exception.ResutfulClientException;
import com.github.whvixd.restful.client.support.CodeResolver;
import com.github.whvixd.restful.client.support.RequestType;
import com.github.whvixd.restful.client.support.RestfulClientModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by wangzhixiang on 2022/02/18.
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
        byte[] encode = coderHandler.encode(body);

        T result = coderHandler.decode(getResult(requestType, url, headers, encode), requestParam.getResultType());
        log.info("requestType:{},url:{},headers:{},body:{},result:{}", requestType, url, headers, body, result);
        return result;

    }

    private byte[] getResult(RequestType requestType, String url, Map<String, String> headers, byte[] encode) {
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
                throw new ResutfulClientException(String.format("暂不支持 %s 请求方式", requestType));
        }

    }
}
