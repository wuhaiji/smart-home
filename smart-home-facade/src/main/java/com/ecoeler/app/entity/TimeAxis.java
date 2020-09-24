package com.ecoeler.app.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2020-09-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sh_time_axis")
public class TimeAxis implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private LocalDateTime nextTime;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 键的名称
     */
    private String zhKeyName;

    /**
     * 键的名称
     */
    private String enKeyName;

    /**
     * 操作
     */
    private String zhOperation;

    /**
     * 操作
     */
    private String enOperation;

    /**
     * 位置
     */
    private String position;

    /**
     * 家庭ID
     */
    @TableField("familyId")
    private Long familyId;


}
