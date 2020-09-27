package com.ecoeler.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.dto.v1.DeviceControlDto;
import com.ecoeler.app.entity.Device;
import com.ecoeler.app.entity.DeviceSwitch;
import com.ecoeler.app.mapper.DeviceMapper;
import com.ecoeler.app.mapper.DeviceSwitchMapper;
import com.ecoeler.app.msg.OrderInfo;
import com.ecoeler.app.service.IDeviceSwitchService;
import com.ecoeler.core.DeviceEvent;
import com.ecoeler.util.SpringUtils;
import com.ecoeler.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 设备开关状态表 服务实现类
 * </p>
 *
 * @author tang
 * @since 2020-09-25
 */
@Service
public class DeviceSwitchServiceImpl extends ServiceImpl<DeviceSwitchMapper, DeviceSwitch> implements IDeviceSwitchService {

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private DeviceSwitchMapper deviceSwitchMapper;

    @Async
//    @Override
    public void openSwitch1(DeviceControlDto deviceControlDto) {
        // 1.查询所有设备
        List<Device> deviceList = getDeviceList(deviceControlDto);
        // 2.获取productId集合
        List<String> productIdList = deviceList.stream().map(Device::getProductId).collect(Collectors.toList());
        //获得设备类型的所有 开关键
        Map<String, List<DeviceSwitch>> deviceSwitchMap = getDeviceSwitchMap(productIdList);

        for(Device device:deviceList){
            List<DeviceSwitch> deviceSwitches = deviceSwitchMap.get(device.getProductId());
            //组装命令
            OrderInfo orderInfo=new OrderInfo();
            orderInfo.setDeviceId(device.getDeviceId());
            orderInfo.setProductId(device.getProductId());
            JSONObject json = new JSONObject();
            for (DeviceSwitch deviceSwitch : deviceSwitches) {
                json.put(deviceSwitch.getDataKey(),deviceSwitch.getDeviceOn());
            }
            orderInfo.setMsg(json);
            //发送器发送指令
            DeviceEvent deviceEvent = (DeviceEvent) SpringUtil.getBean(device.getEventClass());
            deviceEvent.order(orderInfo);
        }
    }

    @Async
    @Override
    public void openSwitch(DeviceControlDto deviceControlDto) {
        // 1.查询所有设备
        List<Device> deviceList = getDeviceList(deviceControlDto);
        // 2.以productId为键，DeviceSwitch为值 的map集合
        List<String> productIdList= deviceList.stream().map(Device::getProductId).collect(Collectors.toList());
        Map<String, List<DeviceSwitch>> deviceSwitchMap = getDeviceSwitchMap(productIdList);
        System.out.println("以productId为键，DeviceSwitch为值的map集合："+JSONObject.toJSONString(deviceSwitchMap));

        // 3.遍历设备集合，并下发指令
        for (Device device : deviceList) {
            String deviceId = device.getDeviceId();
            String productId = device.getProductId();
            String eventClass = device.getEventClass();
            JSONObject msg = new JSONObject();
            List<DeviceSwitch> deviceSwitches = deviceSwitchMap.get(productId);
            for(DeviceSwitch deviceSwitch : deviceSwitches) {
                msg.put(deviceSwitch.getDataKey(), deviceSwitch.getDeviceOn());
            }
            // 下发指令
            DeviceEvent deviceEvent = (DeviceEvent)SpringUtil.getBean(eventClass);
            deviceEvent.order(OrderInfo.of().setDeviceId(deviceId).setProductId(productId).setMsg(msg));
        }
    }

    @Async
    @Override
    public void closeSwitch(DeviceControlDto deviceControlDto) {
        // 1.查询所有设备
        List<Device> deviceList = getDeviceList(deviceControlDto);
        // 2.以productId为键，DeviceSwitch为值 的map集合
        List<String> productIdList= deviceList.stream().map(Device::getProductId).collect(Collectors.toList());
        Map<String, List<DeviceSwitch>> deviceSwitchMap = getDeviceSwitchMap(productIdList);
        System.out.println("以productId为键，DeviceSwitch为值的map集合："+JSONObject.toJSONString(deviceSwitchMap));

        // 3.遍历设备集合，并下发指令
        for (Device device : deviceList) {
            String deviceId = device.getDeviceId();
            String productId = device.getProductId();
            String eventClass = device.getEventClass();
            JSONObject msg = new JSONObject();
            List<DeviceSwitch> deviceSwitches = deviceSwitchMap.get(productId);
            for(DeviceSwitch deviceSwitch : deviceSwitches) {
                msg.put(deviceSwitch.getDataKey(), deviceSwitch.getDeviceOff());
            }
            // 下发指令
            DeviceEvent deviceEvent = (DeviceEvent)SpringUtil.getBean(eventClass);
            deviceEvent.order(OrderInfo.of().setDeviceId(deviceId).setProductId(productId).setMsg(msg));
        }
    }


    /**
     * 根据查deviceControlDto查询设备
     * @author wujihong
     * @param deviceControlDto
     * @since 15:53 2020-09-25
     */
    @Override
    public List<Device> getDeviceList(DeviceControlDto deviceControlDto) {
        QueryWrapper<Device> deviceQueryWrapper = new QueryWrapper<>();
        deviceQueryWrapper.eq(deviceControlDto.getFamilyId() != null,"family_id", deviceControlDto.getFamilyId());
        deviceQueryWrapper.eq(deviceControlDto.getRoomId() != null,"room_id", deviceControlDto.getRoomId());
        List<Device> deviceList = deviceMapper.selectList(deviceQueryWrapper);
        // 空值处理
        return Optional.ofNullable(deviceList).orElse(new ArrayList<Device>());
    }

    /**
     * 根据产品id查询device_switch
     * @author wujihong
     * @param productId
     * @since 15:54 2020-09-25
     */
    @Override
    public List<DeviceSwitch> getDeviceSwitchList(String productId) {
        QueryWrapper<DeviceSwitch> deviceSwitchQueryWrapper = new QueryWrapper<>();
        deviceSwitchQueryWrapper.eq("product_id", productId);
        List<DeviceSwitch> deviceSwitchList = deviceSwitchMapper.selectList(deviceSwitchQueryWrapper);
        // 空值处理
        return Optional.ofNullable(deviceSwitchList).orElse(new ArrayList<DeviceSwitch>());
    }

    /**
     * 根据产品ID集合查询 DeviceSwitch
     * @author wujihong
     * @param productIdList
     * @since 18:39 2020-09-25
     */
    public Map<String,List<DeviceSwitch>> getDeviceSwitchMap(List<String> productIdList){
        QueryWrapper<DeviceSwitch> deviceSwitchQueryWrapper = new QueryWrapper<>();
        deviceSwitchQueryWrapper.in("product_id",productIdList);
        return deviceSwitchMapper.selectList(deviceSwitchQueryWrapper).stream().collect(Collectors.groupingBy(DeviceSwitch::getProductId));
    }
}
