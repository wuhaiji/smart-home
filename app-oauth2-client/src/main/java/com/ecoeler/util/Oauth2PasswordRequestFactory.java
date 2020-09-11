package com.ecoeler.util;

import com.ecoeler.model.request.OauthPasswordRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * 请求构造
 * @author tang
 * @since 2020/9/11
 */
@Component
public class Oauth2PasswordRequestFactory {

    @Value("${security.oauth2.client.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;

    public OauthPasswordRequest build(String username,String password){
        OauthPasswordRequest request=new OauthPasswordRequest();
        request.setClient_id(clientId);
        request.setClient_secret(clientSecret);
        request.setGrant_type("password");
        request.setPassword(password);
        request.setUsername(username);
        return request;
    }

}
