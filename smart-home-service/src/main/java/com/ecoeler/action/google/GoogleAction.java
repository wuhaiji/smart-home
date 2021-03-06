package com.ecoeler.action.google;

import com.alibaba.fastjson.JSONObject;
import com.ecoeler.app.entity.AppUser;
import com.ecoeler.app.entity.Device;
import com.ecoeler.app.entity.Family;
import com.ecoeler.app.entity.UserFamily;
import com.ecoeler.app.mapper.*;
import com.ecoeler.app.service.AppVoiceActionService;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.utils.EptUtil;
import com.ecoeler.utils.Query;
import com.google.actions.api.smarthome.*;
import com.google.home.graph.v1.HomeGraphApiServiceProto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import static com.ecoeler.app.constant.v1.AppVoiceConstant.DTO_KEY_USER_ID;
import static com.ecoeler.app.constant.v1.AppVoiceConstant.GOOGLE_LINK_STATUS_IS_YES;

@Component
@Slf4j
public class GoogleAction {

    @Autowired
    GoogleSmartHomeApp googleSmartHomeApp;

    @Autowired
    ThreadPoolExecutor executor;

    @Autowired
    AppVoiceActionService appVoiceActionService;

    @Autowired
    AppUserMapper appUserMapper;

    @Autowired
    UserFamilyMapper userFamilyMapper;

    @Autowired
    DeviceMapper deviceMapper;

    @Autowired
    FamilyMapper familyMapper;

    @Autowired
    DeviceSwitchMapper deviceSwitchMapper;

    /**
     * 主动向google申请同步
     *
     * @param userId 同步新增或删除设备所属的用户ID
     */
    public void requestSync(Long userId) {
        log.debug("userId:{}", userId);
        executor.execute(() -> {

            //查询用户
            AppUser user = appUserMapper.selectById(userId);
            if (user == null) {
                log.error("user info does not exist！");
                return;
            }

            //查询该用户所有的userFamily
            List<UserFamily> userFamilies = userFamilyMapper.selectList(
                    Query.of(UserFamily.class).eq("user_id", user.getId())
            );

            //查询中间表
            List<Long> familyIds = userFamilies.parallelStream()
                    .map(UserFamily::getFamilyId)
                    .collect(Collectors.toList());
            List<UserFamily> OtherUserFamilies = userFamilyMapper.selectList(
                    Query.of(UserFamily.class).in("family_id", familyIds)
            );

            //查询所有同家庭的用户
            List<Long> userIds = OtherUserFamilies.parallelStream()
                    .map(UserFamily::getAppUserId)
                    .collect(Collectors.toList());
            if (EptUtil.isEmpty(userIds)) return;
            List<AppUser> users = appUserMapper.selectList(
                    Query.of(AppUser.class).in("id", userIds)
            );

            //通知所有user的Google home
            for (AppUser appUser : users) {
                //判断用户是否取消连接
                Integer googleLinkStatus = appUser.getGoogleLinkStatus();
                if (googleLinkStatus.equals(0))
                    return;
                //发送请求
                HomeGraphApiServiceProto.RequestSyncDevicesResponse response = null;
                try {
                    response = googleSmartHomeApp.requestSync(String.valueOf(appUser.getId()));
                } catch (Exception e) {
                    log.error("request sync failed userId:{} ", appUser.getId(), e);
                }
                assert response != null;
                log.info("Eng Google request sync:" + response.toString() + ",userId:" + appUser.getId());
            }

        });
    }


