package com.ecoeler.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecoeler.app.dto.v1.FloorDto;
import com.ecoeler.app.dto.v1.RoomDto;
import com.ecoeler.app.entity.Room;
import com.ecoeler.app.service.IRoomService;
import com.ecoeler.common.NullContentJudge;
import com.ecoeler.model.code.WJHCode;
import com.ecoeler.model.response.Result;
import com.ecoeler.util.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 房间
 * @author tang
 * @since 2020/9/17
 */
@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private IRoomService roomService;

    @PostMapping("/list/floor/room")
    public Result listFloorRoom(@RequestParam Long floorId){
        QueryWrapper<Room> q=new QueryWrapper<>();
        q.eq("floor_id",floorId);
        return Result.ok(roomService.list(q));
    }

    @PostMapping("/list/family/room")
    public Result listFamilyRoom(@RequestParam Long familyId){
        QueryWrapper<Room> q=new QueryWrapper<>();
        q.eq("family_id",familyId);
        return Result.ok(roomService.list(q));
    }

    @PostMapping("/add/room")
    public Result addRoom(@RequestBody Room room){
        roomService.save(room);
        return Result.ok(room.getId());
    }

    @RequestMapping("/remove/room")
    public Result removeRoom(@RequestBody RoomDto roomDto) {
        ExceptionUtil.notNull(NullContentJudge.isNullContent(RoomDto.class, roomDto), WJHCode.PARAM_EMPTY_ERROR);
//        ExceptionUtil.notNull(room.getId(), WJHCode.ROOM_ID_EMPTY_ERROR);
//        ExceptionUtil.notNull(room.getRoomName(), WJHCode.ROOM_NAME_EMPTY_ERROR);
//        ExceptionUtil.notNull(room.getFloorId(), WJHCode.FLOOR_ID_EMPTY_ERROR);
//        ExceptionUtil.notNull(room.getFamilyId(), WJHCode.FAMILY_ID_EMPTY_ERROR);
        return Result.ok(roomService.removeRoom(roomDto));
    }

}
