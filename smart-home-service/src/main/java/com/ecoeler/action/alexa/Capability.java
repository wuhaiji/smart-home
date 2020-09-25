package com.ecoeler.action.alexa;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;


/**
 * @author whj
 * @createTime 2020-02-21 14:24
 * @description discover返回信息设备端点模型中的能力模型
 **/
@Data
public class Capability {

    /**
     * 能力的类型。当前，唯一可用的类型是AlexaInterface。
     */
    private String type;
    /**
     * 功能接口的名称
     */
    @JSONField(name = "interface")
    private String interface_;
    /**
     * 接口的版本
     */
    private String version;
    /**
     * 自定义属性,默认为true,表示可以查询属性的状态和主动上报状态
     */
    private JSONObject properties;

    /**
     * 构造函数
     */
    public Capability() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("proactivelyReported", "true");
        jsonObject.put("retrievable", "true");
        jsonObject.put("supported", new JSONArray());
        properties = jsonObject;
        version = "3";
        type = "AlexaInterface";
    }

    /**
     * 添加属性的名称
     */
    public void supportedAddStateName(String stateName) {
        JSONObject nameObject = new JSONObject();
        nameObject.put("name", stateName);
        JSONArray supported = properties.getJSONArray("supported");
        supported.add(nameObject);
        properties.put("supported",supported);
    }


}
