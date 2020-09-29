package com.ecoeler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.dto.v1.FloorDto;
import com.ecoeler.app.entity.Family;
import com.ecoeler.app.entity.Floor;
import com.ecoeler.app.entity.Room;
import com.ecoeler.app.mapper.FamilyMapper;
import com.ecoeler.app.mapper.FloorMapper;
import com.ecoeler.app.mapper.RoomMapper;
import com.ecoeler.app.service.IDeviceService;
import com.ecoeler.app.service.IFloorService;
import com.ecoeler.app.service.IRoomService;
import com.ecoeler.constant.FamilyTypeConst;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.model.code.TangCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class FloorServiceImpl extends ServiceImpl<FloorMapper, Floor> implements IFloorService {

    @Autowired
    private FloorMapper floorMapper;

    @Autowired
    private FamilyMapper familyMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private IDeviceService deviceService;

    private void familyCheck(Long familyId){
        Family family = familyMapper.selectById(familyId);
        if(family==null || FamilyTypeConst.VILLA !=family.getFamilyType()){
            throw new ServiceException(TangCode.CODE_FAMILY_NOT_VILLA);
        }
    }

    @Override
    public List<Floor> listFamilyFloor(Long familyId) {
        familyCheck(familyId);
        QueryWrapper<Floor> q=new QueryWrapper<>();
        q.eq("family_id",familyId);
        return baseMapper.selectList(q);

    }

    @Override
    public Long addFloor(Floor floor) {
        familyCheck(floor.getFamilyId());
        baseMapper.insert(floor);
        return floor.getId();
    }

    @Override
    public Boolean removeFloor(FloorDto floorDto) {
        boolean result = false;
        boolean removeFamilyBool = false;
        List<Long> floorIdList;
        List<Long> roomIdList;
        QueryWrapper<Floor> floorQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Room> roomQueryWrapper = new QueryWrapper<>();

        if (floorDto.getRemoveFamilyBool() != null) {
            removeFamilyBool = floorDto.getRemoveFamilyBool();
        }

        if(floorDto.getId() != null) {
            floorQueryWrapper.eq("id", floorDto.getId());
        } else if(floorDto.getFamilyId() != null) {
            floorQueryWrapper.eq("family_id", floorDto.getFamilyId());
        }
        floorQueryWrapper.eq(floorDto.getFloorName() != null,"floor_name", floorDto.getFloorName());

        // 根据floorDto，查询楼层id(去重)
        floorIdList = floorMapper.selectList(floorQueryWrapper).stream().map(Floor::getId).distinct().collect(Collectors.toList());

        // 判断查询的楼层集合是否为空
        if (floorIdList.size() == 0) {
            return false;
        }
        // 根据floorId，查询房间id
        roomQueryWrapper.in("floor_id", floorIdList);
        roomIdList = roomMapper.selectList(roomQueryWrapper).stream().map(Room::getId).collect(Collectors.toList());

        if (roomIdList.size() != 0) {
            // 1.软删除房间下的设备（将roomId重置为0，以及是否将家庭id置为0）
            deviceService.removeDevice(roomIdList, floorDto.getFamilyId(), removeFamilyBool);

            // 2.删除房间（根据楼层id集合，去删除房间）
            roomMapper.delete(roomQueryWrapper);
        }
        // 3.最后删除楼层
        if(floorMapper.delete(floorQueryWrapper) > 0 ) {
            result = true;
        }
        return result;
    }

}
