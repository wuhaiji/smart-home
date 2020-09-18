package com.ecoeler.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecoeler.app.bean.v1.DeviceInfo;
import com.ecoeler.app.bean.v1.DeviceStateBean;
import com.ecoeler.app.bean.v1.DeviceVoiceBean;
import com.ecoeler.app.dto.v1.voice.DeviceVoiceDto;
import com.ecoeler.app.dto.v1.voice.UserVoiceDto;
import com.ecoeler.app.entity.*;
import com.ecoeler.app.service.*;
import com.ecoeler.constant.DeviceStatusConst;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.model.code.AppVoiceCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@Slf4j
public class AppVoiceActionServiceImpl implements AppVoiceActionService {


    public static final int action_type_1 = 1;
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
            userFamilyQuery.eq("app_user_id", userVoiceDto.getUserId());
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
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            log.info("返回的设备集合：{}", JSON.toJSONString(deviceVoiceBeans));
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ServiceException(AppVoiceCode.ACTION_SELECT_DEVICE_LIST_ERROR);
        }
        return deviceVoiceBeans;
    }

    @Override
    public DeviceInfo getDeviceStates(DeviceVoiceDto dto) {

        DeviceInfo deviceInfo;
        if (dto == null || dto.getDeviceId() == null)
            throw new ServiceException(AppVoiceCode.ACTION_PARAMS_ERROR);

        //查询设备在线离线
        Device device;
        try {
            device = iDeviceService.getById(dto.getDeviceId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(AppVoiceCode.ACTION_SELECT_DEVICE_STATES_ERROR);
        }
        if (device == null)
            throw new ServiceException(AppVoiceCode.ACTION_DEVICE_NOT_EXIST);

        //生成返回对象，并设置网络状态
        deviceInfo = DeviceInfo.of().setOnline(device.getNetState() == DeviceStatusConst.ONLINE);

        try {
            //查询设备data
            QueryWrapper<DeviceData> deviceDataQuery = new QueryWrapper<>();
            deviceDataQuery.eq("device_id", dto.getDeviceId());
            List<DeviceData> deviceDatas = iDeviceDataService.list(deviceDataQuery);
            if (CollUtil.isEmpty(deviceDatas)) return DeviceInfo.of().setDeviceStateBeans(CollUtil.list(false));
            List<String> deviceKeyList = deviceDatas.parallelStream().map(DeviceData::getDataKey).collect(Collectors.toList());

            //查询设备可控可上传key
            QueryWrapper<DeviceKey> deviceKeyQuery = new QueryWrapper<>();
            deviceKeyQuery.in(deviceKeyList.size() > 0, "device_key", deviceKeyList);
            deviceKeyQuery.eq("action_type", action_type_1);
            List<DeviceKey> deviceKeys = iDeviceKeyService.list(deviceKeyQuery);
            Map<String, DeviceKey> deviceKeyMap = deviceKeys.parallelStream().collect(Collectors.toMap(DeviceKey::getDataKey, i -> i));

            List<DeviceStateBean> deviceStateBeans = deviceDatas.parallelStream()
                    .map(it -> {
                        //获取data对应的key
                        DeviceKey deviceKey = deviceKeyMap.get(it.getDataKey());
                        if (deviceKey != null) {
                            DeviceStateBean stateBean = DeviceStateBean.of().setValue(it.getDataValue());
                            stateBean.setDeviceId(device.getId())
                                    .setAlexaStateName(deviceKey.getAlexaStateName())
                                    .setDataKey(deviceKey.getDataKey())
                                    .setGoogleStateName(deviceKey.getGoogleStateName())
                                    .setAlexaNamespace(deviceKey.getAlexaNamespace());
                            return stateBean;
                        }
                        //没有找到说明该data不是可控可上传的data
                        return null;
                    })
                    //过滤null值
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());


            deviceInfo.setDeviceStateBeans(deviceStateBeans);

        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ServiceException(AppVoiceCode.ACTION_SELECT_DEVICE_STATES_ERROR);
        }

        return deviceInfo;
    }

    @Override
    public DeviceType getDeviceType(DeviceVoiceDto dto) {

        if (dto == null || dto.getDeviceId() == null)
            throw new ServiceException(AppVoiceCode.ACTION_PARAMS_ERROR);

        DeviceType deviceType;
        try {
            Device device = iDeviceService.getById(dto.getDeviceId());
            if (device == null) return null;

            QueryWrapper<DeviceType> deviceTypeQuery = new QueryWrapper<>();
            deviceType = iDeviceTypeService.getOne(deviceTypeQuery);

            return deviceType;
        } catch (Exception e) {

            e.printStackTrace();
            throw new ServiceException(AppVoiceCode.ACTION_SELECT_DEVICE_TYPE_ERROR);
        }
    }


}
