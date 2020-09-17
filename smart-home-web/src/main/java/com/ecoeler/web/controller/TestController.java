package com.ecoeler.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * 测试
 * @author tang
 * @since 2020/9/16
 */
@Slf4j
@RestController
public class TestController {


    @PreAuthorize("hasAuthority('user')")
    @RequestMapping("/test")
    public String test(){
        return  "hello world";
    }

    @RequestMapping("/test2")
    public String test2(Principal principal){
        return principal.getName();
    }

}
