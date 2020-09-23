package com.ecoeler.app.dto.v1.voice;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data(staticConstructor = "of")
@Accessors(chain = true)
public class DeviceKeyVoiceDto {

    /**
     * alexaInterface ，alexa设备接口
     */
    private String alexaInterface;

    /**
     * googleStateName 谷歌设备key名称
     */

    private String googleStateName;

    /**
     * alexaStateName alexa 设备key名称
     */
    private String alexaStateName;

    /**
     * deviceKeyList ,批量查询参数
     */
    private List<String> deviceKeyList;

    /**
     * actionType 0:可上传 1:可上传，可下发 2：可下发
     */
    private int actionType;


    /**
     * 可能值
     */
    public static final int ACTION_TYPE_0 = 0;

    public static final int ACTION_TYPE_1 = 1;

    public static final int ACTION_TYPE_2 = 2;




}
