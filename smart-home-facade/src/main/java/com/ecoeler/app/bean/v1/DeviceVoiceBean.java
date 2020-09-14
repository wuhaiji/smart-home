package com.ecoeler.app.bean.v1;

import lombok.Data;

import java.io.Serializable;

/**
 * 用于返回用户的设备信息列表的对象
 * whj
 * 2020.01.02
 */
@Data
public class DeviceVoiceBean implements Serializable {

    private static final long serialVersionUID = 1358708084514836065L;
    /**
     * 设备ID
     */
    private String id;
    /**
     *房间ID ,如果是0表示该设备未设置房间
     */
    private Integer roomId;

    /**
     * room名称
     */
    private String roomName;
    /**
     *设备名称
     */
    private String name;
    /**
     * 设备默认名称，通常是制造商名称，SKU等。
     */
    private String defaultNames;
    /**
     *  设备别称
     */
    private String nicknames;
    /**
     * 设备在线状态，0表示离线，1表示在线
     */
    private Integer online;
    /**
     * google设备类型
     */
    private String googleTypeName;
    /**
     * google设备特性
     */
    private String googleTraitName;
    /**
     * alexa app显示的设备类别
     */
    private String alexaDisplayName;
    /**
     * alexa app设备描述
     */
    private String description;

    /**
     * 语音语义分析name
     */
    private String nameForVoice;
}
