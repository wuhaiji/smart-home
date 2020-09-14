package com.ecoeler.feign;

import com.ecoeler.model.request.OauthPasswordRequest;
import com.ecoeler.model.response.Result;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 账号密码模式token
 * @author tang
 * @since 2020/9/11
 */
@FeignClient(value = "app-oauth2", path = "/oauth")
public interface Oauth2ClientService {

    /**
     * 账号密码获取token
     * @param request
     * @return
     */
    @PostMapping(value = "/token")
    @Headers(value={"ContentType=application/x-www-form-urlencoded"})
    Result<OAuth2AccessToken> getTokenByPasswordModel(@RequestBody OauthPasswordRequest request);


    /**
     * 账号密码获取token
     * @param request
     * @return
     */
//    @PostMapping("/token")
//    Result<OAuth2AccessToken> getTokenByPasswordModel(
//            @RequestParam String username,
//
//            @RequestParam String password,
//
//            @RequestParam String clientId,
//
//            @RequestParam String clientSecret,
//
//            @RequestParam String grantType
//
//    );

}
