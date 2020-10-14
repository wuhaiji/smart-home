package com.ecoeler.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 *
 * @author tang
 * @since 2020/9/14
 */
@Data
public class Oauth2Token {

    @JsonProperty("access_token")
    private String access_token;

    @JsonProperty("token_type")
    private String token_type;

    @JsonProperty("refresh_token")
    private String refresh_token;

    @JsonProperty("expires_in")
    private Long expires_in;

    private String scope;


    private String username;

    private String headImage;
}
