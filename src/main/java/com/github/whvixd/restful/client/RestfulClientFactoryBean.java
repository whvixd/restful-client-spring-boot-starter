package com.github.whvixd.restful.client;

import org.springframework.beans.factory.FactoryBean;

/**
 * Created by wangzhx on 2018/12/1.
 */
// TODO: 2022/2/18 使用手动注册
public class RestfulClientFactoryBean<T> implements FactoryBean<T> {

    private Class<T> clientType;

    public RestfulClientFactoryBean(Class<T> clientType) {
        this.clientType = clientType;
    }

    @Override
    public T getObject() throws Exception {
        // 添加RestfulClientScan
        return null;
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
