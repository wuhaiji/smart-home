package com.ecoeler.action.alexa.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecoeler.action.alexa.AlexaResponse;
import com.ecoeler.action.alexa.Capability;
import com.ecoeler.action.alexa.Endpoint;
import com.ecoeler.action.alexa.Property;
import com.ecoeler.action.alexa.service.AlexaService;
import com.ecoeler.action.alexa.service.OauthService;
import com.ecoeler.app.bean.v1.DeviceInfo;
import com.ecoeler.app.bean.v1.DeviceStateBean;
import com.ecoeler.app.bean.v1.DeviceVoiceBean;
import com.ecoeler.app.constant.v1.AppVoiceConstant;
import com.ecoeler.app.dto.v1.voice.DeviceKeyVoiceDto;
import com.ecoeler.app.dto.v1.voice.DeviceTypeVoiceDto;
import com.ecoeler.app.dto.v1.voice.DeviceVoiceDto;
import com.ecoeler.app.dto.v1.voice.UserVoiceDto;
import com.ecoeler.app.entity.AlexaReportTime;
import com.ecoeler.app.entity.Device;
import com.ecoeler.app.entity.DeviceKey;
import com.ecoeler.app.entity.DeviceType;
import com.ecoeler.app.service.AppVoiceActionService;
import com.ecoeler.app.service.IAlexaReportTimeService;
import com.ecoeler.core.DeviceEvent;
import com.ecoeler.core.msg.OrderInfo;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.util.SpringUtils;
import com.ecoeler.utils.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author whj
 * @createTime 2020-02-19 12:26
 * @description
 **/
@Service
@Slf4j
public class AlexaServiceImpl implements AlexaService {


    @Value("${home.app.apiJobUrl}")
    private String apiJobUrl;

    @Autowired
    private AppVoiceActionService appVoiceActionService;

    @Autowired
    private IAlexaReportTimeService alexaReportTimeService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OauthService oauthService;


    @Override
    public AlexaResponse Discovery(JSONObject params) {

        if (params == null)
            throw new ServiceException(" params map can not be empty");

        Long userId = Long.valueOf((String) params.get(AppVoiceConstant.DTO_KEY_USER_ID));

        //查询用户所有设备
        UserVoiceDto userVoiceDto = UserVoiceDto.of().setUserId(userId);
        List<DeviceVoiceBean> deviceVoiceBeans = appVoiceActionService.getDeviceVoiceBeans(userVoiceDto);
        log.info("deviceVoiceBeans:" + JSON.toJSONString(deviceVoiceBeans));

        //取出deviceIds
        List<String> deviceIds = deviceVoiceBeans.parallelStream().map(DeviceVoiceBean::getId).collect(Collectors.toList());

        //查询所有设备的对应设备状态信息
        List<DeviceInfo> deviceInfos = appVoiceActionService.getDeviceStatesByIds(deviceIds);

        //转成map
        Map<String, DeviceInfo> deviceInfoMap = deviceInfos.parallelStream().collect(Collectors.toMap(DeviceInfo::getDeviceId, i -> i));

        //获取返回头信息
        JSONObject header = params.getJSONObject("directive").getJSONObject("header");
        String namespace = header.getString("namespace");
        String correlationToken = header.getString("correlationToken");

        AlexaResponse alexaResponse = new AlexaResponse(namespace, AppVoiceConstant.ALEXA_DISCOVER_RESPONSE, null, null, correlationToken);

        List<Endpoint> endpoints = new ArrayList<>();

        for (DeviceVoiceBean deviceVoiceBean : deviceVoiceBeans) {
            //准备设备能力集合
            //默认每个设备都有连接状态interface
            List<Capability> capabilities = new ArrayList<>();

            Capability healthyCapability = new Capability();

            healthyCapability.setInterface_("Alexa.EndpointHealth");

            healthyCapability.supportedAddStateName("connectivity");

            capabilities.add(healthyCapability);

            DeviceInfo deviceInfo = deviceInfoMap.get(deviceVoiceBean.getId());

            List<DeviceStateBean> deviceStateBeans = deviceInfo.getDeviceStateBeans();

            for (DeviceStateBean deviceStateBean : deviceStateBeans) {

                Capability capability = new Capability();
                capability.setInterface_(deviceStateBean.getAlexaInterface());
                capability.supportedAddStateName(deviceStateBean.getAlexaStateName());
                capabilities.add(capability);

            }


            Endpoint endpoint = new Endpoint();
            //端点ID
            endpoint.setEndpointId("" + deviceVoiceBean.getId());
            //制造名称
            endpoint.setManufacturerName(deviceVoiceBean.getDefaultNames());
            //描述
            endpoint.setDescription(deviceVoiceBean.getDescription());
            //名称,判断一下名称是否为空，如果为空使用nickname
            endpoint.setFriendlyName(deviceVoiceBean.getDeviceName());
            //显示类别
            List<String> displayCategories = new ArrayList<>();
            displayCategories.add(deviceVoiceBean.getAlexaDisplayName());
            endpoint.setDisplayCategories(displayCategories);
            //cookie
            endpoint.setCookie(new JSONObject());
            //能力集合
            endpoint.setCapabilities(capabilities);
            //添加
            endpoints.add(endpoint);
        }

        JSONObject payload = new JSONObject();
        payload.put("endpoints", endpoints);
        log.info("discover payload:" + payload.toJSONString());
        alexaResponse.SetPayload(payload.toJSONString());
        return alexaResponse;
    }


