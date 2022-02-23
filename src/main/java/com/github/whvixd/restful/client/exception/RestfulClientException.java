package com.github.whvixd.restful.client.exception;

/**
 * Created by whvixd on 2022/2/22.
 */
public class RestfulClientException extends RuntimeException {

    public RestfulClientException() {

    }

    public RestfulClientException(String errorMessage) {
        super(errorMessage);
    }

    public RestfulClientException(Throwable cause) {
        super(cause);
    }


    public RestfulClientException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }


}
