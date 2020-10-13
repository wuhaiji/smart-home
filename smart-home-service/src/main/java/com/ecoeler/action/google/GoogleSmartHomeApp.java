package com.ecoeler.action.google;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecoeler.app.bean.v1.DeviceInfo;
import com.ecoeler.app.bean.v1.DeviceStateBean;
import com.ecoeler.app.bean.v1.DeviceVoiceBean;
import com.ecoeler.app.constant.v1.AppVoiceConstant;
import com.ecoeler.app.dto.v1.voice.DeviceTypeVoiceDto;
import com.ecoeler.app.dto.v1.voice.DeviceVoiceDto;
import com.ecoeler.app.dto.v1.voice.UserVoiceDto;
import com.ecoeler.app.entity.AppUser;
import com.ecoeler.app.entity.Device;
import com.ecoeler.app.entity.DeviceSwitch;
import com.ecoeler.app.entity.DeviceType;
import com.ecoeler.app.mapper.AppUserMapper;
import com.ecoeler.app.msg.OrderInfo;
import com.ecoeler.app.service.AppVoiceActionService;
import com.ecoeler.app.service.IDeviceService;
import com.ecoeler.app.service.IDeviceTypeService;
import com.ecoeler.core.DeviceEvent;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.feign.Oauth2ClientService;
import com.ecoeler.model.response.Result;
import com.ecoeler.util.SpringUtils;
import com.google.actions.api.smarthome.*;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.home.graph.v1.DeviceProto;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static com.ecoeler.app.constant.v1.AppVoiceConstant.*;

@Service
@Slf4j
public class GoogleSmartHomeApp extends SmartHomeApp implements InitializingBean {


    @Autowired
    AppVoiceActionService appVoiceActionService;

    @Autowired
    IDeviceTypeService iDeviceTypeService;

    @Autowired
    IDeviceService iDeviceService;

    @Autowired
    Oauth2ClientService oauth2ClientService;

    @Autowired
    AppUserMapper appUserMapper;

