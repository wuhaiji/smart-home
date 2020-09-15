package com.ecoeler.core.deliver;

import com.ecoeler.config.mqtt.MqttSenderUtil;
import com.ecoeler.core.msg.OrderInfo;
import org.springframework.stereotype.Component;

/**
 * 指令下发
 * @author tang
 * @since 2020/9/15
 */
@Component
public class OrderDeliver implements Deliver {

    @Override
    public void deliver(OrderInfo order) {
        MqttSenderUtil.getMqttSender().sendToMqtt("/cmd/"+order.getDeviceId(),order.getMsg().toJSONString());
    }
}
