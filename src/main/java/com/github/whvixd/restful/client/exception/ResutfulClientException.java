package com.github.whvixd.restful.client.exception;

/**
 * Created by whvixd on 2018/8/12.
 */
public class ResutfulClientException extends RuntimeException {

    public ResutfulClientException() {

    }

    public ResutfulClientException(String errorMessage) {
        super(errorMessage);
    }

    public ResutfulClientException(Throwable cause) {
        super(cause);
    }


    public ResutfulClientException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }


}
