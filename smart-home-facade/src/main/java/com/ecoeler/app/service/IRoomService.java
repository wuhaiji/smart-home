package com.ecoeler.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.app.dto.v1.RoomDto;
import com.ecoeler.app.entity.Floor;
import com.ecoeler.app.entity.Room;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
public interface IRoomService extends IService<Room> {

    Boolean removeRoom(RoomDto roomDto);
}
