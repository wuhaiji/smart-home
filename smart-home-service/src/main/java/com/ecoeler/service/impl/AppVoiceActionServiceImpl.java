package com.ecoeler.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecoeler.app.bean.v1.DeviceInfo;
import com.ecoeler.app.bean.v1.DeviceStateBean;
import com.ecoeler.app.bean.v1.DeviceVoiceBean;
import com.ecoeler.app.dto.v1.voice.*;
import com.ecoeler.app.entity.*;
import com.ecoeler.app.mapper.*;
import com.ecoeler.app.service.AppVoiceActionService;
import com.ecoeler.constant.DeviceStatusConst;
import com.ecoeler.exception.ExceptionCast;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.utils.EptUtil;
import com.ecoeler.utils.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ecoeler.model.code.AppVoiceCode.*;


@Service
@Slf4j
public class AppVoiceActionServiceImpl implements AppVoiceActionService {

    /**
     * 非网关设备代号
     */
    public static final int GATEWAY_LIKE_NOT = 0;

    @Resource
    UserFamilyMapper userFamilyMapper;

    @Autowired
    RoomMapper roomMapper;

    @Autowired
    DeviceMapper deviceMapper;

    @Autowired
    DeviceTypeMapper deviceTypeMapper;

    @Autowired
    DeviceKeyMapper deviceKeyMapper;

    @Autowired
    DeviceDataMapper deviceDataMapper;

