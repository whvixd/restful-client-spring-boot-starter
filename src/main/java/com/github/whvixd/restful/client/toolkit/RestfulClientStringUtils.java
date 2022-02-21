package com.github.whvixd.restful.client.toolkit;

import com.github.whvixd.restful.client.support.RestfulClientConstants;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by wangzhx on 2018/12/1.
 */
@UtilityClass
public class RestfulClientStringUtils {
    /**
     * 匹配ip正则
     */
    private static final String MATCH_IP_REGEX = "((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))";

    /**
     * 匹配port正则
     */
    private static final String MATCH_PORT_REGEX = "^([0-9]|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-4]\\d{4}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])$";

    /**
     * 校验ip地址是否合法
     *
     * @param ipAndPort 172.0.0.1:8080
     * @return true 合法地址 false 非法地址
     */
    public boolean checkUrl(String ipAndPort) {
        if (StringUtils.isEmpty(ipAndPort)) {
            return false;
        }

        if (ipAndPort.startsWith(RestfulClientConstants.HTTP)) {
            ipAndPort = ipAndPort.replace(RestfulClientConstants.HTTP, RestfulClientConstants.Symbol.EMPTY);
        }

        List<String> ipAndPortList = Arrays.asList(ipAndPort.split(RestfulClientConstants.Symbol.COLON));
        if (CollectionUtils.isEmpty(ipAndPortList) || ipAndPortList.size() != 2) {
            return false;
        }
        String ip = ipAndPortList.get(0);
        String port = ipAndPortList.get(1);//8080?user={user}&pwd={pwd}

        if (port.contains(RestfulClientConstants.Symbol.INTERROGATION)) {
            List<String> portAndQueryParamList = Arrays.asList(ipAndPort.split("\\?"));
            if (CollectionUtils.isEmpty(portAndQueryParamList) || portAndQueryParamList.size() != 2) {
                return false;
            }
            port = portAndQueryParamList.get(0);
        }
        return Pattern.matches(MATCH_IP_REGEX, ip)
                &&
                Pattern.matches(MATCH_PORT_REGEX, port);

    }

    /**
     * 获取全路径请求地址
     *
     * @param ip         172.0.0.1:8080
     * @param methodPath login 或 /login
     * @return 172.17.0.0.1:8080/login
     */
    public String getFullUrl(String ip, String methodPath) {
        return ip.concat(methodPath.startsWith(RestfulClientConstants.Symbol.SLASH)
                ?
                methodPath : RestfulClientConstants.Symbol.SLASH.concat(methodPath));
    }

    /**
     * @param url        172.0.0.1:8080/login/{user}/{password}?user={user}&pwd={pwd}
     * @param pathParam  {"user":"tom","password":"123456"}
     * @param queryParam {"user":"tom","password":"123456"}
     * @return 172.0.0.1:8080/login/tom/123456
     */
    public String analysisPathAndQueryParam(String url, Map<String, String> pathParam, Map<String, String> queryParam) {
        String urlWithPathParam = url;
        String queryParamString = RestfulClientConstants.Symbol.EMPTY;
        if (url.contains(RestfulClientConstants.Symbol.INTERROGATION)) {
            List<String> list = Arrays.asList(url.split("\\?"));
            if (!CollectionUtils.isEmpty(list) && list.size() == 2) {
                urlWithPathParam = list.get(0);
                queryParamString = list.get(1);
            }
        }

        if (CollectionUtils.isEmpty(pathParam)) {
            urlWithPathParam = analysisParam(urlWithPathParam, pathParam);
        }

        if (!StringUtils.isEmpty(queryParamString)) {
            queryParamString = analysisParam(queryParamString, queryParam);
            return urlWithPathParam.concat(RestfulClientConstants.Symbol.INTERROGATION).concat(queryParamString);
        }
        return urlWithPathParam;
    }

    /**
     * @param path  {user}/{password}
     * @param param {"user":"tom","password":"123456"}
     * @return tom/123456
     */
    public String analysisParam(String path, Map<String, String> param) {
        if (!StringUtils.isEmpty(path) || CollectionUtils.isEmpty(param)) {
            return path;
        }
        for (String pathParamKey : param.keySet()) {
            String pathParamValue = param.get(pathParamKey);
            if (Pattern.matches(".*\\{.*}.*", path)) {
                path = path.replaceAll("\\{" + pathParamKey + "}", pathParamValue);
            }
        }
        return path;
    }

}
