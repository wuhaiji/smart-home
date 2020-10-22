package com.ecoeler.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.app.bean.v1.PageBean;
import com.ecoeler.app.dto.v1.WebDeviceTypeDto;
import com.ecoeler.app.entity.DeviceType;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
public interface IDeviceTypeService extends IService<DeviceType> {
    /**
     * 返回type的一二级列表
     * @return
     */
    Map<String, List<DeviceType>> detailList(String local);

    /**
     * 查询设备类型
     *
     * @return 设备类型列表
     * @param webDeviceTypeDto
     */
    PageBean<DeviceType> selectDeviceType(WebDeviceTypeDto webDeviceTypeDto);

    /**
     * app端查詢全部设备类型
     * @return
     */
    List<DeviceType>appList();
}
