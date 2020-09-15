package com.ecoeler.core;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;


/**
 * Mqtt 消息处理
 * @author tang
 * @since 2020/9/15
 */
@Component
public class MqttMsgHandler implements MessageHandler {

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        String topic=(String)message.getHeaders().get("mqtt_receivedTopic");
    }

}
