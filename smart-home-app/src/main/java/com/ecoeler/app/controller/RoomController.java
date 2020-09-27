package com.ecoeler.app.controller;

import com.ecoeler.app.dto.v1.RoomDto;
import com.ecoeler.app.entity.Floor;
import com.ecoeler.app.entity.Room;
import com.ecoeler.common.NullContentJudge;
import com.ecoeler.feign.RoomService;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.model.code.WJHCode;
import com.ecoeler.model.response.Result;
import com.ecoeler.util.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 房间
 * @author tang
 * @since 2020/9/18
 */
@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PostMapping("/list/floor/room")
    public Result listFloorRoom(Long floorId){
        ExceptionUtil.notEqual(floorId, 0L,TangCode.CODE_FLOOR_ID_NULL_ERROR);
        return roomService.listFloorRoom(floorId);
    }

    @PostMapping("/list/family/room")
    public Result listFamilyRoom(Long familyId){
        ExceptionUtil.notNull(familyId, TangCode.CODE_FAMILY_ID_NULL_ERROR);
        return roomService.listFamilyRoom(familyId);
    }

    @PostMapping("/add/room")
    public Result addRoom(Room room){
        ExceptionUtil.notBlank(room.getRoomName(),TangCode.CODE_ROOM_NAME_EMPTY_ERROR);
        ExceptionUtil.notBlank(room.getRoomType(),TangCode.CODE_ROOM_TYPE_EMPTY_ERROR);
        ExceptionUtil.notNull(room.getFamilyId(),TangCode.CODE_FAMILY_ID_NULL_ERROR);
        if(room.getFloorId()==null){
            room.setFloorId(0L);
        }
        return roomService.addRoom(room);
    }

    /**
     * 通过房间id、房间名、楼层id、家庭id去删除房间
     * @author wujihong
     * @param roomDto
     * @since 14:55 2020-09-27
     */
    @RequestMapping("/remove/room")
    public Result removeRoom(RoomDto roomDto) {
        ExceptionUtil.notNull(NullContentJudge.isNullContent(RoomDto.class, roomDto), WJHCode.PARAM_EMPTY_ERROR);
        return roomService.removeRoom(roomDto);
    }

}
