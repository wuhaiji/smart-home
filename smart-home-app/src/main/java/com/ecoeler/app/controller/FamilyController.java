package com.ecoeler.app.controller;


import com.ecoeler.app.entity.Family;
import com.ecoeler.feign.FamilyService;
import com.ecoeler.model.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 家庭
 * @author tang
 * @since 2020/9/18
 */
@RestController
@RequestMapping("/family")
public class FamilyController {

    @Autowired
    private FamilyService familyService;

    @PostMapping("/list/user/family")
    public Result listUserFamily( Long userId){
        return familyService.listUserFamily(userId);
    }

    @PostMapping("/add/family")
    public Result addFamily( Family family, Long userId, String nickname){
        return familyService.addFamily(family,userId,nickname);
    }

}
