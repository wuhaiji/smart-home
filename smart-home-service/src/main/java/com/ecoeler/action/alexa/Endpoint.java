package com.ecoeler.action.alexa;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

/**
 * @author whj
 * @createTime 2020-02-21 13:34
 * @description discover返回信息设备端点模型
 **/
@Data
public class Endpoint {
    /**
     * 端点对象
     */
    private String endpointId;
    /**
     * 设备制造商名称
     */
    private String manufacturerName;
    /**
     * 设备描述
     */
    private String description;
    /**
     * 用户用来标识设备的名称。该值最多可包含128个字符，并且不应包含特殊字符或标点符号
     */
    private String friendlyName;
    /**
     * 在Alexa应用程序中，显示设备的类别。
     */
    private List<String> displayCategories;
    /**
     * 设备所能提供的特性/能力列表
     */
    private List<Capability> capabilities;
    private JSONObject cookie;



}

