package com.ecoeler.controller;


import com.ecoeler.app.entity.Family;
import com.ecoeler.app.service.IFamilyService;
import com.ecoeler.model.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 家庭
 * @author tang
 * @since 2020/9/17
 */
@RequestMapping("/family")
@RestController
public class FamilyController {

    @Autowired
    private IFamilyService familyService;

    /**
     * 列出用户家庭
     * @param userId
     * @return
     */
    @PostMapping("/list/user/family")
    public Result listUserFamily(@RequestParam Long userId){
        return Result.ok(familyService.listUserFamily(userId));
    }

    @PostMapping("/add/family")
    public Result addFamily(@RequestBody Family family,@RequestParam Long userId,@RequestParam String nickname){
        return Result.ok(familyService.addFamily(family,userId,nickname));
    }



}