    // /**
    //  * 主动向google报告状态
    //  *
    //  * @param keyMsgs 设备状态List
    //  */
    // public void reportState(List<KeyMsg> keyMsgs) {
    //     executor.execute(() -> {
    //
    //         //查询所有的对应deviceKey
    //         List<String> datakeys = keyMsgs.parallelStream().map(KeyMsg::getDataKey).collect(Collectors.toList());
    //         List<DeviceKey> deviceKeys = appVoiceActionService
    //                 .getDeviceKeyList(DeviceKeyVoiceDto.of().setDataKeys(datakeys));
    //         Map<String, DeviceKey> deviceKeyMap = deviceKeys.parallelStream()
    //                 .collect(Collectors.toMap(DeviceKey::getDataKey, i -> i));
    //
    //         //先按设备id分组
    //         Map<String, List<KeyMsg>> listMap = keyMsgs.parallelStream()
    //                 .collect(Collectors.groupingBy(KeyMsg::getDeviceId));
    //
    //         for (Map.Entry<String, List<KeyMsg>> entry : listMap.entrySet()) {
    //
    //             String deviceId = entry.getKey();
    //             List<KeyMsg> keyMsgList = entry.getValue();
    //             JSONObject states = new JSONObject();
    //             keyMsgList.parallelStream()
    //                     .forEach(keyMsg -> {
    //                         //获取deviceKey，主要是获取alexa state name
    //                         DeviceKey deviceKey = deviceKeyMap.get(keyMsg.getDataKey());
    //                         if (deviceKey != null) {
    //                             //value
    //                             Object dataValue = keyMsg.getDataValue();
    //
    //                             String alexaStateName = deviceKey.getAlexaStateName();
    //
    //                             //如果是开关类型的value需要转换成ON/OFF
    //                             if (GOOGLE_POWER_STATE_KEY.equals(deviceKey.getAlexaStateName())) {
    //                                 String dataValueStr = (String) dataValue;
    //
    //                                 //获取开关类型的value对应的云屯枚举值
    //                                 DeviceSwitch deviceSwitch = deviceSwitchMapper.selectOne(
    //                                         Query.of(DeviceSwitch.class)
    //                                                 .eq("product_id", deviceKey.getProductId())
    //                                                 .eq("data_key", deviceKey.getDataKey())
    //                                 );
    //                                 //判断是ON还是OFF
    //                                 dataValueStr = dataValueStr.equals(deviceSwitch.getDeviceOn())
    //                                         ? ALEXA_POWER_STATE_ON : ALEXA_POWER_STATE_OFF;
    //                                 states.put(alexaStateName, dataValueStr);
    //                             } else {
    //                                 states.put(alexaStateName, dataValue);
    //                             }
    //                         }
    //                     });
    //
    //
    //             //查询设备属于的家庭
    //             Device device = deviceMapper.selectOne(
    //                     Query.of(Device.class).eq("device_id", deviceId)
    //             );
    //             if (device == null) {
    //                 log.error("device does not exist");
    //                 continue;
    //             }
    //             //查询设备属于的家庭
    //             Family family = familyMapper.selectById(device.getDeviceId());
    //             if (family == null) {
    //                 log.error("family does not exist");
    //                 continue;
    //             }
    //             //查询家庭中的用户
    //             List<UserFamily> userFamilies = userFamilyMapper.selectList(
    //                     Query.of(UserFamily.class).eq("family_id", family.getId())
    //             );
    //             if (EptUtil.isEmpty(userFamilies)) {
    //                 log.error("userFamilies does not exist");
    //                 continue;
    //             }
    //             List<Long> appUserIds = userFamilies.parallelStream().map(UserFamily::getAppUserId).collect(Collectors.toList());
    //             List<AppUser> users = appUserMapper.selectList(
    //                     Query.of(AppUser.class).in(EptUtil.isNotEmpty(appUserIds), "id", appUserIds)
    //             );
    //
    //
    //             //上报状态
    //             for (AppUser user : users) {
    //                 //判断用户是否取消google语音控制
    //                 if (user.getGoogleLinkStatus().equals(1))
    //                     GoogleReportState.makeRequest(googleSmartHomeApp, String.valueOf(user.getId()), deviceId, states);
    //             }
    //
    //         }
    //
    //     });
    // }

