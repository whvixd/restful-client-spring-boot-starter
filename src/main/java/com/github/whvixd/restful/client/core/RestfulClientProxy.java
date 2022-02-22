package com.github.whvixd.restful.client.core;

import com.github.whvixd.restful.client.support.RestfulClientModelTransformer;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Proxy;

/**
 * RestfulClient代理器
 * Created by whvixd on 2022/02/18.
 */
public class RestfulClientProxy {
    @Autowired
    private RestfulClientDispatcher dispatcher;

    public <T> T wrapDynamicProxy(Class<T> clientType) {
        return doWrapDynamicProxy(clientType);
    }

    @SuppressWarnings("all")
    private <T> T doWrapDynamicProxy(Class<T> clientType) {
        return (T) Proxy.newProxyInstance(clientType.getClassLoader(), new Class[]{clientType},
                (proxy, method, args) -> dispatcher.doInvoke(RestfulClientModelTransformer.transfer(method, args)));
    }
}
