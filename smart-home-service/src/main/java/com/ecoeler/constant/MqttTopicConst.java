package com.ecoeler.constant;

/**
 * Mqtt 主题
 * @author tang
 * @since 2020/9/15
 */
public class MqttTopicConst {

    //-----------------------V1版本的通道------------------------------

    /** 报活通道 **/
    public static final String V1_HEART_CHANNEL="$share/g1/heart";
    /** 遗言通道 **/
    public static final String V1_LWT_CHANNEL="$share/g1/lwt";

    //-----------------------V2版本的通道-------------------------------

    /** 统一消息上报通道 **/
    public static final String V2_ALIVE_CHANNEL="$share/g1/msg";

    //-----------------------指令下发通道-------------------------------

    public static final String CMD_CHANNEL="/cmd/";


}
