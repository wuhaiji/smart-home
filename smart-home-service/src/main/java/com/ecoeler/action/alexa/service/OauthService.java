package com.ecoeler.action.alexa.service;


import com.ecoeler.action.alexa.AlexaResponse;

/**
 * @author whj
 * @createTime 2020-02-28 17:14
 * @description
 **/
public interface OauthService {

    /**
     * 通过code获取token和刷新token，过期时间并保存
     * @param code 授权码
     * @return
     */
    AlexaResponse oauthToken(String code, Long userId);




    /**
     * 获取从数据库获取token
     * @param userId 用户ID
     * @return token
     */
    String getToken(Long userId);

}
