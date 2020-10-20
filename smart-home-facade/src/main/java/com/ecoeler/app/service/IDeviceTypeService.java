package com.ecoeler.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
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



    List<DeviceType>appList();
}
