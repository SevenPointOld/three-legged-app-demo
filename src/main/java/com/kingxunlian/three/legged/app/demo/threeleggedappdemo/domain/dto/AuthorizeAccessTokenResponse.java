package com.kingxunlian.three.legged.app.demo.threeleggedappdemo.domain.dto;

public class AuthorizeAccessTokenResponse {

    /**
     * 访问令牌
     */
    private String accessToken;


    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
