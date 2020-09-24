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
import com.ecoeler.app.entity.Device;
import com.ecoeler.app.entity.DeviceType;
import com.ecoeler.app.service.AppVoiceActionService;
import com.ecoeler.app.service.IDeviceService;
import com.ecoeler.app.service.IDeviceTypeService;
import com.ecoeler.core.DeviceEvent;
import com.ecoeler.app.msg.OrderInfo;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.feign.Oauth2ClientService;
import com.ecoeler.model.response.Result;
import com.ecoeler.util.SpringUtils;
import com.google.actions.api.smarthome.*;
import com.google.home.graph.v1.DeviceProto;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GoogleSmartHomeApp extends SmartHomeApp {


    @Autowired
    AppVoiceActionService appVoiceActionService;

    @Autowired
    IDeviceTypeService iDeviceTypeService;

    @Autowired
    IDeviceService iDeviceService;

    @Autowired
    Oauth2ClientService oauth2ClientService;

    @NotNull
    @Override
    public SyncResponse onSync(@NotNull SyncRequest syncRequest, @Nullable Map<?, ?> map) {

        if (map == null)
            throw new ServiceException(" params map can not be empty");

        Long userId = Long.valueOf((String) map.get(AppVoiceConstant.DTO_KEY_USER_ID));

        UserVoiceDto userVoiceDto = UserVoiceDto.of().setUserId(userId);
        List<DeviceVoiceBean> deviceVoiceBeans = appVoiceActionService.getDeviceVoiceBeans(userVoiceDto);

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

        Long userId = Long.valueOf((String) map.get(AppVoiceConstant.DTO_KEY_USER_ID));

        QueryRequest.Inputs.Payload.Device[] devices = ((QueryRequest.Inputs) queryRequest.getInputs()[0]).payload.devices;
        Map<String, Map<String, Object>> deviceStates = new HashMap<>();
        QueryResponse res = new QueryResponse();
        res.setRequestId(queryRequest.requestId);
        res.setPayload(new QueryResponse.Payload());
        for (QueryRequest.Inputs.Payload.Device device : devices) {
            try {

                Map<String, Object> states = this.getDeviceStatesMap(device.getId());

                deviceStates.put(device.id, states);

                //ReportState
                GoogleReportState.makeRequest(this, String.valueOf(userId), device.id, states);

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


    @NotNull
    @Override
    public ExecuteResponse onExecute(@NotNull ExecuteRequest executeRequest, @Nullable Map<?, ?> map) {
        if (map == null)
            throw new ServiceException("params map can not be empty");

        Long userId = Long.valueOf((String) map.get(AppVoiceConstant.DTO_KEY_USER_ID));

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

                    GoogleReportState.makeRequest(this, String.valueOf(userId), device.id, states);

                } catch (Exception e) {

                    e.printStackTrace();

                    ExecuteResponse.Payload.Commands failedDevice = new ExecuteResponse.Payload.Commands();

                    failedDevice.ids = new String[]{device.id};

                    failedDevice.status = AppVoiceConstant.GOOGLE_FAILED_COMMANDS_RESPONSE;

                    failedDevice.setErrorCode(e.getMessage());

                    commandsResponse.add(failedDevice);
                }

            }
        }

        ExecuteResponse.Payload.Commands successfulCommands = new ExecuteResponse.Payload.Commands();
        successfulCommands.status = AppVoiceConstant.GOOGLE_SUCCESS_COMMANDS_RESPONSE;
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

        if (!(Boolean) states.get(AppVoiceConstant.GOOGLE_NET_STATE_KEY))
            throw new ServiceException();

        //下发命令的json内容
        JSONObject jobJson = new JSONObject();

        for (int j = 0; j < executions.length; j++) {

            ExecuteRequest.Inputs.Payload.Commands.Execution execution = executions[j];

            switch (execution.command) {

                case AppVoiceConstant.GOOGLE_COMMAND_BRIGHTNESS:

                    Object brightness = Objects.requireNonNull(execution.getParams()).get(AppVoiceConstant.GOOGLE_BRIGHTNESS_KEY);
                    jobJson.put(AppVoiceConstant.YUNTUN_BRIGHTNESS_DATA_KEY, brightness);
                    states.put(AppVoiceConstant.GOOGLE_BRIGHTNESS_KEY, brightness);
                    break;

                case AppVoiceConstant.GOOGLE_COMMAND_ONOFF:

                    boolean onOffValue = (boolean) Objects.requireNonNull(execution.getParams()).get(AppVoiceConstant.GOOGLE_ON_OFF_KEY);

                    DeviceInfo deviceStates = appVoiceActionService.getDeviceStatesByDeviceId(deviceId);

                    List<DeviceStateBean> deviceStateBeans = deviceStates.getDeviceStateBeans();
                    log.info("设备状态信息集合：{}", JSON.toJSONString(deviceStateBeans));

                    //这里因为google "on" 的key对应的指令device key有多个(多路开关)，我只能查出所有on对应的data key执行统一命令
                    deviceStateBeans.parallelStream()

                            .filter(i -> i.getGoogleStateName().equals(AppVoiceConstant.GOOGLE_ON_OFF_KEY))

                            .map(DeviceStateBean::getDataKey)

                            .collect(Collectors.toList())

                            .forEach(i -> jobJson.put(i, onOffValue ? AppVoiceConstant.YUNTUN_POWER_STATE_ON : AppVoiceConstant.YUNTUN_POWER_STATE_OFF));

                    states.put(AppVoiceConstant.GOOGLE_ON_OFF_KEY, onOffValue);

                    break;
            }
        }

        Device device = appVoiceActionService.getDevice(DeviceVoiceDto.of().setDeviceId(deviceId));

        DeviceType deviceType = appVoiceActionService.getDeviceType(DeviceTypeVoiceDto.of().setProductId(device.getProductId()));

        //查询设备对应的设备类型
        String eventClass = deviceType.getEventClass();

        DeviceEvent deviceEvent = SpringUtils.getBean(eventClass);
        deviceEvent.order(OrderInfo.of()
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
    private Map<String, Object> getDeviceStatesMap(String deviceId) {

        DeviceInfo deviceInfo = appVoiceActionService.getDeviceStatesByDeviceId(deviceId);

        List<DeviceStateBean> deviceStateBeans = deviceInfo.getDeviceStateBeans();

        Map<String, Object> states = deviceStateBeans.parallelStream()
                .collect(Collectors.toMap(
                        DeviceStateBean::getGoogleStateName,
                        DeviceStateBean::getValue
                        )
                );

        states.put(AppVoiceConstant.GOOGLE_NET_STATE_KEY, deviceInfo.getOnline());
        return states;
    }


    @Override
    public void onDisconnect(@NotNull DisconnectRequest disconnectRequest, @Nullable Map<?, ?> map) {
        assert map != null;
        String access_token = (String) map.get(AppVoiceConstant.DTO_KEY_AUTHORIZATION);
        Result result = oauth2ClientService.logout(access_token);
        log.info("退出登录返回数据：{}", JSON.toJSON(result));
    }

}
