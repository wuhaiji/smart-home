package com.ecoeler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.dto.v1.RoomDto;
import com.ecoeler.app.entity.Floor;
import com.ecoeler.app.entity.Room;
import com.ecoeler.app.mapper.RoomMapper;
import com.ecoeler.app.service.IDeviceService;
import com.ecoeler.app.service.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements IRoomService {

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private IDeviceService deviceService;

    @Override
    public Boolean removeRoom(RoomDto roomDto) {
        boolean result = false;
        boolean removeFamilyBool = false;
        List<Long> roomIdList;

        if (roomDto.getRemoveFamilyBool() != null) {
            removeFamilyBool = roomDto.getRemoveFamilyBool();
        }

        QueryWrapper<Room> roomQueryWrapper = new QueryWrapper<>();
        if(roomDto.getId() != null) {
            roomQueryWrapper.eq("id", roomDto.getId());
        } else if(roomDto.getFloorId() != null) {
            roomQueryWrapper.eq("floor_id", roomDto.getFloorId());
        } else if(roomDto.getFamilyId() != null) {
            roomQueryWrapper.eq("family_id", roomDto.getFamilyId());
        }
        roomQueryWrapper.eq(roomDto.getRoomName() != null, "room_name", roomDto.getRoomName());

        // 1.根据roomDto，查询房间id集合
        roomIdList = roomMapper.selectList(roomQueryWrapper).stream().map(Room::getId).collect(Collectors.toList());
        if(roomIdList.size() != 0) {
            // 2.软删除房间下的设备（将设备表中的房间id重置为0）
            deviceService.removeDevice(roomIdList, roomDto.getFamilyId(), removeFamilyBool);
            // 3.删除房间
            if(roomMapper.delete(roomQueryWrapper) > 0 ) {
                result = true;
            }
        }
        return result;
    }
}
