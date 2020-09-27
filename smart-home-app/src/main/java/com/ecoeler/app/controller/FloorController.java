package com.ecoeler.app.controller;

import com.ecoeler.app.dto.v1.FloorDto;
import com.ecoeler.app.dto.v1.RoomDto;
import com.ecoeler.app.entity.Floor;
import com.ecoeler.common.NullContentJudge;
import com.ecoeler.feign.FloorService;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.model.code.WJHCode;
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
        ExceptionUtil.notBlank(floor.getFloorName(),TangCode.CODE_FLOOR_NAME_EMPTY_ERROR);
        floor.setFamilyId(null);
        return floorService.updateFloor(floor);
    }

    /**
     * 通过楼层id、楼层名、家庭id去删除楼层
     * @author wujihong
     * @param floorDto
     * @since 14:56 2020-09-27
     */
    @RequestMapping("/remove/floor")
    public Result removeFloor(FloorDto floorDto) {
        ExceptionUtil.notNull(NullContentJudge.isNullContent(FloorDto.class, floorDto), WJHCode.PARAM_EMPTY_ERROR);
        return floorService.removeFloor(floorDto);
    }
}
