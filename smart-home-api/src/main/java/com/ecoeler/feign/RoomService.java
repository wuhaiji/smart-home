package com.ecoeler.feign;

import com.ecoeler.app.entity.Room;
import com.ecoeler.model.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * room服务
 * @author tang
 * @since 2020/9/18
 */
@FeignClient(value = "smart-home-service", path = "/room",contextId = "room")
public interface RoomService {

    @PostMapping("/list/floor/room")
    Result<List<Room>> listFloorRoom(@RequestParam Long floorId);

    @PostMapping("/list/family/room")
    Result<List<Room>> listFamilyRoom(@RequestParam Long familyId);

    @PostMapping("/add/room")
    Result<Long> addRoom(@RequestBody Room room);


}