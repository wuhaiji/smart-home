package com.ecoeler.core.deliver;


import com.ecoeler.app.msg.OrderInfo;

/**
 * 指令下发方式包
 * @author tang
 * @since 2020/7/20
 */
public interface Deliver {

    /**
     * 发送
     * @param order
     */
    void deliver(OrderInfo order);

}
