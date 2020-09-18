package com.ecoeler.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecoeler.app.entity.Room;
import com.ecoeler.app.service.IRoomService;
import com.ecoeler.model.response.Result;
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

}
