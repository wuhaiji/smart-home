package com.ecoeler.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 设备开关状态表
 * </p>
 *
 * @author tang
 * @since 2020-09-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sh_device_switch")
public class DeviceSwitch implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 产品ID
     */
    private String productId;

    /**
     * 键
     */
    private String dataKey;

    /**
     * 设备 开 键对应的值
     */
    private String deviceOn;

    /**
     * 设备 关 键对应的值
     */
    private String deviceOff;

    /**
     * 对应device_key表的id
     */

    private Long deviceKeyId;

}
