package com.github.whvixd.restful.client.spring.boot.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by wangzhixiang on 2022/02/18.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = RestfulClientProperties.PREFIX)
public class RestfulClientProperties {
    public static final String PREFIX = "spring.restful.client";

    private boolean retryOnConnectionFailure;
    private int connectTimeout;
    private int readTimeout;
    private int writeTimeout;
    private int connectionPoolMaxIdleConnections;
    private long connectionPoolKeepAliveDuration;
}
