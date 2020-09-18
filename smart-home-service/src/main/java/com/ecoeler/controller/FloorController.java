package com.ecoeler.controller;


import com.ecoeler.app.service.IFloorService;
import com.ecoeler.model.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 楼层
 * @author tang
 * @since 2020/9/17
 */
@RestController
@RequestMapping("/floor")
public class FloorController {

    @Autowired
    private IFloorService floorService;

    @RequestMapping("/list/family/floor")
    public Result listFamilyFloor(Long familyId){
        return Result.ok(floorService.listFamilyFloor(familyId));
    }


}
