package com.ecoeler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.bean.v1.DeviceBean;
import com.ecoeler.app.dto.v1.DeviceDto;
import com.ecoeler.app.entity.*;
import com.ecoeler.app.mapper.*;
import com.ecoeler.app.msg.OrderInfo;
import com.ecoeler.app.service.IDeviceService;
import com.ecoeler.app.service.ITimerJobService;
import com.ecoeler.core.DeviceEvent;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.model.code.DeviceCode;
import com.ecoeler.utils.EptUtil;
import com.ecoeler.utils.Query;
import com.ecoeler.utils.SpringUtil;
import com.ecoeler.utils.WebStatisticsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

    @Autowired
    private DeviceTypeMapper deviceTypeMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private FloorMapper floorMapper;

    @Autowired
    private DeviceKeyMapper deviceKeyMapper;

    @Autowired
    private DeviceDataMapper deviceDataMapper;
    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private WebStatisticsUtil webStatisticsUtil;

    @Autowired
    private SceneActionMapper sceneActionMapper;

    @Autowired
    private TimerJobMapper timerJobMapper;

    @Autowired
    private ITimerJobService iTimerJobService;


    @Override
    public void control(OrderInfo orderInfo) {
        QueryWrapper<DeviceType> q = new QueryWrapper<>();
        q.eq("product_id", orderInfo.getProductId()).select("event_class");
        DeviceType deviceType = deviceTypeMapper.selectOne(q);

        DeviceEvent deviceEvent = (DeviceEvent) SpringUtil.getBean(deviceType.getEventClass());
        deviceEvent.getDeliver().deliver(orderInfo);
    }


    @Override
    public DeviceSpace getDeviceSpace(String deviceId) {

        QueryWrapper<Device> q1 = new QueryWrapper<>();
        q1.eq("device_id", deviceId);
        Device device = baseMapper.selectOne(q1);

        if (device == null) {
            return null;
        }

        DeviceSpace deviceSpace = new DeviceSpace();
        deviceSpace.setRoomId(device.getRoomId());
        deviceSpace.setFamilyId(device.getFamilyId());


        Room room = roomMapper.selectById(device.getRoomId());
        deviceSpace.setRoomName(room.getRoomName());

        deviceSpace.setFloorId(room.getFloorId());

        if (room.getFloorId() == 0) {
            deviceSpace.setRoomName("");
            return deviceSpace;
        }

        Floor floor = floorMapper.selectById(room.getFloorId());
        deviceSpace.setFloorName(floor.getFloorName());

        return deviceSpace;
    }

    /**
     * 新增设备
     *
     * @param device
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addDevice(Device device) {
        String deviceId = device.getDeviceId();
        //查询设备是否存在
        Device queryDeviceExit = baseMapper.selectOne(new LambdaQueryWrapper<Device>().eq(Device::getDeviceId, deviceId));
        if (queryDeviceExit != null) {
            //存在 修改设备
            device.setId(queryDeviceExit.getId());
            baseMapper.updateById(device);
            return device.getId();
        }
        String productId = device.getProductId();
        DeviceType deviceType = deviceTypeMapper.selectOne(
                new LambdaQueryWrapper<DeviceType>()
                        .select(DeviceType::getEventClass,
                                DeviceType::getEnTypeName,
                                DeviceType::getZhTypeName,
                                DeviceType::getDefaultIcon,
                                DeviceType::getGatewayLike
                        )
                        .eq(DeviceType::getProductId, productId)
        );
        if (deviceType != null) {
            device.setEnTypeName(deviceType.getEnTypeName());
            device.setEventClass(deviceType.getEventClass());
            device.setZhTypeName(deviceType.getZhTypeName());
            device.setGatewayLike(deviceType.getGatewayLike());
            //没有选择图标则为默认图标
            if (device.getDeviceIcon() == null || "".equals(device.getDeviceIcon().trim())) {
                device.setDeviceIcon(deviceType.getDefaultIcon());
            }
        }

        //没有选择设备状态 默认在线
        if (device.getNetState() == null) {
            device.setNetState(1);
        }
        baseMapper.insert(device);
        List<DeviceKey> deviceKeys = deviceKeyMapper.selectList(new LambdaQueryWrapper<DeviceKey>()
                .eq(DeviceKey::getProductId, productId)
                .select(DeviceKey::getDataKey)
        );
        //插入device_data 数据
        if (deviceKeys != null && deviceKeys.size() != 0) {
            List<DeviceData> deviceDataList = deviceKeys.stream().map(
                    it -> {
                        DeviceData data = new DeviceData();
                        data.setDeviceId(deviceId);
                        data.setDataKey(it.getDataKey());
                        return data;
                    }
            ).collect(Collectors.toList());
            deviceDataMapper.insertBatch(deviceDataList);
        }
        //更加统计表数据
        webStatisticsUtil.updateStatistics(Device.class);
        return device.getId();
    }


//    @Override
//    public Boolean removeDevice(List<Long> roomIdList, Boolean removeFamilyBool) {
//        Boolean result = false;
//        QueryWrapper<Room> roomQueryWrapper = new QueryWrapper<>();
//        UpdateWrapper<Device> deviceUpdateWrapper = new UpdateWrapper<>();
//        Long familyId;
//        Device device = new Device();
//        // 将房间id置为0
//        device.setRoomId(0L);
//
//        // 判断是否将家庭id置为0
//        if (removeFamilyBool) {
//            device.setFamilyId(0L);
//            roomQueryWrapper.select("family_id");
//            roomQueryWrapper.eq("id", roomIdList.get(0));
//            // 通过房间id--查询家庭id
//            familyId = roomMapper.selectOne(roomQueryWrapper).getFamilyId();
//            deviceUpdateWrapper.eq("family_id", familyId);
//            if (deviceMapper.update(device, deviceUpdateWrapper) > 0) {
//                result = true;
//            }
//        } else {
//            deviceUpdateWrapper.in("room_id", roomIdList);
//            if(deviceMapper.update(device, deviceUpdateWrapper) > 0) {
//                result = true;
//            }
//        }
//        return result;
//    }

    @Override
    public Boolean removeDevice(List<Long> roomIdList, Long familyId, Boolean removeFamilyBool) {
        Boolean result = false;
        QueryWrapper<Room> roomQueryWrapper = new QueryWrapper<>();
        UpdateWrapper<Device> deviceUpdateWrapper = new UpdateWrapper<>();
        Device device = new Device();
        // 将房间id置为0
        device.setRoomId(0L);

        // 判断是否将家庭id置为0
        if (removeFamilyBool) {
            device.setFamilyId(0L);
            deviceUpdateWrapper.eq("family_id", familyId);
            if (deviceMapper.update(device, deviceUpdateWrapper) > 0) {
                result = true;
            }
        } else {
            deviceUpdateWrapper.in("room_id", roomIdList);
            if (deviceMapper.update(device, deviceUpdateWrapper) > 0) {
                result = true;
            }
        }
        return result;
    }

    /**
     * 删除设备
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDevice(Long id) {
        //将家庭id更改为0
        Device device = new Device();
        device.setId(id);
        device.setFamilyId(0L);
        baseMapper.updateById(device);
        Device exit = baseMapper.selectById(id);
        String deviceId = exit.getDeviceId();
        //删除scene_action
        sceneActionMapper.delete(new LambdaQueryWrapper<SceneAction>().eq(SceneAction::getDeviceId, deviceId));
        //删除time_job
        TimerJob timerJob = timerJobMapper.selectOne(new LambdaQueryWrapper<TimerJob>().eq(TimerJob::getDeviceId, deviceId)
                .select(TimerJob::getId));
        iTimerJobService.deleteJob(timerJob.getId());
    }

    /**
     * 修改设备
     *
     * @param deviceDto 只修改设备的名称
     */
    @Override
    public void updateDevice(DeviceDto deviceDto) {
        Device device = new Device();
        device.setId(deviceDto.getId());
        device.setDeviceName(deviceDto.getDeviceName());
        baseMapper.updateById(device);
    }

    @Override
    public List<DeviceBean> selectListRoomDevice(Long roomId) {
        return deviceMapper.selectListRoomDevice(roomId);
    }

    @Override
    public List<DeviceBean> selectListFamilyDevice(Long familyId) {
        return deviceMapper.selectListFamilyDevice(familyId);
    }

    @Override
    public List<Device> getFloorDeviceList(Long floorId) {
        //查询楼层id是否存在
        try {
            Floor floor = floorMapper.selectById(floorId);
            if (floor == null) {
                throw new ServiceException();
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ServiceException(DeviceCode.FLOOR_NOT_EXIST);
        }

        try {

            //查询房间列表
            List<Room> rooms = roomMapper.selectList(Query.of(Room.class).eq(floorId != null, "floor_id", floorId));
            List<Long> roomIds = rooms.parallelStream().map(Room::getId).collect(Collectors.toList());
            if (EptUtil.isEmpty(roomIds)) return new ArrayList<>();
            //查询房间下的设备列表
            return deviceMapper.selectList(Query.of(Device.class).in("room_id", roomIds));
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ServiceException(DeviceCode.FLOOR_DEVICES_SELECT_ERROR);
        }


    }
}
