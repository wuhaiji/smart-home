package com.ecoeler.app.msg;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 作为order事件的入参
 * @author tang
 * @since 2020/7/20
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class OrderInfo {

    private String deviceId;

    private String productId;

    private JSONObject msg;

    public static OrderInfo of(){
        return new OrderInfo();
    }

}
