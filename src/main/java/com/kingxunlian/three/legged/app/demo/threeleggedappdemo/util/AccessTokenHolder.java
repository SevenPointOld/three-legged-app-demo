package com.kingxunlian.three.legged.app.demo.threeleggedappdemo.util;

import com.kingxunlian.three.legged.app.demo.threeleggedappdemo.domain.XLResponse;
import com.kingxunlian.three.legged.app.demo.threeleggedappdemo.domain.dto.AuthorizeAccessTokenResponse;
import org.springframework.util.Assert;

public class AccessTokenHolder {

    public static AccessTokenHolder buildByXLResponse(XLResponse<AuthorizeAccessTokenResponse> response) {
        AuthorizeAccessTokenResponse tokenResponse = response.getBody();
        return new AccessTokenHolder(tokenResponse.getAccessToken(), tokenResponse.getEndpointUrl());
    }

    private String accessToken;

    private String resourceUri;

    private AccessTokenHolder(String accessToken, String resourceUri) {
        Assert.notNull(accessToken, "accessToken is required; it must not be null");
        Assert.notNull(resourceUri, "resourceUri is required; it must not be null");
        this.accessToken = accessToken;
        this.resourceUri = resourceUri;
    }


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getResourceUri() {
        return resourceUri;
    }
}
