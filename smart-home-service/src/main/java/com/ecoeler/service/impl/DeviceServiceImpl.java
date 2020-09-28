package com.ecoeler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.entity.*;
import com.ecoeler.app.mapper.*;
import com.ecoeler.app.msg.OrderInfo;
import com.ecoeler.app.service.IDeviceService;
import com.ecoeler.core.DeviceEvent;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.util.ExceptionUtil;
import com.ecoeler.utils.SpringUtil;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.CRC16;

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
    public Long addDevice(Device device) {
        ExceptionUtil.notBlank(device.getDeviceId(), TangCode.CODE_DEVICE_ID_EMPTY_ERROR);
        ExceptionUtil.notBlank(device.getProductId(), TangCode.CODE_PRODUCT_ID_EMPTY_ERROR);
        ExceptionUtil.notBlank(device.getDeviceName(), TangCode.CODE_PRODUCT_NAME_EMPTY_ERROR);
        //ExceptionUtil.notNull(device.getRoomId(), TangCode.CODE_ROOM_ID_NULL_ERROR);
        ExceptionUtil.notNull(device.getFamilyId(), TangCode.CODE_FAMILY_ID_NULL_ERROR);
        ExceptionUtil.notNull(device.getGatewayLike(), TangCode.CODE_GATEWAY_LIKE_NULL_ERROR);
        ExceptionUtil.notBlank(device.getPositionName(), TangCode.CODE_POSITION_NAME_EMPTY_ERROR);
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
                        .select(DeviceType::getEventClass, DeviceType::getEnTypeName, DeviceType::getZhTypeName, DeviceType::getDefaultIcon)
                        .eq(DeviceType::getProductId, productId)
        );
        device.setEnTypeName(deviceType.getEnTypeName());
        device.setEventClass(deviceType.getEventClass());
        device.setZhTypeName(deviceType.getZhTypeName());
        //没有选择图标则为默认图标
        if (device.getDeviceIcon() == null || "".equals(device.getDeviceIcon().trim())){
            device.setDeviceIcon(deviceType.getDefaultIcon());
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

        return device.getId();
    }
    @Override
    public Boolean removeDevice(List<Long> roomIdList) {
        Boolean result = false;
        UpdateWrapper<Device> deviceUpdateWrapper = new UpdateWrapper<>();
        Device device = new Device();

        device.setRoomId(0L);
        deviceUpdateWrapper.in("room_id", roomIdList);
        if(deviceMapper.update(device, deviceUpdateWrapper) > 0) {
            result = true;
        }
        return result;
    }
}
