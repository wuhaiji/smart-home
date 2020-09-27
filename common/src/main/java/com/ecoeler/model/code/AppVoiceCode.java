package com.ecoeler.model.code;


public enum AppVoiceCode implements ResultCode {

    ACTION_EXECUTION_ERROR("30000", " error occurred in action execute"),

    ACTION_SELECT_DEVICE_LIST_ERROR("30001", " error occurred in query user device list "),

    ACTION_PARAMS_ERROR("30002", " Parameter error "),

    ACTION_SELECT_DEVICE_STATES_ERROR("30003", " error occurred in query  device states "),

    ACTION_SELECT_DEVICE_TYPE_ERROR("30004", " error occurred in query  device type "),

    ACTION_DEVICE_TYPE_NOT_EXIST("30005", " device type does not exist "),

    ACTION_DEVICE_NOT_EXIST("30006", " device does not exist "),

    ACTION_SELECT_DEVICE_KEY_ERROR("30007", " error occurred in query  device key "),

    ACTION_SELECT_DEVICE_SWITCH_ERROR("30008", " error occurred in query  device switch "),
    ;


    AppVoiceCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 错误码
     */
    private final String code;

    /**
     * 错误描述
     */
    private final String msg;


    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}
