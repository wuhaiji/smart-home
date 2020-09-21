package com.ecoeler.app.controller;

import com.ecoeler.app.entity.Floor;
import com.ecoeler.feign.FloorService;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.model.response.Result;
import com.ecoeler.util.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 楼层
 * @author tang
 * @since 2020/9/21
 */
@RestController
@RequestMapping("/floor")
public class FloorController {

    @Autowired
    private FloorService floorService;

    @RequestMapping("/add/floor")
    public Result addFloor(Floor floor){
        ExceptionUtil.notNull(floor.getFamilyId(), TangCode.CODE_FAMILY_ID_NULL_ERROR);
        ExceptionUtil.notBlank(floor.getFloorName(),TangCode.CODE_FLOOR_NAME_EMPTY_ERROR);
        return floorService.addFloor(floor);
    }

    @RequestMapping("/list/family/floor")
    public Result listFamilyFloor(Long familyId){
        ExceptionUtil.notNull(familyId, TangCode.CODE_FAMILY_ID_NULL_ERROR);
        return floorService.listFamilyFloor(familyId);
    }

    @RequestMapping("/update/floor")
    public Result updateFloor(Floor floor){
        ExceptionUtil.notNull(floor.getId(),TangCode.CODE_FLOOR_ID_NULL_ERROR);
        ExceptionUtil.notBlank(floor.getFloorName(),TangCode.CODE_FLOOR_NAME_EMPTY_ERROR);
        floor.setFamilyId(null);
        return floorService.updateFloor(floor);
    }
}
