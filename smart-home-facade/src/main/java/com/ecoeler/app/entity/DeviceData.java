package com.ecoeler.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sh_device_data")
public class DeviceData implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 键
     */
    private String dataKey;

    /**
     * 值
     */
    private String dataValue;

    /**
     * 序列号
     */
    private Long seq;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    private LocalDateTime createTime;

    private Long dataKeyId;


}
