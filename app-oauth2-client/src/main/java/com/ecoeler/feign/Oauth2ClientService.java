package com.ecoeler.feign;

import com.ecoeler.config.Oauth2FeignExceptionConfiguration;
import com.ecoeler.model.response.Oauth2Token;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * 账号密码模式token
 * @author tang
 * @since 2020/9/11
 */
@FeignClient(value = "app-oauth2", path = "/oauth" , configuration = Oauth2FeignExceptionConfiguration.class)
public interface Oauth2ClientService {

    /**
     * 账号密码获取token
     * @param clientId
     * @param clientSecret
     * @param username
     * @param password
     * @return
     */
    @PostMapping(value = "/token?client_id={clientId}&client_secret={clientSecret}&username={username}&password={password}&grant_type=password")
    Oauth2Token getToken(
            @PathVariable("clientId") String clientId,
            @PathVariable("clientSecret") String clientSecret,
            @PathVariable("username") String username,
            @PathVariable("password") String password);


    /**
     * 刷新token
     * @param clientId
     * @param clientSecret
     * @param refreshToken
     * @return
     */
    @PostMapping(value = "/token?client_id={clientId}&client_secret={clientSecret}&grant_type=refresh_token&refresh_token={refreshToken}")
    Oauth2Token refreshToken(
            @PathVariable("clientId") String clientId,
            @PathVariable("clientSecret") String clientSecret,
            @PathVariable("refreshToken") String refreshToken);


}
