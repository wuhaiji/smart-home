package com.ecoeler.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.app.bean.v1.DeviceBean;
import com.ecoeler.app.dto.v1.DeviceDto;
import com.ecoeler.app.entity.Device;
import com.ecoeler.app.entity.DeviceSpace;
import com.ecoeler.app.msg.OrderInfo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
public interface IDeviceService extends IService<Device> {

    /**
     * 控制设备
     * @param orderInfo
     */
    void control(OrderInfo orderInfo);

    /**
     * 获得设备空间
     * @param deviceId
     * @return
     */
    DeviceSpace getDeviceSpace(String deviceId);

    /**
     * 新增设备
     * @param device
     * @return
     */
    Long addDevice(Device device);

    Boolean removeDevice(List<Long> roomIdList, Long familyId, Boolean removeFamilyBool);

    /**
     * 删除设备
     * @param id
     */
    void deleteDevice(Long id);

    void updateDevice(DeviceDto deviceDto);

    /***
     * 查询房间下的设备
     * @param roomId
     * @return
     */
    List<DeviceBean> selectListRoomDevice(Long roomId);
    /***
     * 查询家庭的设备
     * @param familyId
     * @return
     */
    List<DeviceBean> selectListFamilyDevice(Long familyId);
}
