package com.ecoeler.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * oauth 请求dto
 * @author tang
 * @since 2020/9/11
 */
@Data
public class OauthPasswordRequest {

    private String username;

    private String password;

    private String client_id;

    private String client_secret;

    private String grant_type;


}
