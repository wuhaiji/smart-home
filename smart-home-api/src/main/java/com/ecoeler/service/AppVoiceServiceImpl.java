package com.ecoeler.service;

import com.alibaba.fastjson.JSONObject;
import com.ecoeler.app.service.AppVoiceService;
import com.ecoeler.exception.ExceptionCast;
import com.ecoeler.feign.AppVoiceFeignClient;
import com.ecoeler.model.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
@Slf4j
public class AppVoiceServiceImpl implements AppVoiceService {

    @Resource
    AppVoiceFeignClient appVoiceFeignClient;

    @Override
    public String action(JSONObject data, Map headerMap) {
        Result<String> result;
        try {
            result = appVoiceFeignClient.action(data, headerMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            result = null;
        }
        ExceptionCast.feignCast(result);
        return result.getData();
    }
}
