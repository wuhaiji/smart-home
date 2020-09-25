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
@TableName("sh_device")
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 产品ID
     */
    private String productId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 类型中文名
     */
    private String zhTypeName;

    /**
     * 类型英文名
     */
    private String enTypeName;

    /**
     * 设备图标
     */
    private String deviceIcon;

    private LocalDateTime onlineTime;

    private LocalDateTime offlineTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 设备所属房间ID，没有的房间默认为0
     */
    private Long roomId;

    /**
     * 所属家庭id
     */
    private Long familyId;

    /**
     * 在线离线 ，1在线 ，0离线
     */
    private Integer netState;
    /**
     * 设备是否为网关设备
     */
    private Integer gatewayLike;


    private String eventClass;

}
