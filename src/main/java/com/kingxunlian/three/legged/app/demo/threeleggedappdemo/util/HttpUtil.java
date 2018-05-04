package com.kingxunlian.three.legged.app.demo.threeleggedappdemo.util;

import com.alibaba.fastjson.JSONObject;
import com.kingxunlian.three.legged.app.demo.threeleggedappdemo.domain.Status;
import com.kingxunlian.three.legged.app.demo.threeleggedappdemo.domain.XLResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class HttpUtil {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    private static final String HEADER_AUTHORIZATION = "AUTHORIZATION";

//    public static <T> T postForObject(String uri, Object requestObj, Class<T> responseType) {
//        return REST_TEMPLATE.postForObject(uri, JSONObject.toJSONString(requestObj), responseType);
//    }

    public static <T> XLResponse<T> xlPostRequest(String uri, Object requestObj, Class<T> responseresponseBodyType) {
        return xlPostRequest(uri, requestObj, responseresponseBodyType, null);
    }

    public static <T> XLResponse<T> xlPostRequest(String uri, Object requestObj, Class<T> responseBodyType, String accessToken) {
        return xlRequest(uri, requestObj, responseBodyType, HttpMethod.POST, null, accessToken);
    }

    public static <T> XLResponse<T> xlGetRequest(String uri, Class<T> responseBodyType, Map<String, String> paramMap) {
        return xlGetRequest(uri, responseBodyType, paramMap, null);
    }

    public static <T> XLResponse<T> xlGetRequest(String uri, Class<T> responseBodyType, Map<String, String> paramMap, String accessToken) {
        return xlRequest(uri, null, responseBodyType, HttpMethod.GET, paramMap, accessToken);
    }


    private static <T> XLResponse<T> xlRequest(String uri, Object requestObj, Class<T> responseBodyType, HttpMethod httpMethod, Map<String, String> paramMap, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        if (accessToken != null) {
            headers.add(HEADER_AUTHORIZATION, accessToken);
        }
        HttpEntity httpEntity = new HttpEntity(JSONObject.toJSONString(requestObj), headers);
        if (paramMap == null) {
            paramMap = new HashMap<>();
        }
        ResponseEntity<String> res = REST_TEMPLATE.exchange(
                uri,
                httpMethod,
                httpEntity,
                String.class,
                paramMap
        );
        if (200 != res.getStatusCodeValue() || null == res.getBody()) {
            throw new HttpServerErrorException(res.getStatusCode());
        }
        String responseStr = res.getBody();
        return convertResponse(responseStr, responseBodyType);
//        try {
//            responseStr = new String(responseStr.getBytes("ISO-8859-1"), "UTF-8");
//            return convertResponse(responseStr, responseBodyType);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
    }


    private static <T> XLResponse<T> convertResponse(String responseJson, Class<T> responseBodyType) {
        JSONObject jsonObject = JSONObject.parseObject(responseJson);
        Boolean ok = jsonObject.getBoolean("ok");
        Status status = jsonObject.getObject("status", Status.class);
        T body = jsonObject.getObject("body", responseBodyType);

        XLResponse<T> response = new XLResponse<>();
        response.setBody(body);
        response.setOk(ok);
        response.setStatus(status);
        return response;
    }
}
