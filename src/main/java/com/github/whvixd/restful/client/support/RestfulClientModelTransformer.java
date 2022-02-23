package com.github.whvixd.restful.client.support;

import com.github.whvixd.restful.client.annotation.*;
import com.github.whvixd.restful.client.exception.RestfulClientException;
import com.github.whvixd.restful.client.toolkit.RestfulClientStringUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * RestfulClient模型转换器
 * Created by whvixd on 2022/02/21.
 */
@Slf4j
public class RestfulClientModelTransformer {
    /**
     * 将method中被修饰的注解塞到RestfulClientModel中
     *
     * @param method
     * @param args
     * @return
     */
    public static RestfulClientModel transfer(Method method, Object[] args) {
        Class<?> declaringClass = method.getDeclaringClass();
        RequestMapping requestMapping = declaringClass.getAnnotation(RequestMapping.class);
        if (Objects.isNull(requestMapping)) {
            log.error("{} interface does not decorate @RequestMapping",declaringClass.getName());
            throw new RestfulClientException("interface does not decorate @RequestMapping");
        }

        String ipAndPort = requestMapping.path();
        ipAndPort = ipAndPort.startsWith(RestfulClientConstants.HTTP) ? ipAndPort : RestfulClientConstants.HTTP.concat(ipAndPort);
        if (!RestfulClientStringUtils.checkUrl(ipAndPort)) {
            log.error("url format is illegal,ip:{} ", ipAndPort);
            throw new RestfulClientException("url format is illegal");
        }

        return doTransfer(method, args, ipAndPort, requestMapping.coder());
    }

    private static RestfulClientModel doTransfer(Method method, Object[] args, String ipAndPort, Class<? extends CodeResolver> coder) {
        RestfulClientModel restfulClientModel = new RestfulClientModel();

        RequestGet requestGet = method.getAnnotation(RequestGet.class);
        RequestPost requestPost = method.getAnnotation(RequestPost.class);
        RequestPut requestPut = method.getAnnotation(RequestPut.class);
        RequestDelete requestDelete = method.getAnnotation(RequestDelete.class);

        restfulClientModel.fillArgs(method, args);
        restfulClientModel.setResultType(method.getReturnType());
        String path = null;

        if (Objects.nonNull(requestGet)) {
            restfulClientModel.setRequestType(RequestType.Get);
            path = requestGet.path();

        } else if (Objects.nonNull(requestPost)) {
            restfulClientModel.setRequestType(RequestType.Post);
            path = requestPost.path();

        } else if (Objects.nonNull(requestPut)) {
            restfulClientModel.setRequestType(RequestType.Put);
            path = requestPut.path();

        } else if (Objects.nonNull(requestDelete)) {
            restfulClientModel.setRequestType(RequestType.Delete);
            path = requestDelete.path();

        }
        restfulClientModel.setUrl(RestfulClientStringUtils.analysisPathAndQueryParam(
                RestfulClientStringUtils.getFullUrl(ipAndPort, path),
                restfulClientModel.getPathParam(),
                restfulClientModel.getQueryParam()));

        restfulClientModel.setCodeResolver(instanceCoderHandler(coder));
        return restfulClientModel;
    }


    private static CodeResolver instanceCoderHandler(Class<? extends CodeResolver> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            log.error("coderHandler instance error ", e);
            throw new RestfulClientException(e);
        }
    }
}
