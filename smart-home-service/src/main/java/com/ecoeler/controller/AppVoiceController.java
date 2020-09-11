package com.ecoeler.controller;


import com.ecoeler.model.response.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/voice")
public class AppVoiceController {

    @PostMapping("action")
    public Result action(){
        return null;
    }

}
