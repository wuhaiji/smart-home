package com.ecoeler.action.alexa.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecoeler.action.alexa.AlexaOAuth2Properties;
import com.ecoeler.action.alexa.AlexaResponse;
import com.ecoeler.action.alexa.service.OauthService;
import com.ecoeler.app.entity.AlexaToken;
import com.ecoeler.app.service.IAlexaTokenService;
import com.ecoeler.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * @author whj
 * @createTime 2020-03-02 11:57
 * @description
 **/

@Service
@Slf4j
public class OauthServiceImpl implements OauthService {

    /**
     * token过期容错时间，10秒
     */
    public static final int FAULT_TOLERANCE_TIME = 10000;
    @Autowired
    IAlexaTokenService alexaTokenService;

    @Autowired
    AlexaOAuth2Properties alexaOAuth2Properties;

    @Autowired
    RestTemplate restTemplate;


    @Override
    public AlexaResponse oauthToken(String code, Long userId) {
        try {
            //发送请求;
            String responseStr = getTokenByCode(code);
            //解析
            AlexaToken alexaToken = this.parseObject(responseStr);
            //保存
            saveTokenInfo(userId, alexaToken);
            AlexaResponse alexaResponse = new AlexaResponse("Alexa.Authorization", "AcceptGrant.Response");
            alexaResponse.SetPayload(new JSONObject().toString());
            return alexaResponse;
        } catch (Exception e) {
            //出现异常，返回失败信息
            log.error("get Token failed!");
            e.printStackTrace();
            AlexaResponse alexaResponse = new AlexaResponse("Alexa.Authorization", "ErrorResponse");
            JSONObject payload = new JSONObject();
            payload.put("type", "ACCEPT_GRANT_FAILED");
            payload.put("message", "Failed to handle the AcceptGrant directive because  can not  exchange the " +
                    "authorization code used to access and refresh tokens");
            alexaResponse.SetPayload(payload.toJSONString());
            return alexaResponse;
        }

    }

    /**
     * string转换成AlexaToken 对象
     *
     * @param responseStr string
     * @return AlexaToken 对象
     */
    private AlexaToken parseObject(String responseStr) {
        JSONObject jsonObject = JSONObject.parseObject(responseStr);
        String access_token = jsonObject.getString("access_token");
        String refresh_token = jsonObject.getString("refresh_token");
        String token_type = jsonObject.getString("token_type");
        String expires_in = jsonObject.getString("expires_in");

        AlexaToken alexaToken = new AlexaToken();
        alexaToken.setRefreshToken(refresh_token);
        alexaToken.setAccessToken(access_token);
        alexaToken.setTokenType(token_type);
        alexaToken.setExpiresIn(Integer.parseInt(expires_in));
        return alexaToken;
    }

    @Override
    public String getToken(Long userId) {
        log.info("userId:" + userId);

        //先从数据库查询token信息
        AlexaToken one = alexaTokenService.getOne(new QueryWrapper<AlexaToken>().eq("user_id", userId));
        //检查是否过期
        long creatTime = one.getCreatTime().toInstant(OffsetDateTime.now().getOffset()).toEpochMilli();
        long nowTime = System.currentTimeMillis();
        Long expiresIn = Long.valueOf(one.getExpiresIn());
        //如果access_token没有超期,直接返回
        if ((creatTime + expiresIn) + FAULT_TOLERANCE_TIME > nowTime) {
            return one.getAccessToken();
        }
        //否则重新获取令牌信息
        String responseBody = getTokenByRefreshToken(one.getRefreshToken());
        AlexaToken alexaToken = this.parseObject(responseBody);
        //更新token信息
        alexaToken.setId(one.getId());
        return saveTokenInfo(userId, alexaToken);

    }

    /**
     * 通过刷新令牌获取token信息
     *
     * @param refreshToken 刷新令牌
     * @return response body
     */
    public String getTokenByRefreshToken(String refreshToken) {
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.set("grant_type", "refresh_token");
        multiValueMap.set("refresh_token", refreshToken);
        multiValueMap.set("client_id", alexaOAuth2Properties.getClientId());
        multiValueMap.set("client_secret", alexaOAuth2Properties.getClientSecret());
        String response = postForString(multiValueMap);
        log.info("response:" + response);
        return response;

    }

    /**
     * 保存令牌信息
     *
     * @param userId     用户id
     * @param alexaToken 令牌信息对象
     * @return 访问令牌
     */
    private String saveTokenInfo(Long userId, AlexaToken alexaToken) {
        alexaToken.setCreatTime(LocalDateTime.now());
        alexaToken.setUserId(userId);
        log.info("alexa token:" + alexaToken);
        log.info("alexaTokenService:" + alexaTokenService);
        alexaTokenService.saveUpdate(alexaToken);
        return alexaToken.getAccessToken();
    }


    /**
     * 通过code交换token
     *
     * @param code 授权码
     * @return responseBody
     */
    public String getTokenByCode(String code) {
        log.info("code:" + code);
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.set("grant_type", "authorization_code");
        multiValueMap.set("client_id", alexaOAuth2Properties.getClientId());
        multiValueMap.set("client_secret", alexaOAuth2Properties.getClientSecret());
        multiValueMap.set("code", code);
        String response = postForString(multiValueMap);
        log.info("response:" + response);
        return response;

    }


    /**
     * 发送Post远程请求token
     *
     * @param multiValueMap body参数
     * @return jsonString
     */
    private String postForString(MultiValueMap<String, Object> multiValueMap) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(multiValueMap, httpHeaders);
            String s = httpEntity.toString();
            log.info("请求entity:" + s);

            String responseBody = restTemplate.postForObject(alexaOAuth2Properties.getOauthUrl(), httpEntity, String.class);
            log.info(responseBody);
            return responseBody;
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new ServiceException("A network exception occurred when applying for alexa token!");
        }

    }


}
