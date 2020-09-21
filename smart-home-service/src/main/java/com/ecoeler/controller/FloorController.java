package com.ecoeler.controller;


import com.ecoeler.app.entity.Floor;
import com.ecoeler.app.service.IFloorService;
import com.ecoeler.model.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/list/family/floor")
    public Result listFamilyFloor(@RequestParam Long familyId){
        return Result.ok(floorService.listFamilyFloor(familyId));
    }

    @PostMapping("/add/floor")
    public Result addFloor(@RequestBody Floor floor){
        return Result.ok(floorService.addFloor(floor));
    }

    @PostMapping("/update/floor")
    public Result updateFloor(@RequestBody Floor floor){
        floorService.updateById(floor);
        return Result.ok();
    }

}
