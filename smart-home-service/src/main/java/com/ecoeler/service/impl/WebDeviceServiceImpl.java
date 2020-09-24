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

    @Autowired
    private DeviceDataMapper deviceDataMapper;

    @Autowired
    private DeviceKeyMapper deviceKeyMapper;

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
        String deviceTypeName = Optional.ofNullable(webDeviceDto.getDeviceTypeName()).orElse("");
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
        queryWrapper.eq(!"".equals(deviceId.trim()), "device_id", deviceId);
        queryWrapper.eq(!"".equals(deviceName.trim()), "device_name", deviceName);
        queryWrapper.eq(!"".equals(deviceTypeName.trim()), "device_type_name", deviceTypeName);
        queryWrapper.eq(netState != -1, "net_state", netState);
        queryWrapper.ge(timeType != -1 && startTime != null, timeLine, startTime);
        queryWrapper.le(timeType != -1 && endTime != null, timeLine, endTime);
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
        QueryWrapper<DeviceType> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("distinct type_name", "id", "product_id", "image", "default_icon", "create_time", "update_time");
        String typeName = Optional.ofNullable(webDeviceTypeDto.getTypeName()).orElse("");
        String productId = Optional.ofNullable(webDeviceTypeDto.getProductId()).orElse("");
        queryWrapper.eq(!"".equals(typeName.trim()), "type_name", typeName);
        queryWrapper.eq(!"".equals(productId.trim()), "product_id", productId);
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
     * 新增设备
     *
     * @param device
     * @return
     */
    @Override
    public Long addDevice(Device device) {
        baseMapper.insert(device);
        return device.getId();
    }

    /**
     * 修改设备
     *
     * @param device
     */
    @Override
    public void updateDevice(Device device) {
        baseMapper.updateById(device);
    }

    /**
     * 删除设备
     */
    @Override
    public void deleteDevice(Long id) {
        baseMapper.deleteById(id);
    }

    /**
     * 查询设备参数
     *
     * @param deviceId 设备ID
     * @return
     */
    @Override
    public List<WebDeviceDataBean> queryDeviceData(Long deviceId) {
        List<WebDeviceDataBean> result = new ArrayList<>();
        QueryWrapper<DeviceData> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "device_id", "data_key", "data_value", "data_key_id");
        queryWrapper.eq("device_id", deviceId);
        List<DeviceData> deviceData = deviceDataMapper.selectList(queryWrapper);
        if (deviceData != null && deviceData.size() != 0) {
            for (DeviceData deviceDatum : deviceData) {
                WebDeviceDataBean bean = new WebDeviceDataBean();
                BeanUtils.copyProperties(deviceDatum, bean);
                if (deviceDatum.getDataKeyId() != null) {
                    DeviceKey deviceKey = deviceKeyMapper.selectById(deviceDatum.getDataKeyId());
                    if (deviceKey != null) {
                        bean.setKeyName(deviceKey.getZhKeyName());
                    }
                }
                result.add(bean);
            }
        }
        return result;
    }

}
