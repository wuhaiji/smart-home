package com.ecoeler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.bean.v1.PageBean;
import com.ecoeler.app.bean.v1.WebDeviceDataBean;
import com.ecoeler.app.dto.v1.WebDeviceDto;
import com.ecoeler.app.entity.*;
import com.ecoeler.app.mapper.DeviceMapper;
import com.ecoeler.app.service.*;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.model.code.WebCustomerCode;

import com.ecoeler.model.code.WebDeviceCode;
import com.ecoeler.util.TimeUtil;
import org.checkerframework.checker.units.qual.A;
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
    private IFamilyService iFamilyService;

    @Autowired
    private IDeviceTypeService iDeviceTypeService;

    @Autowired
    private IDeviceDataService iDeviceDataService;

    @Autowired
    private IDeviceKeyService iDeviceKeyService;

    /**
     * 设备分布
     * @return
     */
    @Override
    public List<Family> selectMap() {
        try {
            QueryWrapper<Family> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("id", "coordinate", "coordinate_type");
            return iFamilyService.list(queryWrapper);
        } catch (Exception e) {
            log.error(Optional.ofNullable(e.getMessage()).orElse(""), Optional.ofNullable(e.getCause()).orElse(e));
            throw new ServiceException(WebDeviceCode.DEVICE_MAP);
        }
    }

    /**
     * 分页查询设备
     *
     * @param webDeviceDto 查询条件
     * @param page         分页
     * @return
     */
    @Override
    public PageBean<Device> selectDevice(WebDeviceDto webDeviceDto, Page<Device> page) {
        try {
            String deviceId = Optional.ofNullable(webDeviceDto.getDeviceId()).orElse("");
            String deviceName = Optional.ofNullable(webDeviceDto.getDeviceName()).orElse("");
            String deviceTypeName = Optional.ofNullable(webDeviceDto.getDeviceTypeName()).orElse("");
            Integer netState = Optional.ofNullable(webDeviceDto.getNetState()).orElse(-1);
            //时间段字段  0-online_time 1-offline_time 2-create_time 3-update_time
            Integer timeType = Optional.ofNullable(webDeviceDto.getTimeType()).orElse(-1);
            String timeLine;
            switch (timeType) {
                case 0:
                    timeLine = "online_time";
                    break;
                case 1:
                    timeLine = "offline_time";
                    break;
                case 2:
                    timeLine = "create_time";
                    break;
                case 3:
                    timeLine = "update_time";
                    break;
                default:
                    timeLine = "";
                    break;
            }
            //获取查询时间
            Map<String, LocalDateTime> stringLocalDateTimeMap = TimeUtil.verifyQueryTime(webDeviceDto);
            LocalDateTime startTime = stringLocalDateTimeMap.get(TimeUtil.START);
            LocalDateTime endTime = stringLocalDateTimeMap.get(TimeUtil.END);
            QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(!"".equals(deviceId.trim()), "device_id", deviceId);
            queryWrapper.eq(!"".equals(deviceName.trim()), "device_name", deviceName);
            queryWrapper.eq(!"".equals(deviceTypeName.trim()), "device_type_name", deviceTypeName);
            queryWrapper.ge(timeType != -1 && startTime != null, timeLine, startTime);
            queryWrapper.le(timeType != -1 && endTime != null, timeLine, endTime);
            Page<Device> webUserPage = baseMapper.selectPage(page, queryWrapper);
            PageBean<Device> result = new PageBean<>();
            result.setTotal(webUserPage.getTotal());
            result.setPages(webUserPage.getPages());
            result.setList(webUserPage.getRecords());
            return result;
        } catch (Exception e) {
            log.error(Optional.ofNullable(e.getMessage()).orElse(""), Optional.ofNullable(e.getCause()).orElse(e));
            throw new ServiceException(WebDeviceCode.DEVICE_LIST);
        }
    }

    @Override
    public List<DeviceType> selectDeviceType() {
        try {
            QueryWrapper<DeviceType> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("distinct type_name", "id");
            return iDeviceTypeService.list(queryWrapper);
        } catch (Exception e) {
            log.error(Optional.ofNullable(e.getMessage()).orElse(""), Optional.ofNullable(e.getCause()).orElse(e));
            throw new ServiceException(WebDeviceCode.DEVICE_TYPE);
        }
    }

    /**
     * 新增设备
     *
     * @param device
     * @return
     */
    @Override
    public Long addDevice(Device device) {
        try {
            baseMapper.insert(device);
            return device.getId();
        } catch (Exception e) {
            log.error(Optional.ofNullable(e.getMessage()).orElse(""), Optional.ofNullable(e.getCause()).orElse(e));
            throw new ServiceException(WebDeviceCode.ADD);
        }
    }

    /**
     * 修改设备
     *
     * @param device
     */
    @Override
    public void updateDevice(Device device) {
        try {
            baseMapper.updateById(device);
        } catch (Exception e) {
            log.error(Optional.ofNullable(e.getMessage()).orElse(""), Optional.ofNullable(e.getCause()).orElse(e));
            throw new ServiceException(WebDeviceCode.UPDATE);
        }
    }

    /**
     * 删除设备
     */
    @Override
    public void deleteDevice(Long id) {
        try {
            baseMapper.deleteById(id);
        } catch (Exception e) {
            log.error(Optional.ofNullable(e.getMessage()).orElse(""), Optional.ofNullable(e.getCause()).orElse(e));
            throw new ServiceException(WebDeviceCode.DELETE);
        }
    }

    /**
     * 查询设备参数
     * @param deviceId 设备ID
     * @return
     */
    @Override
    public List<WebDeviceDataBean> queryDeviceData(Long deviceId) {
        try {
            List<WebDeviceDataBean> result = new ArrayList<>();
            QueryWrapper<DeviceData> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("id", "device_id", "data_key", "data_value", "data_key_id");
            queryWrapper.eq("device_id", deviceId);
            List<DeviceData> deviceData = iDeviceDataService.list(queryWrapper);
            if (deviceData != null && deviceData.size() != 0) {
                for (DeviceData deviceDatum : deviceData) {
                    WebDeviceDataBean bean = new WebDeviceDataBean();
                    BeanUtils.copyProperties(deviceDatum, bean);
                    if (deviceDatum.getDataKeyId() != null) {
                        DeviceKey deviceKey = iDeviceKeyService.getById(deviceDatum.getDataKeyId());
                        if (deviceKey != null) {
                            bean.setKeyName(deviceKey.getKeyName());
                        }
                    }
                    result.add(bean);
                }
            }
            return result;
        } catch (Exception e) {
            log.error(Optional.ofNullable(e.getMessage()).orElse(""), Optional.ofNullable(e.getCause()).orElse(e));
            throw new ServiceException(WebDeviceCode.DEVICE_TYPE_DATA);
        }
    }

}
