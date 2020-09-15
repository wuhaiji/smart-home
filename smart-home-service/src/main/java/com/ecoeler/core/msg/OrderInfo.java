package com.ecoeler.core.msg;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * 作为order事件的入参
 * @author tang
 * @since 2020/7/20
 */
@Data
public class OrderInfo {

    private String deviceId;

    private String productId;

    private JSONObject msg;

}
