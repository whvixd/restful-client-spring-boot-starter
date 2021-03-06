package com.github.whvixd.restful.client;

import com.github.whvixd.restful.client.core.RestfulClientProxy;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by whvixd on 2022/2/22.
 */
public class RestfulClientFactoryBean<T> implements FactoryBean<T> {

    private final Class<T> clientType;

    @Autowired
    private RestfulClientProxy proxy;

    public RestfulClientFactoryBean(Class<T> clientType) {
        this.clientType = clientType;
    }

    @Override
    public T getObject() throws Exception {
        return proxy.wrapDynamicProxy(clientType);
    }

    @Override
    public Class<T> getObjectType() {
        return clientType;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
