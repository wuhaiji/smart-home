package com.ecoeler.app.entity;

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

    private Long id;

    private String deviceId;

    private String productId;

    private String deviceName;

    private String deviceTypeName;

    private LocalDateTime onlineTime;

    private LocalDateTime offlineTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * roomId
     */
    private Long roomId;


}