    /**
     * 主动向google报告状态
     *
     * @param deviceId 设备id
     */
    public void reportState(String deviceId) {
        executor.execute(() -> {
            log.info("deviceId:{}", deviceId);
            //-----------------------------------------------查询与该设备有关的用户----------------------------------------
            Device device = deviceMapper.selectOne(
                    Query.of(Device.class).eq("device_id", deviceId)
            );
            if (device == null) {
                log.error("device does not exist");
                return;
            }
            //查询设备属于的家庭
            Family family = familyMapper.selectById(device.getDeviceId());
            if (family == null) {
                log.error("family does not exist");
                return;
            }
            //查询家庭中的用户
            List<UserFamily> userFamilies = userFamilyMapper.selectList(
                    Query.of(UserFamily.class).eq("family_id", family.getId())
            );
            if (EptUtil.isEmpty(userFamilies)) {
                log.error("userFamilies does not exist");
                return;
            }
            List<Long> appUserIds = userFamilies.parallelStream().map(UserFamily::getAppUserId).collect(Collectors.toList());
            List<AppUser> users = appUserMapper.selectList(
                    Query.of(AppUser.class).in(EptUtil.isNotEmpty(appUserIds), "id", appUserIds)
            );

            //-----------------------------------------------查询设备状态----------------------------------------------
            Map<String, Object> states = appVoiceActionService.getDeviceGoogleStatesMap(deviceId);

            //-----------------------------------------------上报状态----------------------------------------------
            for (AppUser user : users) {
                //判断用户是否取消google语音控制
                if (user.getGoogleLinkStatus().equals(1))
                    GoogleReportState.makeRequest(googleSmartHomeApp, String.valueOf(user.getId()), deviceId, states);
            }

        });
    }

    /**
     * 处理google语音请求
     *
     * @param data 请求参数map {AppVoice,googleJson,accessToken}
     * @return
     */
    public String action(JSONObject data) {
        log.info("JSON smartHomeRequest：{}", data.toString());
        //修改用户的google_link为1,表示用户已经连接googleHome
        updateUserGoogleLinkStatus(data.getInteger(DTO_KEY_USER_ID), GOOGLE_LINK_STATUS_IS_YES);
        //转成google smartHome 请求参数对象
        SmartHomeRequest smartHomeRequest = SmartHomeRequest.Companion.create(data.toJSONString());
        //获取请求的数据
        SmartHomeRequest.RequestInputs[] inputs1 = smartHomeRequest.getInputs();
        //google规定的requestId
        String requestId = smartHomeRequest.getRequestId();

        //用于接收响应的数据
        JSONObject responseJson = new JSONObject();
        try {
            //命名空间
            String intent = inputs1[0].intent;
            log.info("begin================================================================================");
            log.info("intend:" + intent);
            switch (intent) {
                case "action.devices.SYNC":
                    SyncResponse syncResponse = googleSmartHomeApp.onSync((SyncRequest) smartHomeRequest, data);
                    responseJson = JSONObject.parseObject(syncResponse.build().toString());
                    break;

                case "action.devices.QUERY":
                    QueryResponse queryResponse = googleSmartHomeApp.onQuery((QueryRequest) smartHomeRequest, data);
                    responseJson = JSONObject.parseObject(queryResponse.build().toString());
                    break;

                case "action.devices.EXECUTE":
                    ExecuteResponse executeResponse = googleSmartHomeApp.onExecute((ExecuteRequest) smartHomeRequest, data);
                    responseJson = JSONObject.parseObject(executeResponse.build().toString());
                    break;

                case "action.devices.DISCONNECT":
                    googleSmartHomeApp.onDisconnect((DisconnectRequest) smartHomeRequest, data);
                    break;

                default:
                    String errorCode = "notSupportAction";
                    responseJson.put("errorCode", errorCode);
                    break;
            }


        } catch (ServiceException e) {
            e.printStackTrace();
            JSONObject payload = new JSONObject();
            payload.put("errorCode", e.getMsg());
            payload.put("status", "ERROR");
            responseJson.put("payload", payload);
        } catch (Exception e) {
            e.printStackTrace();
            String errorCode = "service error";
            responseJson.put("errorCode", errorCode);
        }

        //google请求Id
        responseJson.put("requestId", requestId);
        log.info("responseJson:" + responseJson.toJSONString());

        log.info("begin====================================================================================");

        return responseJson.toJSONString();
    }

    //修改用户google连接状态为1
    private void updateUserGoogleLinkStatus(Integer userId, Integer googleLinkStatus) {
        try {
            AppUser appUser = appUserMapper.selectById(userId);
            if (appUser == null) {
                log.error("User does not exist");
                return;
            }
            appUser.setGoogleLinkStatus(googleLinkStatus);
            appUserMapper.updateById(appUser);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Failed to modify user googleLinkStatus!");
        }
    }


}
