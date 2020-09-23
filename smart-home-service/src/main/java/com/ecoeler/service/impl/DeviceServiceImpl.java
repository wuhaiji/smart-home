package com.ecoeler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.entity.Device;
import com.ecoeler.app.entity.DeviceType;
import com.ecoeler.app.mapper.DeviceMapper;
import com.ecoeler.app.mapper.DeviceTypeMapper;
import com.ecoeler.app.msg.OrderInfo;
import com.ecoeler.app.service.IDeviceService;
import com.ecoeler.core.DeviceEvent;
import com.ecoeler.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

    @Autowired
    private DeviceTypeMapper deviceTypeMapper;

    @Override
    public void control(OrderInfo orderInfo) {

        QueryWrapper<DeviceType> q=new QueryWrapper<>();
        q.eq("product_id",orderInfo.getProductId()).select("event_class");
        DeviceType deviceType = deviceTypeMapper.selectOne(q);

        DeviceEvent deviceEvent=(DeviceEvent)SpringUtil.getBean(deviceType.getEventClass());
        deviceEvent.getDeliver().deliver(orderInfo);
    }
}
