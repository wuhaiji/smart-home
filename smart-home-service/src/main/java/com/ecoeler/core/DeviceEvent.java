package com.ecoeler.core;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ecoeler.app.entity.Device;
import com.ecoeler.app.entity.DeviceData;
import com.ecoeler.app.entity.DeviceKey;
import com.ecoeler.app.service.IDeviceDataService;
import com.ecoeler.app.service.IDeviceKeyService;
import com.ecoeler.app.service.IDeviceService;
import com.ecoeler.constant.DeviceStatusConst;
import com.ecoeler.core.deliver.Deliver;
import com.ecoeler.app.msg.*;
import com.ecoeler.core.resolver.KeyResolver;
import com.ecoeler.core.resolver.ResolveResult;
import com.ecoeler.core.type.BooleanType;
import com.ecoeler.core.type.EnumType;
import com.ecoeler.core.type.IType;
import com.ecoeler.core.type.IntegerType;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 设备事件
 *
 * @author tang
 * @since 2020/7/20
 */
@Data
@Accessors(chain = true)
public class DeviceEvent {

    private Deliver deliver;

    private KeyResolver resolver;

    private IDeviceService deviceService;

    private IDeviceDataService deviceDataService;

    private IDeviceKeyService deviceKeyService;



    /**
     * 获得键信息中的 键Type 信息
     * @param deviceKey
     * @return
     */
    private IType parse(DeviceKey deviceKey){
        String keyType = deviceKey.getKeyType();
        IType type = null;
        if (IType.TYPE_ENUM.equals(keyType)) {
            type = JSONObject.parseObject(deviceKey.getKeyInfo(), EnumType.class);
        }
        if (IType.TYPE_INTEGER.equals(keyType)) {
            type = JSONObject.parseObject(deviceKey.getKeyInfo(), IntegerType.class);
        }
        if(IType.TYPE_BOOLEAN.equals(keyType)){
            type= JSONObject.parseObject(deviceKey.getKeyInfo(), BooleanType.class);
        }
        return type;
    }


    private void argCheckWhenIn0(String deviceId,String productId, List<KeyMsg> keyMsgList) {
        //获得设备信息
        QueryWrapper<Device> q1=new QueryWrapper<>();
        q1.eq("device_id",deviceId)
                .eq("product_id",productId);
        Device device = deviceService.getOne(q1);

        if(device==null){
            return ;
        }

        for (KeyMsg keyMsg : keyMsgList) {
            //获取key信息
            QueryWrapper<DeviceKey> q2=new QueryWrapper<>();
            q2.eq("product_id",productId)
                    .eq("data_key",keyMsg.getDataKey());
            DeviceKey deviceKey = deviceKeyService.getOne(q2);

            if (deviceKey != null) {
                IType type = parse(deviceKey);
                if (type != null) {
                    keyMsg.setDataValue(type.checkin(keyMsg.getDataValue()));
                }

//                //触发key 绑定的 事件
//                if(!StringUtils.isBlank(limit.getConditionClass())){
//                    SceneCondition sceneCondition = (SceneCondition) SpringUtil.getBean(limit.getConditionClass());
//                    sceneExecutor.execute(device,limit.getDataKey(),keyMsg.getDataValue(), sceneCondition);
//                }
            }
        }
    }

    private void record0(List<KeyMsg> keyMsgList) {
        for (KeyMsg keyMsg : keyMsgList) {
            UpdateWrapper<DeviceData> updateWrapper=new UpdateWrapper<>();
            updateWrapper.eq("device_id",keyMsg.getDeviceId())
                    .eq("data_key",keyMsg.getDataKey())
                    .lt("seq",keyMsg.getSeq())
                    .set("data_value",keyMsg.getDataValue());

            deviceDataService.update(updateWrapper);

        }
    }

    /**
     * 设备上线
     *
     * @param msg 消息
     * @throws Exception
     */
    public void online(Object msg) throws Exception {
        ResolveResult res = resolver.resolve(msg);
        //更新设备状态
        String deviceId = res.getDeviceId();
        UpdateWrapper<Device> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("device_id", deviceId)
                .set("net_state", DeviceStatusConst.ONLINE)
                .set("online_time", new Date());
        boolean updated = deviceService.update(updateWrapper);
        if(!updated){return;}
        //更新设备键值
        List<KeyMsg> keyMsgList = res.getKeyMsgList();
        //参数检测
        this.argCheckWhenIn0(res.getDeviceId(),res.getProductId(), keyMsgList);
        if (keyMsgList.size() == 0) {
            return;
        }

        this.record0(keyMsgList);
    }

    /**
     * 设备下线
     *
     * @param msg 消息
     * @throws Exception
     */
    public void offline(Object msg) throws Exception {
        ResolveResult res = resolver.resolve(msg);
        String deviceId = res.getDeviceId();
        UpdateWrapper<Device> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("device_id", deviceId)
                .set("offline_time",new Date())
                .set("net_state",DeviceStatusConst.OFFLINE);
        deviceService.update(updateWrapper);
    }

    /**
     * 记录
     *
     * @param msg 消息
     * @throws Exception
     */
    public void record(Object msg) throws Exception {
        ResolveResult res = resolver.resolve(msg);
        List<KeyMsg> keyMsgList = res.getKeyMsgList();
        this.argCheckWhenIn0(res.getDeviceId(),res.getProductId(), keyMsgList);
        this.record0(keyMsgList);
    }

    /**
     * 发送指令
     *
     * @param orderInfo 指令信息
     */
    public void order(OrderInfo orderInfo){

        for (Map.Entry<String, Object> entry : orderInfo.getMsg().entrySet()) {

            DeviceKey deviceKey = deviceKeyService.getOne(
                    new QueryWrapper<DeviceKey>()
                            .eq("product_id",orderInfo.getProductId())
                            .eq("data_key",entry.getKey())
            );

            if (deviceKey != null) {
                IType type = parse(deviceKey);
                if (type != null) {
                    entry.setValue(type.checkout(entry.getValue()));
                }
            }
        }

        deliver.deliver(orderInfo);
    }


    /**
     * 发送升级指令
     */
    public void upgradeOrder(){

    }

}
