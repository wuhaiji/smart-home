package com.ecoeler.core;


import com.ecoeler.app.service.IDeviceDataService;
import com.ecoeler.app.service.IDeviceKeyService;
import com.ecoeler.app.service.IDeviceService;
import com.ecoeler.core.deliver.Deliver;
import com.ecoeler.core.resolver.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 设备事件设置
 * @author tang
 * @since 2020/9/15
 */
@Configuration
public class DeviceEventConfig {

    @Bean
    public DeviceEvent v1DeviceEvent(
            KeyResolver oldMsgResolver,
            Deliver orderDeliver,
            IDeviceService deviceService,
            IDeviceDataService deviceDataService,
            IDeviceKeyService deviceKeyService
            ){
        return new DeviceEvent()
                .setResolver(oldMsgResolver)
                .setDeliver(orderDeliver)
                .setDeviceService(deviceService)
                .setDeviceDataService(deviceDataService)
                .setDeviceKeyService(deviceKeyService);
    }

    @Bean
    public DeviceEvent v2DeviceEvent(
            KeyResolver deviceMsgResolver,
            Deliver orderDeliver,
            IDeviceService deviceService,
            IDeviceDataService deviceDataService,
            IDeviceKeyService deviceKeyService
    ){
        return new DeviceEvent()
                .setResolver(deviceMsgResolver)
                .setDeliver(orderDeliver)
                .setDeviceService(deviceService)
                .setDeviceDataService(deviceDataService)
                .setDeviceKeyService(deviceKeyService);
    }

}
