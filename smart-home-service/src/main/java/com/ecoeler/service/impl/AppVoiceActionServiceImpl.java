package com.ecoeler.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecoeler.app.bean.v1.DeviceInfo;
import com.ecoeler.app.bean.v1.DeviceStateBean;
import com.ecoeler.app.bean.v1.DeviceVoiceBean;
import com.ecoeler.app.dto.v1.voice.DeviceVoiceDto;
import com.ecoeler.app.dto.v1.voice.UserVoiceDto;
import com.ecoeler.app.entity.*;
import com.ecoeler.app.service.*;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.model.code.AppVoiceCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
public class AppVoiceActionServiceImpl implements AppVoiceActionService {


    @Autowired
    IUserFamilyService iUserFamilyService;

    @Autowired
    IRoomService iRoomService;

    @Autowired
    IDeviceService iDeviceService;

    @Autowired
    IDeviceTypeService iDeviceTypeService;

    @Autowired
    IDeviceKeyService iDeviceKeyService;

    @Autowired
    IDeviceDataService iDeviceDataService;

    /**
     * 根据用户ID查询用户的设备列表
     *
     * @param userVoiceDto
     * @return
     */
    @Override
    public List<DeviceVoiceBean> getDeviceVoiceBeans(UserVoiceDto userVoiceDto) {

        List<DeviceVoiceBean> deviceVoiceBeans;
        try {
            if (userVoiceDto == null || userVoiceDto.getUserId() == null)
                throw new ServiceException(AppVoiceCode.ACTION_PARAMS_ERROR);

            //查询出用户拥有的家庭ids
            QueryWrapper<UserFamily> userFamilyQuery = new QueryWrapper<>();
            userFamilyQuery.eq("user_id", userVoiceDto.getUserId());
            List<UserFamily> families = iUserFamilyService.list(userFamilyQuery);
            if (CollUtil.isEmpty(families)) return new ArrayList<>();
            List<Long> familyIds = families.parallelStream().map(UserFamily::getId).collect(Collectors.toList());


            //查询家庭下面设备集合
            QueryWrapper<Device> deviceQuery = new QueryWrapper<>();
            deviceQuery.in("family_id", familyIds);
            List<Device> devices = iDeviceService.list(deviceQuery);
            if (CollUtil.isEmpty(devices)) return new ArrayList<>();
            List<String> productIds = devices.parallelStream().map(Device::getProductId).collect(Collectors.toList());
            List<Long> roomIds = devices.parallelStream().map(Device::getRoomId).collect(Collectors.toList());
            log.info("查詢到的设备Ids：{}", JSON.toJSONString(roomIds));

            //查询设备对应房间信息集合
            QueryWrapper<Room> roomQuery = new QueryWrapper<>();
            roomQuery.in("id", roomIds);
            List<Room> rooms = iRoomService.list(roomQuery);
            Map<Long, Room> roomMap = rooms.parallelStream().collect(Collectors.toMap(Room::getId, i -> i));

            //查询设备对应的设备类型信息
            QueryWrapper<DeviceType> deviceTypeQuery = new QueryWrapper<>();
            deviceTypeQuery.in("product_id", productIds);
            List<DeviceType> deviceTypes = iDeviceTypeService.list(deviceTypeQuery);
            Map<String, DeviceType> deviceTypeMap = deviceTypes.parallelStream().collect(Collectors.toMap(DeviceType::getProductId, i -> i));

            // //查询设备对应的设备key信息
            // QueryWrapper<DeviceKey> deviceKeyQuery = new QueryWrapper<>();
            // userFamilyQuery.in("product_id", productIds);
            // List<DeviceKey> deviceKeys = iDeviceKeyService.list(deviceKeyQuery);
            // Map<String, DeviceKey> deviceKeyMap = deviceKeys.parallelStream().collect(Collectors.toMap(DeviceKey::getProductId, i -> i));


            deviceVoiceBeans = devices.parallelStream()
                    .map(it -> {

                        //设备基本信息
                        DeviceVoiceBean deviceVoiceBean = DeviceVoiceBean.of()
                                .setId(it.getId())
                                .setDeviceName(it.getDeviceName())
                                .setDefaultNames(it.getDeviceName())
                                .setNicknames(it.getDeviceName());

                        //设备类型信息
                        DeviceType deviceType = deviceTypeMap.get(it.getProductId());
                        if (deviceType != null) {
                            deviceVoiceBean.setAlexaDisplayName(deviceType.getAlexaDisplayName())
                                    .setGoogleTraitNames(deviceType.getGoogleTraitNames())
                                    .setGoogleTypeName(deviceType.getGoogleTypeName());
                        }

                        //房间信息
                        Room room = roomMap.get(it.getRoomId());
                        if (room != null) {
                            deviceVoiceBean.setRoomName(room.getRoomName());
                        }

                        return deviceVoiceBean;
                    })
                    .collect(Collectors.toList());

            log.info("返回的设备集合：{}", JSON.toJSONString(deviceVoiceBeans));
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ServiceException(AppVoiceCode.ACTION_SELECT_DEVICE_LIST_ERROR);
        }
        return deviceVoiceBeans;
    }

    @Override
    public DeviceInfo getUserDeviceStates(DeviceVoiceDto userVoiceDto) {

        if (userVoiceDto == null || userVoiceDto.getDeviceId() == null)
            throw new ServiceException(AppVoiceCode.ACTION_PARAMS_ERROR);
        //查询设备在线离线
        Device device = iDeviceService.getById(userVoiceDto.getDeviceId());

        //查询设备data
        QueryWrapper<DeviceData> deviceDataQuery = new QueryWrapper<>();
        deviceDataQuery.eq("device_id", device.getProductId());
        List<DeviceData> deviceDatas = iDeviceDataService.list(deviceDataQuery);
        List<String> deviceKeyList = deviceDatas.parallelStream().map(deviceData -> deviceData.getDataKey()).collect(Collectors.toList());
        Map<String, DeviceData> deviceDataMap = deviceDatas.parallelStream().collect(Collectors.toMap(DeviceData::getDataKey, i -> i));


        //查询设备可控可上传key
        QueryWrapper<DeviceKey> deviceKeyQuery = new QueryWrapper<>();
        deviceKeyQuery.in("device_key", deviceKeyList);
        deviceKeyQuery.eq("status_type", 1);
        List<DeviceKey> deviceKeys = iDeviceKeyService.list(deviceKeyQuery);


        DeviceInfo deviceInfo = DeviceInfo.of().setOnline(device.getNetState() == 1);

        List<DeviceStateBean> deviceStateBeans = deviceKeys.parallelStream().map(it -> {

            DeviceStateBean deviceStateBean = DeviceStateBean.of()
                    .setDeviceId(device.getId())
                    .setAlexaStateName(it.getAlexaStateName())
                    .setGoogleStateName(it.getGoogleStateName())
                    .setAlexaNamespace(it.getAlexaNamespace());

            DeviceData deviceData = deviceDataMap.get(it.getDataKey());
            if (deviceData != null) {
                deviceStateBean.setValue(deviceData.getDataValue());
            }

            return deviceStateBean;
        }).collect(Collectors.toList());

        deviceInfo.setDeviceStateBeans(deviceStateBeans);

        return deviceInfo;
    }


    @Override
    public Device getDeviceNetState(DeviceVoiceDto deviceVoiceDto) {
        if (deviceVoiceDto == null || deviceVoiceDto.getDeviceId() == null)
            throw new ServiceException(AppVoiceCode.ACTION_PARAMS_ERROR);
        //查询设备在线离线
        return iDeviceService.getById(deviceVoiceDto.getDeviceId());
    }

}