package com.ecoeler.core.resolver;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecoeler.app.entity.Device;
import com.ecoeler.app.service.IDeviceService;
import com.ecoeler.app.msg.KeyMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * V1 通道的消息分解器
 * @author tang
 * @since 2020/9/15
 */
@Slf4j
@Component
public class OldMsgResolver implements KeyResolver<JSONObject> {

    @Autowired
    private IDeviceService deviceService;

    @Override
    public ResolveResult resolve(JSONObject msg) {
        ResolveResult res=new ResolveResult();
        //设备ID
        String deviceId=(String)msg.get("id");
        //产品ID
        QueryWrapper<Device> q=new QueryWrapper<>();
        q.eq("device_id",deviceId)
                .select("product_id");
        log.info("----deviceId:{}",deviceId);
        Device one = deviceService.getOne(q);
        res.setProductId(one.getProductId());
        res.setDeviceId(deviceId);
        res.setKeyMsgList(getKeyList(msg));
        return res;
    }


    private List<KeyMsg> getKeyList(JSONObject msg) {
        String deviceId = (String)msg.remove("id");
        Long seq=System.currentTimeMillis();
        List<KeyMsg> keyMsgList=new ArrayList<>();
        for (Map.Entry<String, Object> entry : msg.entrySet()) {
            KeyMsg keyMsg=new KeyMsg();
            keyMsg.setDeviceId(deviceId);
            keyMsg.setSeq(seq);
            keyMsg.setDataKey(entry.getKey());
            keyMsg.setDataValue(entry.getValue());
            keyMsgList.add(keyMsg);
        }
        return keyMsgList;
    }


}
