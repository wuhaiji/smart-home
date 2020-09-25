package com.ecoeler.model.code;


/**
 * 错误码
 * @author tang
 * @since 2020/9/14
 */
public enum  TangCode implements ResultCode {

    //参数错误
    CODE_EMAIL_EMPTY_ERROR("40001","邮箱不能为空!"),
    CODE_PASSWORD_EMPTY_ERROR("40002","密码不能为空!"),
    CODE_CODE_EMPTY_ERROR("40003","验证码不能为空!"),
    CODE_USERNAME_EMPTY_ERROR("40004","用户名不能为空!"),
    CODE_REFRESH_TOKEN_EMPTY_ERROR("40005","刷新token不能为空!"),
    CODE_ACCOUNT_EMPTY_ERROR("40006","账号不能为空!"),
    CODE_FAMILY_NAME_EMPTY_ERROR("40007","家庭名称不能为空!"),
    CODE_FAMILY_TYPE_NULL_ERROR("40008","家庭类型必选!"),
    CODE_FLOOR_ID_NULL_ERROR("40009","楼层ID不能为空且不能为0!"),
    CODE_FAMILY_ID_NULL_ERROR("40010","家庭ID不能为空!"),
    CODE_ROOM_NAME_EMPTY_ERROR("40011","房间名称不能为空!"),
    CODE_ROOM_TYPE_EMPTY_ERROR("40012","房间类型不能为空!"),
    CODE_FLOOR_NAME_EMPTY_ERROR("40013","楼层名称不能为空!"),
    CODE_ROOM_ID_NULL_ERROR("40014","房间ID不能为空!"),
    CODE_PRODUCT_ID_EMPTY_ERROR("40015","产品ID不能为空!"),
    CODE_SCENE_NAME_EMPTY_ERROR("40016","场景名称不能为空!"),
    CODE_SCENE_ID_NULL_ERROR("40017","场景ID不能为空!"),
    CODE_DEVICE_ID_EMPTY_ERROR("40018","设备ID不能为空!"),
    CODE_DATA_KEY_EMPTY_ERROR("40019","键名不能为空!"),
    CODE_DATA_VALUE_EMPTY_ERROR("40020","键值不能为空!"),
    CODE_ORDER_MSG_NULL_ERROR("40021","指令内容不能为空!"),
    CODE_ECHARTS_DAY_ERROR("40022","选择时间间隔不能大于60天"),
    CODE_FILE_EMPTY_ERROR("40023","未选择上传文件"),
    CODE_CMD_EMPTY_ERROR("40024","指令不能为空!"),
    CODE_CRON_EMPTY_ERROR("40025","CRON表达式不能为空!"),
    CODE_TIMER_JOB_ID_NULL_ERROR("40026","计时任务ID不能为空!"),

    //业务错误
    BLANK_PHONE_NUMBER_EMPTY_ERROR("20001","手机号不能为空"),
    PASSWORD_NOT_IN_RANGE_ERROR("20002","密码长度不在6~16之间"),
    EMAIL_NOT_MATCH_ERROR("20003","邮箱格式不匹配"),
    NULL_ROLE_ID_EMPTY_ERROR("20004","未选中角色"),
    NULL_ROLE_EMPTY_ERROR("20005","角色不能为空"),
    START_TIME_AFTER_END_TIME("20006","开始时间不能比结束时间晚"),
    CODE_LOGIN_ERROR("20007", "登录失败，账号或密码错误!"),
    CODE_CAPTCHA_ERROR("20008","图片验证码错误!"),
    CODE_LIMIT_EMAIL_ERROR("20009","两分钟内勿重复请求邮箱验证码!"),
    CODE_EMAIL_CODE_ERROR("20010","邮箱验证码错误!"),
    CODE_USER_EXIST("20011","用户已存在!"),
    CODE_FAMILY_NOT_VILLA("20012","当前家庭应该为别墅!"),
    CODE_UPLOAD_FILE_FAIL("20013","文件上传失败!"),
    CODE_ROLE_TO_USER_NOT_EMPTY("20014","该角色还有用户使用,不能删除"),

    //TOKEN
    CODE_TOKEN_ERROR("30005","无效TOKEN,TOKEN超时或异常!"),
    CODE_NO_AUTH_ERROR("30006","权限不足!")
    ;



    TangCode(String code, String msg) {
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
