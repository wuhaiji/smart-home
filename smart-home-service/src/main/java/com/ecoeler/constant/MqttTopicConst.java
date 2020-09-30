package com.ecoeler.constant;

/**
 * Mqtt 主题
 * @author tang
 * @since 2020/9/15
 */
public class MqttTopicConst {

    /**
     * 共享队列 协议 前缀
     */
    public static final String SHARED_PROTOCOL="$share/g1/";

    //-----------------------V1版本的通道------------------------------

    /** 报活通道 **/
    public static final String V1_HEART_CHANNEL="/heart/";
    /** 遗言通道 **/
    public static final String V1_LWT_CHANNEL="/lwt";

    //-----------------------V2版本的通道-------------------------------

    /** 统一消息上报通道 **/
    public static final String V2_ALIVE_CHANNEL="/heart";

    //-----------------------指令下发通道-------------------------------

    public static final String CMD_CHANNEL="/cmd/";


}
