package com.ecoeler.voice;

import com.alibaba.fastjson.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/voice")
public class VoiceController {


    @RequestMapping(value = "/googleIot/actionDevice")
    public JSONObject actionDevice(@RequestBody JSONObject data, Authentication authentication, HttpServletRequest request){
        return null;
    }

}