    @Override
    public AlexaResponse StateReport(JSONObject data) {
        //解析json请求信息
        JSONObject directive = data.getJSONObject("directive");
        JSONObject header = directive.getJSONObject("header");
        String correlationToken = header.getString("correlationToken");
        String name = header.getString("name");
        Long userId = data.getLong(AppVoiceConstant.DTO_KEY_USER_ID);
        String endpointId = directive.getJSONObject("endpoint").getString("endpointId");
        log.info("endpointId:" + endpointId);

        if (!"ReportState".equals(name)) {
            log.error("Not found StateReport");
            AlexaResponse alexaResponse = new AlexaResponse("Alexa.Authorization", "ErrorResponse");
            JSONObject payload = new JSONObject();
            payload.put("type", "ACCEPT_GRANT_FAILED");
            payload.put("message", "can't handle this stateReport");
            alexaResponse.SetPayload(payload.toJSONString());
            return alexaResponse;
        }
        //查询设备所有状态
        JSONObject context = getContext(endpointId, userId);

        //获取alexa_token,用于异步发送,
//        String token = oauthService.getToken(userId);

        //生成返回数据对象
        AlexaResponse alexaResponse = new AlexaResponse("Alexa", "StateReport",
                endpointId + "", "", correlationToken);
        alexaResponse.setContext(context.toJSONString());
        return alexaResponse;
    }

    private JSONObject getContext(String deviceId, Long userId) {

        JSONObject context = new JSONObject();
        JSONArray properties = new JSONArray();

        //获取设备状态
        DeviceInfo deviceInfo = appVoiceActionService.getDeviceStatesByDeviceId(deviceId);
        List<DeviceStateBean> deviceStateBeans = deviceInfo.getDeviceStateBeans();
        log.info("Device state List:" + JSON.toJSONString(deviceStateBeans));

        //查询上一次设备report时间
        AlexaReportTime alexaReportTime = alexaReportTimeService.getOne(
                Query
                        .of(AlexaReportTime.class)
                        .eq("device_id", deviceId)
                        .eq("user_id", userId)

        );

        //如果没有查到，说明是第一次report,需要保存一个report time
        if (alexaReportTime == null) {
            alexaReportTime = new AlexaReportTime();
            alexaReportTime.setDeviceId(deviceId);
            alexaReportTime.setQueryTime(LocalDateTime.now());
        }

        //组装数据

        //获取设备网络连接状态,网路状态单独set
        Boolean netState = deviceInfo.getOnline();
        String connectivity = "connectivity";
        String namespace = "Alexa.EndpointHealth";

        //这里根据网络状态set不同的value对象
        JSONObject netValue = new JSONObject();
        netValue.put("value", netState ? AppVoiceConstant.ALEXA_NET_STATE_ONLINE : AppVoiceConstant.ALEXA_NET_STATE_OFFLINE);

        this.setProperty(alexaReportTime.getQueryTime(), properties, netValue, connectivity, namespace, LocalDateTime.now());

        //其余状态set
        for (DeviceStateBean deviceStateBean : deviceStateBeans) {

            String value = deviceStateBean.getValue();

            //因为开关类的状态值最终json数据需要的value值并不是0和1，而是ON/OFF所以这里转换一下
            if (AppVoiceConstant.ALEXA_STATE_NAME_POWER_STATE.equals(deviceStateBean.getAlexaStateName())) {

                value = StrUtil.equals(value, AppVoiceConstant.YUNTUN_POWER_STATE_ON) ?
                        AppVoiceConstant.ALEXA_POWER_STATE_ON : AppVoiceConstant.ALEXA_POWER_STATE_OFF;

            }

            this.setProperty(
                    alexaReportTime.getQueryTime(),
                    properties,
                    value,
                    deviceStateBean.getAlexaStateName(),
                    deviceStateBean.getAlexaInterface(),
                    deviceStateBean.getCreateTime()
            );

            log.info(properties.toJSONString());

        }

        log.info("context" + context.toJSONString());

        context.put("properties", properties);

        //查询完了重新更新或者插入report time
        alexaReportTime.setQueryTime(LocalDateTime.now());
        alexaReportTimeService.saveOrUpdate(alexaReportTime);

        return context;
    }


