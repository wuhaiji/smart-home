package com.ecoeler.core;

import com.alibaba.fastjson.JSONObject;
import com.ecoeler.app.msg.DeviceMsg;
import com.ecoeler.constant.DeviceActionConst;
import com.ecoeler.constant.MqttTopicConst;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private DeviceEvent v1DeviceEvent;

    @Autowired
    private DeviceEvent v2DeviceEvent;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        String topic=(String)message.getHeaders().get("mqtt_receivedTopic");
        String payload=(String) message.getPayload();
        try {
            if (MqttTopicConst.V1_HEART_CHANNEL.equals(topic)){
                // /heart/
                v1DeviceEvent.online(JSONObject.parseObject(payload));
                v1DeviceEvent.record(JSONObject.parseObject(payload));
            }
            if(MqttTopicConst.V1_LWT_CHANNEL.equals(topic)){
                // 老版本 遗言
                JSONObject msg=new JSONObject();
                msg.put("id",payload.trim());
                v1DeviceEvent.offline(msg);
            }
            if(MqttTopicConst.V2_ALIVE_CHANNEL.equals(topic)){
                DeviceMsg deviceMsg = JSONObject.parseObject(payload, DeviceMsg.class);
                String action=deviceMsg.getAct();
                if(DeviceActionConst.ONLINE.equals(action)){
                    v2DeviceEvent.online(deviceMsg);
                }
                if(DeviceActionConst.OFFLINE.equals(action)){
                    v2DeviceEvent.offline(deviceMsg);
                }
                if(DeviceActionConst.CHANGE.equals(action)){
                    v2DeviceEvent.record(deviceMsg);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
