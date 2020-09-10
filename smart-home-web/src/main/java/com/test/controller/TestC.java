package com.test.controller;

import com.ecoeler.test.TestTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestC {

    @Autowired
    private TestTest testTest;

    @GetMapping("/test")
    public void logasdasd(){
        testTest.test();
    }

}
