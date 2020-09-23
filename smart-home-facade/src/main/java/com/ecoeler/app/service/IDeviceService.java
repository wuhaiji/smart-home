package com.ecoeler.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.app.entity.Device;
import com.ecoeler.app.msg.OrderInfo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
public interface IDeviceService extends IService<Device> {

    /**
     * 控制设备
     * @param orderInfo
     */
    void control(OrderInfo orderInfo);

}
