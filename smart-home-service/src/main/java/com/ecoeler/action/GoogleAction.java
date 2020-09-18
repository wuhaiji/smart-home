package com.ecoeler.action;

import com.alibaba.fastjson.JSONObject;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.model.response.Result;
import com.google.actions.api.smarthome.*;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.home.graph.v1.HomeGraphApiServiceProto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;

@Component
@Slf4j
public class GoogleAction {

    @Autowired
    private GoogleSmartHomeApp googleSmartHomeApp;

    /**
     * SmartHomeApp注入googleApi密匙
     */
    @PostConstruct
    public void init() {
        try {
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("yoti-mart.json");
            assert resourceAsStream != null;
            GoogleCredentials credentials = GoogleCredentials.fromStream(resourceAsStream);
            googleSmartHomeApp.setCredentials(credentials);
        } catch (Exception e) {
            log.error("couldn't load credentials");
        }
    }


    /**
     * 主动向google申请同步
     *
     * @param userId 同步新增或删除设备所属的用户ID
     * @return
     */
    public Result requestSync(Integer userId) {
        log.info("begin request sync!!!");
        HomeGraphApiServiceProto.RequestSyncDevicesResponse response = googleSmartHomeApp.requestSync(String.valueOf(userId));
        log.info("RSYNC-Response:" + response.toString());
        return Result.ok();
    }

    public String action(JSONObject data) {
        log.info("request data：" + data.toString());
        SmartHomeRequest smartHomeRequest = SmartHomeRequest.Companion.create(data.toJSONString());
        //获取请求头
        //用于接收响应的数据
        JSONObject responseJson = new JSONObject();
        try {

            //获取请求的数据
            ArrayList inputs = (ArrayList) data.get("inputs");

            //google请求id
            String requestId = (String) data.get("requestId");

            //命名空间
            LinkedHashMap intent = (LinkedHashMap) inputs.get(0);
            String namespace = (String) intent.get("intent");

            System.out.println("begin================================================================================");
            log.info("intend:" + namespace);
            switch (namespace) {
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

            //google请求Id
            responseJson.put("requestId", requestId);

        } catch (ServiceException e) {
            e.printStackTrace();
            responseJson.put("errorCode", e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            String errorCode = "service error";
            responseJson.put("errorCode", errorCode);
        }

        log.info("responseJson:" + responseJson.toJSONString());

        System.out.println("begin====================================================================================");

        return responseJson.toJSONString();
    }


}
