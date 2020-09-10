package com.ecoeler.app.controller;

import com.ecoeler.model.response.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * 用户登录
 * @author tang
 * @since 2020/9/10
 */
@RequestMapping("/app_user")
@RestController
public class AppUserController {

    @PostMapping("/login")
    public Result login(Principal principal){
        return Result.ok(principal);
    }

}
