package com.ecoeler.service;

import com.alibaba.fastjson.JSONObject;
import com.ecoeler.app.service.AppVoiceService;
import com.ecoeler.exception.ExceptionCast;
import com.ecoeler.feign.AppVoiceFeignClient;
import com.ecoeler.model.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AppVoiceServiceImpl implements AppVoiceService {

    @Autowired
    AppVoiceFeignClient appVoiceFeignClient;

    @Override
    public String action(JSONObject data) {
        Result<String> result;
        try {
            result = appVoiceFeignClient.action(data);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            result = null;
        }
        ExceptionCast.feignCast(result);
        return result.getData();
    }
}