    /**
     * 设置 状态对象
     *
     * @param queryTime  report时间
     * @param properties 状态对象集合
     * @param value      状态值
     * @param name       状态名称
     * @param namespace  状态值命名空间
     */
    private void setProperty(LocalDateTime queryTime, JSONArray properties, Object value, String name, String namespace,
                             LocalDateTime time) {
        Property property = new Property();
        property.setName(name);
        property.setNamespace(namespace);
        //设置时间
        property.setTimeOfSample(time.atZone(ZoneOffset.UTC).toString());
        //获取上一次report到现在间隔的毫秒数
        property.setUncertaintyInMilliseconds(System.currentTimeMillis() - queryTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        property.setValue(value);
        properties.add(property);
    }


    /**
     * 传入Data类型日期，返回字符串类型时间（ISO8601标准时间）
     *
     * @param date 日期对象
     * @return 日期字符串
     */
    public static String getISO8601Timestamp(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(tz);
        return df.format(date);
    }


    @Override
    public AlexaResponse powerController(JSONObject data) {

        JSONObject directive = data.getJSONObject("directive");

        Long userId = data.getLong(AppVoiceConstant.DTO_KEY_USER_ID);

        JSONObject header = directive.getJSONObject("header");
        String name = header.getString("name");
        String correlationToken = header.getString("correlationToken");
        String endpointId = directive.getJSONObject("endpoint").getString("endpointId");

        JSONObject stateJson = new JSONObject();

        //组装执行命令json

        //查询开关的data key
        List<DeviceKey> deviceKeys = appVoiceActionService.getDeviceKeys(
                DeviceKeyVoiceDto.of()
                        .setAlexaStateName(AppVoiceConstant.ALEXA_STATE_NAME_POWER_STATE)
        );

        deviceKeys.parallelStream().forEach(deviceKey -> {
            if (AppVoiceConstant.ALEXA_VALUE_TURN_OFF.equals(name))
                stateJson.put(deviceKey.getDataKey(), AppVoiceConstant.YUNTUN_POWER_STATE_OFF);
            else if (AppVoiceConstant.ALEXA_VALUE_TURN_ON.equals(name))
                stateJson.put(deviceKey.getDataKey(), AppVoiceConstant.YUNTUN_POWER_STATE_ON);
        });

        return addJob(userId, correlationToken, endpointId, stateJson);
    }

    @Override
    public AlexaResponse brightnessController(JSONObject data) {

        Long userId = data.getLong(AppVoiceConstant.DTO_KEY_USER_ID);

        JSONObject directive = data.getJSONObject("directive");

        JSONObject header = directive.getJSONObject("header");
        String correlationToken = header.getString("correlationToken");
        String endpointId = directive.getJSONObject("endpoint").getString("endpointId");
        String brightness = directive.getJSONObject("payload").getString("brightness");

        JSONObject stateJson = new JSONObject();

        //查询灯光的data key
        List<DeviceKey> deviceKeys = appVoiceActionService.getDeviceKeys(
                DeviceKeyVoiceDto.of()
                        .setAlexaStateName(AppVoiceConstant.ALEXA_STATE_NAME_BRIGHTNESS)
        );
        deviceKeys.parallelStream().forEach(deviceKey -> {
            stateJson.put(deviceKey.getDataKey(), Integer.parseInt(brightness));
        });


        return addJob(userId, correlationToken, endpointId, stateJson);
    }

    /**
     * 执行命令
     *
     * @param userId           用户id
     * @param correlationToken correlationToken
     * @param deviceId         设备ID
     * @param json             json
     * @return AlexaResponse
     */
    private AlexaResponse addJob(Long userId, String correlationToken, String deviceId, JSONObject json) {

        AlexaResponse alexaResponse = new AlexaResponse("Alexa", "Response", deviceId, null, correlationToken);

        //发送设备执行命令请求
        try {

            Device device = appVoiceActionService.getDevice(DeviceVoiceDto.of().setDeviceId(deviceId));

            DeviceType deviceType = appVoiceActionService.getDeviceType(DeviceTypeVoiceDto.of().setProductId(device.getProductId()));

            DeviceEvent deviceEvent = SpringUtils.getBean(deviceType.getEventClass());
            deviceEvent.order(
                    OrderInfo
                            .of()
                            .setDeviceId(deviceId)
                            .setMsg(json)
                            .setProductId(deviceType.getProductId())
            );


            JSONObject context = getContext(deviceId, userId);

            alexaResponse.setContext(context.toJSONString());

            return alexaResponse;

        } catch (Exception e) {

            e.printStackTrace();
            JSONObject payload = new JSONObject();
            payload.put("type", "INTERNAL_ERROR");
            payload.put("message", "INTERNAL_ERROR");
            AlexaResponse errorResponse = new AlexaResponse("Alexa", "ErrorResponse", deviceId,
                    null, correlationToken);
            errorResponse.SetPayload(payload.toJSONString());
            return errorResponse;
        }
    }

}