    /**
     * bean初始化后，注入googleApi密匙
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        try {
            InputStream resourceAsStream = GoogleSmartHomeApp.class.getClassLoader().getResourceAsStream("yoti-mart.json");
            if (resourceAsStream != null) {
                GoogleCredentials credentials = GoogleCredentials.fromStream(resourceAsStream);
                this.setCredentials(credentials);
            } else {
                log.error("couldn't load google credentials");
            }
        } catch (Exception e) {
            log.error("couldn't load google credentials");
        }
    }

    @NotNull
    @Override
    public SyncResponse onSync(@NotNull SyncRequest syncRequest, @Nullable Map<?, ?> map) {

        if (map == null)
            throw new ServiceException(" params map can not be empty");

        Long userId = Long.valueOf((String) map.get(AppVoiceConstant.DTO_KEY_USER_ID));

        List<DeviceVoiceBean> deviceVoiceBeans = appVoiceActionService.getDeviceVoiceBeans(
                UserVoiceDto.of().setUserId(userId)
        );

        SyncResponse response = new SyncResponse();
        response.setRequestId(syncRequest.requestId);
        response.setPayload(new SyncResponse.Payload());
        response.payload.agentUserId = String.valueOf(userId);
        response.payload.devices = new SyncResponse.Payload.Device[deviceVoiceBeans.size()];

        for (int i = 0; i < deviceVoiceBeans.size(); i++) {

            DeviceVoiceBean device = deviceVoiceBeans.get(i);

            List<String> defaultNames = new ArrayList<>();
            defaultNames.add(device.getDefaultNames());

            List<String> nicknames = new ArrayList<>();
            defaultNames.add(device.getNicknames());

            List<String> traits = StrUtil.splitTrim(device.getGoogleTraitNames(), ",");

            DeviceProto.DeviceNames deviceNames = DeviceProto.DeviceNames.newBuilder()
                    .addAllDefaultNames(defaultNames)
                    .setName(device.getDeviceName())
                    .addAllNicknames(nicknames)
                    .build();

            SyncResponse.Payload.Device.Builder deviceBuilder = new SyncResponse.Payload.Device.Builder()
                    .setId(String.valueOf(device.getId()))
                    .setType(device.getGoogleTypeName())
                    .setTraits(traits)
                    .setName(deviceNames)
                    .setWillReportState(true)
                    .setRoomHint(device.getRoomName());

            response.payload.devices[i] = deviceBuilder.build();
        }

        return response;
    }

    @NotNull
    @Override
    public QueryResponse onQuery(@NotNull QueryRequest queryRequest, @Nullable Map<?, ?> map) {

        if (map == null)
            throw new ServiceException("params map can not be empty");

        // Long userId = Long.valueOf((String) map.get(DTO_KEY_USER_ID));

        QueryRequest.Inputs.Payload.Device[] devices = ((QueryRequest.Inputs) queryRequest.getInputs()[0]).payload.devices;
        Map<String, Map<String, Object>> deviceStates = new HashMap<>();
        QueryResponse res = new QueryResponse();
        res.setRequestId(queryRequest.requestId);
        res.setPayload(new QueryResponse.Payload());
        for (QueryRequest.Inputs.Payload.Device device : devices) {
            try {

                Map<String, Object> states = this.getDeviceStatesMap(device.getId());

                deviceStates.put(device.id, states);

            } catch (ServiceException e) {
                e.printStackTrace();
                log.error("QUERY FAILED");
                Map<String, Object> failedDevice = new HashMap<>();
                failedDevice.put("errorCode", e.getMsg());
                deviceStates.put(device.id, failedDevice);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("QUERY FAILED");
                Map<String, Object> failedDevice = new HashMap<>();
                failedDevice.put("errorCode", "service exception");
                deviceStates.put(device.id, failedDevice);
            }
        }
        res.payload.setDevices(deviceStates);
        return res;
    }

    @Override
    public void onDisconnect(@NotNull DisconnectRequest disconnectRequest, @Nullable Map<?, ?> map) {
        assert map != null;
        String access_token = (String) map.get(DTO_KEY_AUTHORIZATION);
        Result result = oauth2ClientService.logout(access_token);

        //更新appUser的googleLinkStatus
        Long userId = Long.valueOf((String) map.get(DTO_KEY_USER_ID));

        appUserMapper.updateById(
                new AppUser()
                        .setGoogleLinkStatus(GOOGLE_LINK_STATUS_IS_NOT)
                        .setId(userId)
        );

        log.info("退出登录返回数据：{}", JSON.toJSON(result));
    }

    @NotNull
    @Override
    public ExecuteResponse onExecute(@NotNull ExecuteRequest executeRequest, @Nullable Map<?, ?> map) {
        if (map == null)
            throw new ServiceException("params map can not be empty");

        Long userId = Long.valueOf((String) map.get(DTO_KEY_USER_ID));

        ExecuteResponse res = new ExecuteResponse();

        List<ExecuteResponse.Payload.Commands> commandsResponse = new ArrayList<>();

        Map<String, Object> states = new HashMap<>();

        ExecuteRequest.Inputs.Payload.Commands[] commands = ((ExecuteRequest.Inputs) executeRequest.inputs[0]).payload.commands;

        List<String> successfulDevices = ListUtil.list(false);

        for (ExecuteRequest.Inputs.Payload.Commands command : commands) {

            for (ExecuteRequest.Inputs.Payload.Commands.Devices device : command.devices) {
                try {

                    states = execute(device.id, command.execution);

                    successfulDevices.add(device.id);

                    //改变了设备状态，需要发起状态报告
                    GoogleReportState.makeRequest(this, String.valueOf(userId), device.id, states);

                } catch (Exception e) {

                    e.printStackTrace();

                    ExecuteResponse.Payload.Commands failedDevice = new ExecuteResponse.Payload.Commands();

                    failedDevice.ids = new String[]{device.id};

                    failedDevice.status = GOOGLE_FAILED_COMMANDS_RESPONSE;

                    failedDevice.setErrorCode(e.getMessage());

                    commandsResponse.add(failedDevice);
                }

            }
        }

        ExecuteResponse.Payload.Commands successfulCommands = new ExecuteResponse.Payload.Commands();
        successfulCommands.status = GOOGLE_SUCCESS_COMMANDS_RESPONSE;
        successfulCommands.setStates(states);
        successfulCommands.ids = successfulDevices.toArray(new String[]{});
        commandsResponse.add(successfulCommands);

        res.requestId = executeRequest.requestId;
        ExecuteResponse.Payload payload = new ExecuteResponse.Payload(
                commandsResponse.toArray(new ExecuteResponse.Payload.Commands[]{})
        );
        res.setPayload(payload);
        return res;

    }

    private Map<String, Object> execute(String deviceId, ExecuteRequest.Inputs.Payload.Commands.Execution[] executions) {

        Map<String, Object> states = this.getDeviceStatesMap(deviceId);
        Object onlineValue = states.get(GOOGLE_NET_STATE_KEY);
        if (onlineValue == null || (Boolean) onlineValue) {
            throw new ServiceException("deviceOffline");
        }

        //下发命令的json内容
        JSONObject jobJson = new JSONObject();

        for (int j = 0; j < executions.length; j++) {

            ExecuteRequest.Inputs.Payload.Commands.Execution execution = executions[j];

            switch (execution.command) {

                case GOOGLE_COMMAND_BRIGHTNESS:

                    Object brightness = Objects.requireNonNull(execution.getParams()).get(GOOGLE_BRIGHTNESS_KEY);
                    jobJson.put(YUNTUN_BRIGHTNESS_DATA_KEY, brightness);
                    states.put(GOOGLE_BRIGHTNESS_KEY, brightness);
                    break;

                case GOOGLE_COMMAND_ON_OFF:

                    boolean onOffValue = (boolean) Objects.requireNonNull(execution.getParams()).get(GOOGLE_POWER_STATE_KEY);

                    //查询该设备的所有开关量
                    List<DeviceSwitch> deviceSwitches = appVoiceActionService.getDeviceSwitchListByDeviceId(deviceId);

                    //只能控制一路开关，多路就统一开关
                    deviceSwitches.parallelStream().forEach(deviceSwitch ->
                            jobJson.put(deviceSwitch.getDataKey(), onOffValue ? deviceSwitch.getDeviceOn() : deviceSwitch.getDeviceOff())
                    );
                    states.put(GOOGLE_POWER_STATE_KEY, onOffValue);
                    break;
            }
        }

        Device device = appVoiceActionService.getDevice(DeviceVoiceDto.of().setDeviceId(deviceId));

        DeviceType deviceType = appVoiceActionService.getDeviceType(DeviceTypeVoiceDto.of().setProductId(device.getProductId()));

        //查询设备对应的设备类型
        String eventClass = deviceType.getEventClass();

        DeviceEvent deviceEvent = SpringUtils.getBean(eventClass);
        deviceEvent.order(
                OrderInfo.of()
                        .setDeviceId(deviceId)
                        .setMsg(jobJson)
                        .setProductId(deviceType.getProductId())
        );
        log.info("states:" + states);
        return states;
    }


    /**
     * 根据设备ID获取设备的各项语音相关的key状态 map
     *
     * @param deviceId
     * @return
     */
    @NotNull
    public Map<String, Object> getDeviceStatesMap(String deviceId) {

        DeviceInfo deviceInfo = appVoiceActionService.getDeviceStatesByDeviceId(deviceId);

        List<DeviceStateBean> deviceStateBeans = deviceInfo.getDeviceStateBeans();

        Map<String, List<DeviceStateBean>> listMap = deviceStateBeans.parallelStream().collect(Collectors.groupingBy(DeviceStateBean::getGoogleStateName));

        Map<String, Object> states = new HashMap<>();

        for (Map.Entry<String, List<DeviceStateBean>> entry : listMap.entrySet()) {

            List<DeviceStateBean> list = entry.getValue();

            if (!entry.getKey().equals(GOOGLE_POWER_STATE_KEY)) {
                states.put(entry.getKey(), list.get(0).getValue());
                continue;
            }

            //默认设备是关闭的
            boolean flag = false;
            for (DeviceStateBean deviceStateBean : list) {
                //因为多路设备不止一个开关量
                if (deviceStateBean.getValue().equals(POWER_STATE_ON)) {
                    //只要一个开关是开的就认为该设备是开着的
                    flag = true;
                    break;
                }
            }
            if (flag) {
                states.put(entry.getKey(), POWER_STATE_ON);
            } else {
                states.put(entry.getKey(), POWER_STATE_OFF);
            }

        }

        states.put(GOOGLE_NET_STATE_KEY, deviceInfo.getOnline());

        //开关量1/0转换成google需要的boolean
        if (states.containsKey(GOOGLE_POWER_STATE_KEY)) {
            states.put(GOOGLE_POWER_STATE_KEY, Boolean.valueOf((String) states.get(GOOGLE_POWER_STATE_KEY)));
        }

        return states;
    }


}
