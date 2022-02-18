package com.github.whvixd.restful.client.support;

/**
 * Created by wangzhixiang on 2022/02/18.
 */
public interface RestfulClientConstants {
    String HTTP = "http://";
    String APPLICATION_JSON = "application/json;charset=utf-8";

    Long CONNECT_TIMEOUT = 10L;
    Long WRITE_TIMEOUT = 10L;
    Long READ_TIMEOUT = 10L;

    /**
     * 符号
     */
    interface Symbol {
        String SLASH = "/";
        String COLON = ":";
        String EMPTY = "";
        String INTERROGATION = "?";
    }
}
