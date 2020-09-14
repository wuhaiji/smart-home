package com.ecoeler.app.bean.v1;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用于返回用户的设备信息列表的对象
 * whj
 * 2020.01.02
 */
@Data
@Accessors(chain = true)
@RequiredArgsConstructor(staticName = "of")
public class DeviceVoiceBean {
    /**
     * 设备ID
     */
    private Long id;
    /**
     * 房间ID ,如果是0表示该设备未设置房间
     */
    private Integer roomId;

    /**
     * room名称
     */
    private String roomName = "not set room";
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 设备默认名称，通常是制造商名称，SKU等。
     */
    private String defaultNames;
    /**
     * 设备别称
     */
    private String nicknames;
    /**
     * google设备类型
     */
    private String googleTypeName;
    /**
     * google设备特性
     */
    private String googleTraitNames;
    /**
     * alexa app显示的设备类别
     */
    private String alexaDisplayName;
}
