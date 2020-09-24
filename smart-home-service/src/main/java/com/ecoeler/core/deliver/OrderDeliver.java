package com.ecoeler.core.deliver;

import com.ecoeler.config.mqtt.MqttSenderUtil;
import com.ecoeler.constant.MqttTopicConst;
import com.ecoeler.app.msg.OrderInfo;
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
        MqttSenderUtil.getMqttSender().sendToMqtt(MqttTopicConst.CMD_CHANNEL +order.getDeviceId(),order.getMsg().toJSONString());
    }
}
