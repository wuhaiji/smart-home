package com.ecoeler.action;

import cn.hutool.core.util.StrUtil;
import com.ecoeler.app.bean.v1.DeviceVoiceBean;
import com.ecoeler.app.dto.v1.voice.DeviceVoiceDto;
import com.ecoeler.app.dto.v1.voice.UserVoiceDto;
import com.ecoeler.app.service.AppVoiceActionService;
import com.ecoeler.exception.ServiceException;
import com.google.actions.api.smarthome.*;
import com.google.home.graph.v1.DeviceProto;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class MySmartHomeApp extends SmartHomeApp {

    @Autowired
    private AppVoiceActionService appVoiceActionService;

    @Override
    public void onDisconnect(@NotNull DisconnectRequest disconnectRequest, @Nullable Map<?, ?> map) {

    }

    @NotNull
    @Override
    public ExecuteResponse onExecute(@NotNull ExecuteRequest executeRequest, @Nullable Map<?, ?> map) {
        return null;
    }

    @NotNull
    @Override
    public QueryResponse onQuery(@NotNull QueryRequest queryRequest, @Nullable Map<?, ?> map) {

        if (map == null)
            throw new ServiceException("params map can not be empty");

        Long userId = Long.valueOf((String) map.get("userId"));

        QueryRequest.Inputs.Payload.Device[] devices = ((QueryRequest.Inputs) queryRequest.getInputs()[0]).payload.devices;
        Map<String, Map<String, Object>> deviceStates = new HashMap<>();
        QueryResponse res = new QueryResponse();
        res.setRequestId(queryRequest.requestId);
        res.setPayload(new QueryResponse.Payload());
        for (QueryRequest.Inputs.Payload.Device device : devices) {
            try {
                DeviceVoiceDto userVoiceDto = DeviceVoiceDto.of().setDeviceId(Long.valueOf(device.getId()));
                Map<String, Object> states = appVoiceActionService.getUserDeviceStates(userVoiceDto);
                if (states.containsKey("on")) {
                    String on = (String) states.get("on");
                    states.put("on", !on.equals("0"));
                }
                deviceStates.put(device.id, states);
                // ReportState.makeRequest(this, userId, device.id, states);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("QUERY FAILED");
                Map<String, Object> failedDevice = new HashMap<>();
                failedDevice.put("errorCode", e.getMessage());
                deviceStates.put(device.id, failedDevice);
            }
        }
        res.payload.setDevices(deviceStates);
        return res;
    }

    @NotNull
    @Override
    public SyncResponse onSync(@NotNull SyncRequest syncRequest, @Nullable Map<?, ?> map) {

        if (map == null)
            throw new ServiceException(" params map can not be empty");

        Long userId = Long.valueOf((String) map.get("userId"));

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


}