    /**
     * 根据用户ID查询用户的设备列表
     *
     * @param userVoiceDto
     * @return
     */
    @Override
    public List<DeviceVoiceBean> getDeviceVoiceBeans(UserVoiceDto userVoiceDto) {

        if (userVoiceDto == null || userVoiceDto.getUserId() == null)
            throw new ServiceException(ACTION_PARAMS_ERROR);

        List<DeviceVoiceBean> deviceVoiceBeans;
        try {


            //查询出用户拥有的家庭ids
            List<UserFamily> families = userFamilyMapper.selectList(
                    Query.of(UserFamily.class).eq("app_user_id", userVoiceDto.getUserId())
            );
            if (CollUtil.isEmpty(families))
                return new ArrayList<>();
            List<Long> familyIds = families.parallelStream().map(UserFamily::getId).collect(Collectors.toList());


            //查询家庭下面设备集合
            List<Device> devices = this.getDeviceList(
                    DeviceVoiceDto.of()
                            .setFamilyIds(familyIds)
                            .setGatewayLike(DeviceVoiceDto.GATEWAY_LIKE_IS_NOT)
            );
            if (CollUtil.isEmpty(devices))
                return new ArrayList<>();
            List<String> productIds = devices.parallelStream().map(Device::getProductId).collect(Collectors.toList());
            List<Long> roomIds = devices.parallelStream().map(Device::getRoomId).collect(Collectors.toList());
            log.info("查詢到的设备Ids：{}", JSON.toJSONString(roomIds));

            //查询设备对应房间信息集合
            List<Room> rooms = roomMapper.selectList(
                    Query.of(Room.class).in(CollUtil.isNotEmpty(roomIds), "id", roomIds)
            );
            Map<Long, Room> roomMap = rooms.parallelStream().collect(Collectors.toMap(Room::getId, i -> i));

            //查询设备对应的设备类型信息
            List<DeviceType> deviceTypes = deviceTypeMapper.selectList(
                    Query.of(DeviceType.class)
                            .in(CollUtil.isNotEmpty(productIds), "product_id", productIds)
            );
            Map<String, DeviceType> deviceTypeMap = deviceTypes.parallelStream().collect(Collectors.toMap(DeviceType::getProductId, i -> i));


            deviceVoiceBeans = devices.parallelStream()
                    .map(it -> {

                        //设备基本信息
                        DeviceVoiceBean deviceVoiceBean = DeviceVoiceBean.of()
                                .setId(it.getDeviceId())
                                .setDeviceName(it.getDeviceName())
                                .setNicknames(it.getDeviceName());

                        //设备类型信息
                        DeviceType deviceType = deviceTypeMap.get(it.getProductId());
                        if (deviceType != null) {
                            deviceVoiceBean.setAlexaDisplayName(deviceType.getAlexaDisplayName())
                                    .setGoogleTraitNames(deviceType.getGoogleTraitNames())
                                    .setGoogleTypeName(deviceType.getGoogleTypeName())
                                    .setDescription(it.getEnTypeName())
                                    .setDefaultNames(it.getEnTypeName())
                            ;
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
            throw new ServiceException(ACTION_SELECT_DEVICE_LIST_ERROR);
        }
        return deviceVoiceBeans;
    }

    @Override
    public DeviceInfo getDeviceStatesByDeviceId(String deviceId) {

        DeviceInfo deviceInfo;
        if (EptUtil.isEmpty(deviceId))
            throw new ServiceException(ACTION_PARAMS_ERROR);

        //查询设备在线离线
        Device device = this.getDevice(DeviceVoiceDto.of().setDeviceId(deviceId));

        //生成返回对象，并设置网络状态
        deviceInfo = DeviceInfo.of()
                .setDeviceId(device.getDeviceId())
                .setOnline(device.getNetState() == DeviceStatusConst.ONLINE);


        //查询设备data
        List<DeviceData> deviceDatas = this.getDeviceDataList(DeviceDataVoiceDto.of().setDeviceId(deviceId));
        if (CollUtil.isEmpty(deviceDatas)) return DeviceInfo.of().setDeviceStateBeans(CollUtil.list(false));
        List<String> deviceKeyList = deviceDatas.parallelStream().map(DeviceData::getDataKey).collect(Collectors.toList());

        //查询设备可控可上传key
        List<DeviceKey> deviceKeys = this.getDeviceKeys(DeviceKeyVoiceDto.of().setDataKeys(deviceKeyList));
        Map<String, DeviceKey> deviceKeyMap = deviceKeys.parallelStream().collect(Collectors.toMap(DeviceKey::getDataKey, i -> i));

        List<DeviceStateBean> deviceStateBeans = deviceDatas.parallelStream()
                .map(it -> {
                    //获取data对应的key
                    DeviceKey deviceKey = deviceKeyMap.get(it.getDataKey());
                    if (deviceKey != null) {
                        DeviceStateBean stateBean = DeviceStateBean.of().setValue(it.getDataValue());
                        stateBean.setDeviceId(device.getDeviceId())
                                .setAlexaStateName(deviceKey.getAlexaStateName())
                                .setDataKey(deviceKey.getDataKey())
                                .setGoogleStateName(deviceKey.getGoogleStateName())
                                .setAlexaInterface(deviceKey.getAlexaInterface())
                                .setCreateTime(it.getCreateTime())
                        ;
                        return stateBean;
                    }
                    //没有找到说明该data不是可控可上传的data
                    return null;
                })
                //过滤null值
                .filter(Objects::nonNull)
                .collect(Collectors.toList());


        deviceInfo.setDeviceStateBeans(deviceStateBeans);


        return deviceInfo;
    }

    @Override
    public List<DeviceKey> getDeviceAbleControlKey(String deviceId) {

        if (EptUtil.isEmpty(deviceId))
            ExceptionCast.cast(ACTION_PARAMS_ERROR);

        Device device = this.getDevice(DeviceVoiceDto.of().setDeviceId(deviceId));

        List<DeviceKey> deviceKeys = this.getDeviceKeys(DeviceKeyVoiceDto.of().setProductId(device.getProductId()));

        return deviceKeys;
    }


    @Override
    public List<DeviceInfo> getDeviceStatesByIds(List<String> deviceIds) {

        if (CollUtil.isEmpty(deviceIds))
            ExceptionCast.cast(ACTION_PARAMS_ERROR);

        //查询所有的设备
        List<Device> devices = this.getDeviceList(DeviceVoiceDto.of().setDeviceIds(deviceIds));
        if (CollUtil.isEmpty(devices)) return ListUtil.empty();

        //生成返回deviceInfo集合
        List<DeviceInfo> deviceInfos = devices.parallelStream()
                .map(device -> DeviceInfo.of()
                        .setDeviceId(device.getDeviceId())
                        .setOnline(device.getNetState() == DeviceStatusConst.ONLINE)
                )
                .collect(Collectors.toList());


        //查询设备data
        List<DeviceData> deviceDatas = this.getDeviceDataList(DeviceDataVoiceDto.of().setDeviceIds(deviceIds));
        if (CollUtil.isEmpty(deviceDatas)) return deviceInfos;
        List<String> deviceKeyList = deviceDatas.parallelStream().map(DeviceData::getDataKey).collect(Collectors.toList());


        //查询设备可控可上传key
        List<DeviceKey> deviceKeys = this.getDeviceKeys(DeviceKeyVoiceDto.of().setDataKeys(deviceKeyList));
        Map<String, DeviceKey> deviceKeyMap = deviceKeys.parallelStream().collect(Collectors.toMap(DeviceKey::getDataKey, i -> i));

        //组装设备状态信息，并按deviceId分组
        Map<String, List<DeviceStateBean>> DeviceStateBeanGroupMap = deviceDatas.parallelStream()

                .map(deviceData -> {
                    //获取data对应的key
                    DeviceKey deviceKey = deviceKeyMap.get(deviceData.getDataKey());
                    if (deviceKey != null) {

                        return DeviceStateBean.of()
                                .setValue(deviceData.getDataValue())
                                .setDeviceId(deviceData.getDeviceId())
                                .setAlexaStateName(deviceKey.getAlexaStateName())
                                .setDataKey(deviceKey.getDataKey())
                                .setGoogleStateName(deviceKey.getGoogleStateName())
                                .setAlexaInterface(deviceKey.getAlexaInterface());

                    }
                    //没有找到说明该data不是可控可上传的data
                    return null;
                })
                //过滤null值
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(DeviceStateBean::getDeviceId));

        //组合每个device的状态集合
        deviceInfos.parallelStream().forEach(deviceInfo -> {

            List<DeviceStateBean> deviceStateBeans = DeviceStateBeanGroupMap.get(deviceInfo.getDeviceId());

            if (deviceStateBeans != null) {

                deviceInfo.setDeviceStateBeans(deviceStateBeans);

            }

        });
        return deviceInfos;
    }

    @Override
    public List<DeviceKey> getDeviceKeys(DeviceKeyVoiceDto dto) {

        QueryWrapper<DeviceKey> query = Query.of(DeviceKey.class);
        if (dto != null) {

            query
                    .eq("action_type", DeviceKeyVoiceDto.ACTION_TYPE_1)
                    .in(EptUtil.isNotEmpty(dto.getDataKeys()), "device_key", dto.getDataKeys())
                    .eq(EptUtil.isNotEmpty(dto.getAlexaStateName()), "alexa_state_name", dto.getAlexaStateName())
                    .eq(EptUtil.isNotEmpty(dto.getGoogleStateName()), "google_state_name", dto.getGoogleStateName())
                    .eq(EptUtil.isNotEmpty(dto.getProductId()), "product_id", dto.getProductId())
            ;
        }

        //查询设备可控可上传key
        try {
            return deviceKeyMapper.selectList(query);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ACTION_SELECT_DEVICE_KEY_ERROR);
        }

    }

    @Override
    public List<DeviceData> getDeviceDataList(DeviceDataVoiceDto dto) {

        //查询所有设备的data信息
        QueryWrapper<DeviceData> query = Query.of(DeviceData.class);

        if (dto != null) {
            query
                    .eq(EptUtil.isNotEmpty(dto.getDeviceId()), "device_id", dto.getDeviceId())
                    .eq(EptUtil.isNotEmpty(dto.getDataKey()), "data_key", dto.getDataKey())
                    .eq(EptUtil.isNotEmpty(dto.getId()), "id", dto.getId())
                    .eq(EptUtil.isNotEmpty(dto.getSeq()), "seq", dto.getSeq())
                    .in(EptUtil.isNotEmpty(dto.getDeviceIds()), "device_id", dto.getDeviceIds())
            ;
        }

        try {

            return deviceDataMapper.selectList(query);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ACTION_SELECT_DEVICE_LIST_ERROR);
        }
    }


