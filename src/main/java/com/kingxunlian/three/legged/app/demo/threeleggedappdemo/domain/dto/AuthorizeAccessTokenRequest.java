package com.kingxunlian.three.legged.app.demo.threeleggedappdemo.domain.dto;

public class AuthorizeAccessTokenRequest {

    /**
     * 使用的授权模式，必选项，此处的值固定为"authorization_code"
     */
    private String grant_type;

    /**
     * 授权码
     */
    private String code;

    /**
     * 对应业务的appKey
     */
    private String client_id;

    /**
     * 对应业务的app_secret
     */
    private String client_secret;


    public String getGrant_type() {
        return this.grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getClient_id() {
        return this.client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return this.client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }
}
