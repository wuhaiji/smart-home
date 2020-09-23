package com.ecoeler.core.resolver;

import com.alibaba.fastjson.JSONObject;
import com.ecoeler.app.msg.DeviceMsg;
import com.ecoeler.app.msg.KeyMsg;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 分解键
 * @author tang
 * @since 2020/9/15
 */
@Component
public class DeviceMsgResolver implements KeyResolver<DeviceMsg> {


    @Override
    public ResolveResult resolve(DeviceMsg msg) {
        ResolveResult res=new ResolveResult();
        res.setDeviceId(msg.getDevId());
        res.setProductId(msg.getProductId());
        res.setKeyMsgList(getKeyList(msg));
        return res;
    }

    private List<KeyMsg> getKeyList(DeviceMsg msg) {
        JSONObject jsonObject=(JSONObject)msg.getStatus();
        List<KeyMsg> list=new ArrayList<>();
        for (Map.Entry<String,Object> entry:jsonObject.entrySet()){
            KeyMsg keyMsg=new KeyMsg();
            keyMsg.setDeviceId(msg.getDevId());
            keyMsg.setDataKey(entry.getKey());
            keyMsg.setDataValue(entry.getValue());
            keyMsg.setSeq(msg.getSeq());
            list.add(keyMsg);
        }
        return list;
    }

}
