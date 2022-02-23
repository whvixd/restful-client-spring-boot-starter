package com.github.whvixd.restful.client.support;

import java.lang.reflect.Type;

/**
 * 编码、解码解析器
 * Created by whvixd on 2020/3/20.
 */
public interface CodeResolver {
    <T> byte[] encode(T o);

    <T> T decode(byte[] bytes, Type type);
}
