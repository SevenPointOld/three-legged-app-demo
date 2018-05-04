package com.kingxunlian.three.legged.app.demo.threeleggedappdemo.controller;

import com.kingxunlian.three.legged.app.demo.threeleggedappdemo.domain.Status;
import com.kingxunlian.three.legged.app.demo.threeleggedappdemo.domain.XLResponse;
import com.kingxunlian.three.legged.app.demo.threeleggedappdemo.domain.dto.AuthorizeAccessTokenRequest;
import com.kingxunlian.three.legged.app.demo.threeleggedappdemo.domain.dto.AuthorizeAccessTokenResponse;
import com.kingxunlian.three.legged.app.demo.threeleggedappdemo.domain.dto.UserInfo;
import com.kingxunlian.three.legged.app.demo.threeleggedappdemo.util.AccessTokenHolder;
import com.kingxunlian.three.legged.app.demo.threeleggedappdemo.util.HttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class DemoController {

    private static final String ACCESS_TOKEN_SESSION_ATTR = "ACCESS_TOKEN_SESSION_ATTR";

    private static final String ACCESS_TOKEN_EXPIRE_RETURN_CODE = "-21";

    @Value("${client.id}")
    private String clientId;

    @Value("${client.secret}")
    private String clientSecret;

    @Value("${redirect.uri}")
    private String redirectUri;

    @Value("${auth.code.uri}")
    private String authCodeUri;

    @Value("${auth.access.token.uri}")
    private String authAccessTokenUri;

    @Value("${test.resource.uri}")
    private String testResourceUri;


    /**
     * 应用注册到迅联开放平台的landing地址，用户打开应用会跳转到这里
     */
    @RequestMapping(path = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public String index(RedirectAttributes attributes) {
        /*
         * 理论上，每次请求到应用landing地址都需要走一次oauth2完整流程获取accessToken
         */
        //  整理自己的信息，申请授权码
        addAuthCodeAttr(attributes);
        return "redirect:" + authCodeUri;
    }

    /**
     * 申请授权码成功后，迅联开放平台会重定向到该地址
     * 回调地址是申请授权码参数中 redirect_uri 的值
     */
    @RequestMapping(path = "/authCodeCallBack", method = {RequestMethod.GET, RequestMethod.POST})
    public String authCodeCallBack(@RequestParam("code") String code, String state, Model model, RedirectAttributes attributes, HttpServletRequest request) throws Exception {
        // 根据code 去申请访问令牌(accessToken)
        AuthorizeAccessTokenRequest accessTokenRequest = new AuthorizeAccessTokenRequest();
        accessTokenRequest.setClient_id(clientId);
        accessTokenRequest.setClient_secret(clientSecret);
        accessTokenRequest.setCode(code);
        accessTokenRequest.setGrant_type("authorization_code");

        XLResponse<AuthorizeAccessTokenResponse> response = HttpUtil.xlPostRequest(authAccessTokenUri, accessTokenRequest, AuthorizeAccessTokenResponse.class);
        if (!response.isOk()) {
            // accessToken获取失败，需要重走oauth2流程
            addAuthCodeAttr(attributes);
            return "redirect:" + authCodeUri;
        }

        Object attribute = request.getSession().getAttribute(ACCESS_TOKEN_SESSION_ATTR);
        if (attribute != null) {
            // 应用端session未过期，刷新accessToken即可
            AccessTokenHolder accessTokenHolder = (AccessTokenHolder) attribute;
            accessTokenHolder.setAccessToken(response.getBody().getAccessToken());
        } else {
            // 保存accessToken，这里使用http web session保存，应用方可选择其他全局存储方案
            AccessTokenHolder accessTokenHolder = AccessTokenHolder.buildByXLResponse(response);
            request.getSession().setAttribute(ACCESS_TOKEN_SESSION_ATTR, accessTokenHolder);
        }


        model.addAttribute("response", response);
        return "index";
    }


    @ResponseBody
    @RequestMapping(path = "/testResourceRequest", method = {RequestMethod.GET, RequestMethod.POST})
    public XLResponse<UserInfo> testResourceRequest(HttpServletRequest request) {
        Object sessionAttrObj = request.getSession().getAttribute(ACCESS_TOKEN_SESSION_ATTR);
        if (sessionAttrObj == null) {
            XLResponse<UserInfo> response = new XLResponse<>();
            // 应用方accessToken存储过期，需要重走oauth2流程
            response.setOk(false);
            // 这里假设应用方后端通过响应码告知对应前端accessToken已过期，前端再次发起oauth2流程，应用方可自定方式重新获取accessToken
            Status status = new Status();
            status.setMessage("accessToken app end expire");
            status.setReturnCode(ACCESS_TOKEN_EXPIRE_RETURN_CODE);
            response.setStatus(status);
            return response;
        }
        AccessTokenHolder accessTokenHolder = (AccessTokenHolder) sessionAttrObj;
        // 资源服务网关地址 + api相对路径 = 资源服务完成地址
        String fullResourceUri = accessTokenHolder.getResourceUri().concat(testResourceUri);
        // 访问资源服务
        /*
         *   ***注意***
         *   demo应用后端未判断资源请求的响应是否成功，直接将响应信息返回给应用前端，demo前端会根据返回码做业务处理
         *   如遇到accessToken过期，则需要重走oauth2流程，具体返回码含义说明请参照 迅联开发平台公共返回码说明
         */
        return HttpUtil.xlGetRequest(fullResourceUri, UserInfo.class, null, accessTokenHolder.getAccessToken());
    }

    private void addAuthCodeAttr(RedirectAttributes attributes) {
        attributes.addAttribute("client_id", clientId);
        attributes.addAttribute("response_type", "code");
        attributes.addAttribute("redirect_uri", redirectUri);
        // 实际应用中state需要全局存储
        attributes.addAttribute("state", UUID.randomUUID().toString());
    }
}
