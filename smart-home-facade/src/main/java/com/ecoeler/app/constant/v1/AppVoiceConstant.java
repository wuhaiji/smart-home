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
    String GOOGLE_COMMAND_ON_OFF = "action.devices.commands.OnOff";
    String GOOGLE_BRIGHTNESS_KEY = "brightness";
    String YUNTUN_BRIGHTNESS_DATA_KEY = "light";
    String GOOGLE_SUCCESS_COMMANDS_RESPONSE = "SUCCESS";
    String GOOGLE_FAILED_COMMANDS_RESPONSE = "ERROR";

    Integer GOOGLE_LINK_STATUS_IS_NOT = 0;
    Integer GOOGLE_LINK_STATUS_IS_YES = 1;


    String YUNTUN_POWER_STATE_ON = "1";
    String YUNTUN_POWER_STATE_OFF = "0";

    String ALEXA_POWER_STATE_ON = "ON";
    String ALEXA_POWER_STATE_OFF = "OFF";


    String ALEXA_NET_STATE_OFFLINE = "UNREACHABLE";
    String ALEXA_NET_STATE_ONLINE = "OK";
    String ALEXA_STATE_NAME_POWER_STATE = "powerState";
    String ALEXA_VALUE_TURN_OFF = "TurnOff";
    String ALEXA_VALUE_TURN_ON = "TurnOn";
    String ALEXA_DISCOVER_RESPONSE = "Discover.Response";

    String ALEXA_STATE_NAME_BRIGHTNESS = "brightness";


}
