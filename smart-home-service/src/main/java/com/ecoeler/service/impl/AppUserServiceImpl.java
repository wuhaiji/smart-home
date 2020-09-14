package com.ecoeler.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.bean.v1.DeviceVoiceBean;
import com.ecoeler.app.dto.v1.UserDto;
import com.ecoeler.app.entity.*;
import com.ecoeler.app.mapper.AppUserMapper;
import com.ecoeler.app.service.*;
import com.ecoeler.util.ErrorUtils;
import com.ecoeler.util.ListUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
@Slf4j
public class AppUserServiceImpl extends ServiceImpl<AppUserMapper, AppUser> implements IAppUserService {

    public static final int deviceRoomIdDefaultValue = 0;
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

    /**
     * 根据用户ID查询用户的设备列表
     *
     * @param userDto
     * @return
     */
    @Override
    public List<DeviceVoiceBean> getDeviceVoiceBeans(UserDto userDto) {

        ErrorUtils.isNullable(userDto, "userDto");
        ErrorUtils.isNullable(userDto.getUserId(), "userId");

        //查询出用户拥有的家庭ids
        QueryWrapper<UserFamily> userFamilyQuery = new QueryWrapper<>();
        userFamilyQuery.eq("user_id", userDto.getUserId());
        List<UserFamily> families = iUserFamilyService.list(userFamilyQuery);
        if (ListUtils.isEmpty(families)) return new ArrayList<>();
        List<Long> familyIds = families.parallelStream().map(UserFamily::getId).collect(Collectors.toList());


        //查询家庭下面设备集合
        QueryWrapper<Device> deviceQuery = new QueryWrapper<>();
        deviceQuery.in("family_id", familyIds);
        List<Device> devices = iDeviceService.list(deviceQuery);
        if (ListUtils.isEmpty(devices)) return new ArrayList<>();
        List<String> productIds = devices.parallelStream().map(Device::getProductId).collect(Collectors.toList());
        List<Long> roomIds = devices.parallelStream().map(Device::getRoomId).collect(Collectors.toList());
        log.info("查詢到的设备Ids：{}", JSON.toJSONString(roomIds));

        //查询设备对应房间信息集合
        QueryWrapper<Room> roomQuery = new QueryWrapper<>();
        deviceQuery.in("id", roomIds);
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


        List<DeviceVoiceBean> deviceVoiceBeans = devices.parallelStream()
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

        return deviceVoiceBeans;
    }
}
