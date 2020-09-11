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
@TableName("sh_device_type")
public class DeviceType implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 网关类型：0否，显示在首页，1是，不显示在首页
     */
    private Boolean gatewayLike;

    /**
     * 产品ID
     */
    private String productId;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;


}
