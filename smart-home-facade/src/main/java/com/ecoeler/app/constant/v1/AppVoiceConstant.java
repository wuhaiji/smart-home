package com.ecoeler.app.constant.v1;

/**
 * voice action常量
 */
public interface AppVoiceConstant {
    /**
     * 请求头map
     */
    String DTO_KEY_HEADER_MAP = "headerMap";
    /**
     * 客户端id
     */
    String DTO_KEY_CLIENT_ID = "clientId";
    /**
     * client id
     */
    String DTO_KEY_USER_ID = "userId";

    /**
     * client id
     */
    String DTO_KEY_AUTHORIZATION = "accessToken";

    /**
     * client id
     */
    String HEADER_AUTHORIZATION = "authorization";

    /**
     * google
     */
    String GOOGLE_CLIENT = "google_client";
    /**
     * alexa
     */
    String ALEXA_CLIENT = "alexa_client";

    /**
     * client_name
     */
    String CLIENT_NAME = "client_name";

    String GOOGLE_NET_STATE_KEY = "online";
    String GOOGLE_ON_OFF_KEY = "on";
    String GOOGLE_COMMAND_BRIGHTNESS = "action.devices.commands.BrightnessAbsolute";
    String GOOGLE_COMMAND_ONOFF = "action.devices.commands.OnOff";
    String GOOGLE_BRIGHTNESS_KEY = "brightness";
    String YUNTUN_BRIGHTNESS_DATA_KEY = "light";
    String GOOGLE_SUCCESS_COMMANDS_RESPONSE = "SUCCESS";
    String GOOGLE_FAILED_COMMANDS_RESPONSE = "ERROR";


}
