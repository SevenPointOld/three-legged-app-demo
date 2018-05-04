package com.kingxunlian.three.legged.app.demo.threeleggedappdemo.domain.dto;

public class AuthorizeAccessTokenResponse {

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 对应zone中three-Legged-gateway端点url，也就是资源服务地址
     */
    private String endpointUrl;


    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getEndpointUrl() {
        return this.endpointUrl;
    }

    public void setEndpointUrl(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }
}