    @Override
    public DeviceType getDeviceType(DeviceTypeVoiceDto dto) {


        QueryWrapper<DeviceType> query = Query.of(DeviceType.class);

        if (dto != null) {
            query
                    .eq(EptUtil.isNotEmpty(dto.getProductId()), "product_id", dto.getProductId())
            ;
        }


        DeviceType deviceType;
        try {

            deviceType = deviceTypeMapper.selectOne(query);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ACTION_SELECT_DEVICE_TYPE_ERROR);
        }

        if (deviceType == null)
            throw new ServiceException(ACTION_DEVICE_TYPE_NOT_EXIST);

        return deviceType;

    }

    public Device getDevice(DeviceVoiceDto dto) {

        QueryWrapper<Device> query = Query.of(Device.class);
        if (dto != null) {
            query
                    .eq(EptUtil.isNotEmpty(dto.getFamilyId()), "device_id", dto.getFamilyId())
                    .eq(EptUtil.isNotEmpty(dto.getNetState()), "net_state", dto.getNetState())
                    .eq(EptUtil.isNotEmpty(dto.getProductId()), "product_id", dto.getProductId())
                    .eq(EptUtil.isNotEmpty(dto.getFamilyId()), "family_id", dto.getFamilyId())
                    .eq(EptUtil.isNotEmpty(dto.getDeviceName()), "device_name", dto.getDeviceName())
                    .eq(EptUtil.isNotEmpty(dto.getDeviceTypeName()), "device_type_name", dto.getDeviceTypeName())
                    .eq(EptUtil.isNotEmpty(dto.getGatewayLike()), "gate_way_like", dto.getGatewayLike())

            ;
        }

        Device device;
        try {
            device = deviceMapper.selectOne(query);
        } catch (Exception e) {
            e.printStackTrace();
            //抛出查询异常
            throw new ServiceException(ACTION_DEVICE_NOT_EXIST);
        }

        //如果为空，抛出异常
        if (device == null)
            throw new ServiceException(ACTION_DEVICE_NOT_EXIST);

        return device;
    }

    @Override
    public List<Device> getDeviceList(DeviceVoiceDto dto) {


        QueryWrapper<Device> query = Query.of(Device.class);

        if (dto != null) {

            query
                    .eq(EptUtil.isNotEmpty(dto.getDeviceId()), "device_id", dto.getDeviceId())
                    .eq(EptUtil.isNotEmpty(dto.getNetState()), "net_state", dto.getNetState())
                    .eq(EptUtil.isNotEmpty(dto.getProductId()), "product_id", dto.getProductId())
                    .eq(EptUtil.isNotEmpty(dto.getDeviceName()), "device_name", dto.getDeviceName())
                    .eq(EptUtil.isNotEmpty(dto.getFamilyId()), "family_id", dto.getFamilyId())
                    .eq(EptUtil.isNotEmpty(dto.getDeviceTypeName()), "device_type_name", dto.getDeviceTypeName())
                    .eq(EptUtil.isNotEmpty(dto.getGatewayLike()), "gateway_like", dto.getGatewayLike())
                    .in(EptUtil.isNotEmpty(dto.getDeviceIds()), "device_id", dto.getDeviceIds())
                    .in(EptUtil.isNotEmpty(dto.getFamilyIds()), "family_id", dto.getFamilyIds())
            ;

        }

        try {

            return deviceMapper.selectList(query);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ACTION_SELECT_DEVICE_TYPE_ERROR);
        }
    }

}
