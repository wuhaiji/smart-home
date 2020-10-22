package com.ecoeler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.bean.v1.PageBean;
import com.ecoeler.app.dto.v1.LocalStatic;
import com.ecoeler.app.dto.v1.WebDeviceTypeDto;
import com.ecoeler.app.entity.DeviceType;
import com.ecoeler.app.mapper.DeviceTypeMapper;
import com.ecoeler.app.service.IDeviceTypeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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
public class DeviceTypeServiceImpl extends ServiceImpl<DeviceTypeMapper, DeviceType> implements IDeviceTypeService {

    @Value("${goFastDFS.file.path}")
    public String goFastDFSPath;

    /**
     * 返回deviceType一二级列表
     *
     * @return
     */
    @Override
    public Map<String, List<DeviceType>> detailList(String local) {
        /**
         * 查询所有DeviceType
         */
        LambdaQueryWrapper<DeviceType> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(DeviceType::getId,
                DeviceType::getZhTypeName,
                DeviceType::getEnTypeName,
                DeviceType::getImage,
                DeviceType::getEnPrimaryType,
                DeviceType::getZhPrimaryType,
                DeviceType::getNetType);
        List<DeviceType> dataDeviceTypeList = baseMapper.selectList(wrapper);
        if (LocalStatic.EN.equals(local)) {
            //英文
            return dataDeviceTypeList.stream()
                    .filter(it -> it != null && !"".equals(it.getEnPrimaryType().trim()))
                    .peek(this::packageImage)
                    .collect(Collectors.groupingBy(DeviceType::getEnPrimaryType));
        } else {
            return dataDeviceTypeList.stream()
                    .filter(it -> it != null && !"".equals(it.getZhPrimaryType().trim()))
                    .peek(this::packageImage)
                    .collect(Collectors.groupingBy(DeviceType::getZhPrimaryType));
        }
    }

    @Override
    public List<DeviceType> appList() {
        /**
         * 查询所有DeviceType
         */
        LambdaQueryWrapper<DeviceType> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(DeviceType::getId,
                DeviceType::getProductId,
                DeviceType::getZhTypeName,
                DeviceType::getEnTypeName,
                DeviceType::getImage
        );
        return baseMapper.selectList(wrapper);
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
        queryWrapper.lambda().
                select(DeviceType::getEnTypeName,
                        DeviceType::getZhTypeName,
                        DeviceType::getId,
                        DeviceType::getProductId,
                        DeviceType::getImage,
                        DeviceType::getDefaultIcon,
                        DeviceType::getCreateTime,
                        DeviceType::getUpdateTime)
                .eq(!"".equals(typeName.trim()), DeviceType::getZhTypeName, typeName)
                .eq(!"".equals(productId.trim()), DeviceType::getProductId, productId)
                .or()
                .eq(!"".equals(typeName.trim()), DeviceType::getEnTypeName, typeName)
                .eq(!"".equals(productId.trim()), DeviceType::getProductId, productId);
        Page<DeviceType> page = new Page<>();
        page.setCurrent(webDeviceTypeDto.getCurrent());
        page.setSize(webDeviceTypeDto.getSize());
        Page<DeviceType> deviceTypePage = baseMapper.selectPage(page, queryWrapper);
        PageBean<DeviceType> result = new PageBean<>();
        result.setList(deviceTypePage.getRecords().stream().peek(this::packageImage).collect(Collectors.toList()));
        result.setTotal(deviceTypePage.getTotal());
        result.setPages(deviceTypePage.getPages());
        return result;
    }


    private void packageImage(DeviceType deviceType) {
        deviceType.setImage(goFastDFSPath + deviceType.getImage() + "?download=0");
    }
}
