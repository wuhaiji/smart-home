package com.ecoeler.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecoeler.app.constant.v1.AppVoiceConstant;
import com.ecoeler.app.dto.v1.UserDto;
import com.ecoeler.app.service.IAppUserService;
import com.ecoeler.exception.CustomException;
import com.google.actions.api.smarthome.*;
import com.google.home.graph.v1.DeviceProto;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MySmartHomeApp extends SmartHomeApp {

    @Autowired
    private IAppUserService appUserService;

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
        return null;
    }

    @NotNull
    @Override
    public SyncResponse onSync(@NotNull SyncRequest syncRequest, @Nullable Map<?, ?> map) {
        // if(map==null) throw new CustomException("google voice params map can not be empty");
        // Long userId = Long.valueOf((String) map.get("userId"));
        // UserDto userDto =  UserDto.of().setUserId(userId);
        // List<UserDevice> devices = appUserService.getUserDeviceList(userDto);
        // log.info("devices:" + JSON.toJSONString(devices));
        // SyncResponse response = new SyncResponse();
        // response.setRequestId(syncRequest.requestId);
        // response.setPayload(new SyncResponse.Payload());
        // response.payload.agentUserId = "" + userId;
        // int numOfDevices = devices.size();
        // response.payload.devices = new SyncResponse.Payload.Device[numOfDevices];
        //
        // for (int i = 0; i < numOfDevices; i++) {
        //     UserDevice device = devices.get(i);
        //     List<String> defaultNames = new ArrayList<>();
        //     defaultNames.add(device.getDefaultNames());
        //     List<String> nicknames = new ArrayList<>();
        //     defaultNames.add(device.getNicknames());
        //     String[] split = device.getTrait().split(",");
        //     List<String> traits = new ArrayList<>(split.length);
        //     Collections.addAll(traits, split);
        //     log.info(traits.toString());
        //
        //     SyncResponse.Payload.Device.Builder deviceBuilder =
        //             new SyncResponse.Payload.Device.Builder()
        //                     .setId("" + device.getId())
        //                     .setType(device.getType())
        //                     .setTraits(traits)
        //                     .setName(DeviceProto.DeviceNames.newBuilder()
        //                             .addAllDefaultNames(defaultNames)
        //                             .setName(device.getName())
        //                             .addAllNicknames(nicknames)
        //                             .build())
        //                     .setWillReportState(true)
        //                     .setRoomHint("" + (device.getRoomId().equals(0) ? null : device.getRoomId()));
        //     response.payload.devices[i] = deviceBuilder.build();
        // }
        return null;
    }
}
