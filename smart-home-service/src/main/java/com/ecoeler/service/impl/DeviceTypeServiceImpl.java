package com.ecoeler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.bean.v1.DeviceTypeBean;
import com.ecoeler.app.entity.DeviceType;
import com.ecoeler.app.mapper.DeviceTypeMapper;
import com.ecoeler.app.service.IDeviceTypeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
@Service
public class DeviceTypeServiceImpl extends ServiceImpl<DeviceTypeMapper, DeviceType> implements IDeviceTypeService {
    /**
     * 返回deviceType一二级列表
     * @return
     */
    @Override
    public DeviceTypeBean detailList() {
        /**
         * 查询所有DeviceType
         */
        LambdaQueryWrapper<DeviceType> wrapper=new LambdaQueryWrapper<>();
        wrapper.select(DeviceType::getId,
                DeviceType::getZhTypeName,
                DeviceType::getEnTypeName,
                DeviceType::getImage,
                DeviceType::getEnPrimaryType,
                DeviceType::getZhPrimaryType,
                DeviceType::getNetType);
        List<DeviceType> dataDeviceTypeList = baseMapper.selectList(wrapper);
        Map<String, List<DeviceType>> map = dataDeviceTypeList.stream().collect(Collectors.groupingBy(DeviceType::getZhPrimaryType));
        List<DeviceType> primaryTypeList=new ArrayList<>();
        map.forEach((key, value) -> {
            DeviceType deviceType=new DeviceType();
            deviceType.setZhPrimaryType(key);
            deviceType.setEnPrimaryType(value.get(0).getEnPrimaryType());
            primaryTypeList.add(deviceType);

        });
        DeviceTypeBean result=new DeviceTypeBean();
        result.setMap(map);
        result.setPrimaryTypeList(primaryTypeList);
        return result;
    }
}
