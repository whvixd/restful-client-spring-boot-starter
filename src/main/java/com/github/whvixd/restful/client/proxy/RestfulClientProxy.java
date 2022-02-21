package com.github.whvixd.restful.client.proxy;

import com.github.whvixd.restful.client.support.RestfulClientModelTransformer;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Proxy;

/**
 * Created by wangzhixiang on 2022/02/18.
 */
public class RestfulClientProxy {
    @Autowired
    private RestfulClientDispatcher dispatcher;

    public <T> T invoke(Class<T> clientType) {
        return getJdkClientProxy(clientType);
    }


    @SuppressWarnings("all")
    private <T> T getJdkClientProxy(Class<T> clientType) {
        return (T) Proxy.newProxyInstance(clientType.getClassLoader(), new Class[]{clientType},
                (proxy, method, args) -> dispatcher.doInvoke(RestfulClientModelTransformer.transfer(method, args)));
    }
}
