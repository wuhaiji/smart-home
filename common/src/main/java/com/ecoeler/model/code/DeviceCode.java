package com.ecoeler.model.code;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/10/27
 */
public enum DeviceCode implements ResultCode {

    FLOOR_ID_ERROR("50001", "FloorId Can not be null."),
    FLOOR_NOT_EXIST("50002", "Floor does not exist."),
    FLOOR_DEVICES_SELECT_ERROR("50003", "The query floor equipment list is abnormal."),
    ;


    DeviceCode(String code, String msg) {
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
