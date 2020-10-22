package com.ecoeler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.bean.v1.PageBean;
import com.ecoeler.app.dto.v1.WebDeviceDto;
import com.ecoeler.app.dto.v1.WebDeviceTypeDto;
import com.ecoeler.app.entity.*;
import com.ecoeler.app.mapper.*;
import com.ecoeler.app.service.*;

import kotlin.jvm.internal.Lambda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
@Service
public class WebDeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IWebDeviceService {
    @Autowired
    private FamilyMapper familyMapper;

    @Autowired
    private DeviceTypeMapper deviceTypeMapper;


    /**
     * 设备分布
     *
     * @return
     */
    @Override
    public List<Family> selectMap() {
        QueryWrapper<Family> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(Family::getId,Family::getCoordinate,Family::getCoordinateType);
        return familyMapper.selectList(queryWrapper);
    }

    /**
     * 分页查询设备
     *
     * @param webDeviceDto 查询条件
     * @return
     */
    @Override
    public PageBean<Device> selectDeviceList(WebDeviceDto webDeviceDto) {
        String deviceId = Optional.ofNullable(webDeviceDto.getDeviceId()).orElse("");
        String deviceName = Optional.ofNullable(webDeviceDto.getDeviceName()).orElse("");
        String productId = Optional.ofNullable(webDeviceDto.getProductId()).orElse("");
        Integer netState = Optional.ofNullable(webDeviceDto.getNetState()).orElse(-1);
        //时间段字段  3-online_time 2-offline_time 0-create_time 1-update_time
        Integer timeType = Optional.ofNullable(webDeviceDto.getTimeType()).orElse(0);
        //获取查询时间
        Date startTime = webDeviceDto.getStartTime();
        Date endTime = webDeviceDto.getEndTime();
        String timeLine;
        switch (timeType) {
            case 3:
                timeLine = "online_time";
                break;
            case 2:
                timeLine = "offline_time";
                break;
            case 0:
                timeLine = "create_time";
                break;
            case 1:
                timeLine = "update_time";
                break;
            default:
                timeLine = "";
                break;
        }
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(!"".equals(deviceId.trim()), Device::getDeviceId, deviceId)
                .eq(!"".equals(deviceName.trim()), Device::getDeviceName, deviceName)
                .eq(!"".equals(productId.trim()), Device::getProductId, productId)
                .eq(netState != -1, Device::getNetState, netState);
        queryWrapper.ge(timeType != -1 && startTime != null, timeLine, startTime)
                .le(timeType != -1 && endTime != null, timeLine, endTime);
        Page<Device> page = new Page<>();
        page.setCurrent(webDeviceDto.getCurrent());
        page.setSize(webDeviceDto.getSize());
        Page<Device> webUserPage = baseMapper.selectPage(page, queryWrapper);
        PageBean<Device> result = new PageBean<>();
        result.setTotal(webUserPage.getTotal());
        result.setPages(webUserPage.getPages());
        result.setList(webUserPage.getRecords());
        return result;
    }


    /**
     * 查询所有设备类型 下拉框
     *
     * @return 所有设备类型
     */
    @Override
    public List<DeviceType> selectAllDeviceType() {
        LambdaQueryWrapper<DeviceType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(DeviceType::getProductId,DeviceType::getEnTypeName,DeviceType::getZhTypeName);
        return deviceTypeMapper.selectList(queryWrapper);
    }


}
