package com.ecoeler.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author tang
 * @since 2020-09-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sh_device_key")
public class DeviceKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 产品ID
     */
    private String productId;

    /**
     * 键
     */
    private String dataKey;

    /**
     * 类型：Boolean Integer Enum
     */
    private String keyType;

    /**
     * 对于类型的描述
     */
    private String keyInfo;

    /**
     * 键的名称 中文
     */
    private String zhKeyName;

    /**
     * 键的名称 英文
     */
    private String enKeyName;

    /**
     * 谷歌语音key对应的state名称
     */
    private String googleStateName;

    /**
     * alexa语音对应的命名空间
     */
    private String alexaNamespace;

    /**
     * alexa语音对应的state名称
     */
    private String alexaStateName;

    private Integer actionType;


}
