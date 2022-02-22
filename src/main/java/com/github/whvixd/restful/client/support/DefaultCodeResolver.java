package com.github.whvixd.restful.client.support;


import com.github.whvixd.restful.client.toolkit.FastJsonUtil;

import java.lang.reflect.Type;

/**
 * 编码、解码默认解析器
 * Created by whvixd on 2020/3/20.
 */
public class DefaultCodeResolver implements CodeResolver {
    @Override
    public <T> byte[] encode(T o) {
        return FastJsonUtil.toJson(o);
    }

    @Override
    public <T> T decode(byte[] bytes, Type type) {
        return FastJsonUtil.fromJson(bytes, type);
    }
}
