package com.ecoeler.app.controller;


import com.ecoeler.feign.AppUserService;
import com.ecoeler.model.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * 用户
 * @author tang
 * @since 2020/9/17
 */
@RequestMapping("/app-user")
@RestController
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    @RequestMapping("/init")
    public Result init(Principal principal){

        return Result.ok();
    }




}
