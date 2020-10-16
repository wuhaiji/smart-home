package com.ecoeler.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.app.bean.v1.DeviceTypeBean;
import com.ecoeler.app.entity.DeviceType;

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
    DeviceTypeBean detailList();
}
