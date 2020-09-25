package com.ecoeler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.bean.v1.PageBean;
import com.ecoeler.app.bean.v1.WebDeviceDataBean;
import com.ecoeler.app.dto.v1.WebDeviceDto;
import com.ecoeler.app.dto.v1.WebDeviceTypeDto;
import com.ecoeler.app.entity.*;
import com.ecoeler.app.mapper.*;
import com.ecoeler.app.service.*;

import com.ecoeler.util.OverviewUtil;
import com.ecoeler.util.RatioUtil;
import com.ecoeler.util.TimeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        queryWrapper.select("id", "coordinate", "coordinate_type");
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
        Map<String, LocalDateTime> stringLocalDateTimeMap = TimeUtil.verifyQueryTime(webDeviceDto);
        LocalDateTime startTime = stringLocalDateTimeMap.get(TimeUtil.START);
        LocalDateTime endTime = stringLocalDateTimeMap.get(TimeUtil.END);
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
        queryWrapper
                .eq(!"".equals(deviceId.trim()), "device_id", deviceId)
                .eq(!"".equals(deviceName.trim()), "device_name", deviceName)
                .eq(!"".equals(productId.trim()), "product_id", productId)
                .eq(netState != -1, "net_state", netState)
                .ge(timeType != -1 && startTime != null, timeLine, startTime)
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
     * 分页按条件查询设备类型
     *
     * @param webDeviceTypeDto
     * @return
     */
    @Override
    public PageBean<DeviceType> selectDeviceType(WebDeviceTypeDto webDeviceTypeDto) {

        String typeName = Optional.ofNullable(webDeviceTypeDto.getTypeName()).orElse("");
        String productId = Optional.ofNullable(webDeviceTypeDto.getProductId()).orElse("");
        QueryWrapper<DeviceType> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("zh_type_name", "en_type_name", "id", "product_id", "image", "default_icon", "create_time", "update_time")
                .eq(!"".equals(typeName.trim()), "zh_type_name", typeName)
                .eq(!"".equals(productId.trim()), "product_id", productId)
                .or()
                .eq(!"".equals(typeName.trim()), "en_type_name", typeName)
                .eq(!"".equals(productId.trim()), "product_id", productId);
        Page<DeviceType> page = new Page<>();
        page.setCurrent(webDeviceTypeDto.getCurrent());
        page.setSize(webDeviceTypeDto.getSize());
        Page<DeviceType> deviceTypePage = deviceTypeMapper.selectPage(page, queryWrapper);
        PageBean<DeviceType> result = new PageBean<>();
        result.setList(deviceTypePage.getRecords());
        result.setTotal(deviceTypePage.getTotal());
        result.setPages(deviceTypePage.getPages());
        return result;
    }

    /**
     * 查询所有设备类型 下拉框
     *
     * @return
     */
    @Override
    public List<DeviceType> selectAllDeviceType() {
        QueryWrapper<DeviceType> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("product_id", "zh_type_name", "en_type_name");
        return deviceTypeMapper.selectList(queryWrapper);
    }




}
