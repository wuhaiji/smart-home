package com.ecoeler.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author tang
 * @since 2020-09-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sh_timer_job")
public class TimerJob implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 任务ID
     */
    private Integer jobId;

    /**
     * 0：倒计时，1：定时
     */
    private Integer type;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 产品ID
     */
    private String productId;

    /**
     * 键值对
     */
    private String cmd;

    /**
     * 任务的cron表达式
     */
    private String jobCron;

    /**
     * 下次执行时间
     */
    private Date nextTime;

    /**
     * 位置名称
     */
    private String positionName;

    /**
     * 定时器状态
     */
    private Integer status;

    /**
     * 家庭ID
     */
    private Long familyId;

}
