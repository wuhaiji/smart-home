package com.ecoeler.core.scene;


import com.alibaba.fastjson.JSONObject;
import com.ecoeler.app.entity.SceneAction;
import com.ecoeler.core.DeviceEvent;
import com.ecoeler.app.msg.OrderInfo;
import com.ecoeler.core.type.EnumType;
import com.ecoeler.core.type.IType;
import com.ecoeler.core.type.IntegerType;
import com.ecoeler.utils.SpringUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 场景执行器
 * @author tang
 * @since 2020/9/22
 */
@Component
public class SceneExecutor {

    private static ScheduledThreadPoolExecutor executor=new ScheduledThreadPoolExecutor(8, new ThreadPoolExecutor.AbortPolicy());

    /**
     * 组装指令
     * @param action
     * @return
     */
    private OrderInfo parseOrder(SceneAction action){
        OrderInfo orderInfo=new OrderInfo();
        orderInfo.setDeviceId(action.getDeviceId());
        orderInfo.setProductId(action.getProductId());
        JSONObject msg=new JSONObject();
        msg.put(
                action.getDataKey(),
                action.getDataValue()
        );
        orderInfo.setMsg(msg);
        return orderInfo;
    }

    /**
     * 执行指令
     * @param action
     */
    private void execute0(SceneAction action){
        DeviceEvent deviceEvent = (DeviceEvent)SpringUtil.getBean(action.getEventClass());
        deviceEvent.order(parseOrder(action));
    }

    public void execute(List<SceneAction> sceneActionList){
        for (SceneAction sceneAction : sceneActionList) {
            if(sceneAction.getDelayTime()==0){
                execute0(sceneAction);
            }else{
                executor.schedule(
                        new Runnable() {
                            @Override
                            public void run() {
                                execute0(sceneAction);
                            }
                            },
                        sceneAction.getDelayTime(),
                        TimeUnit.SECONDS
                );
            }
        }
    }

}
