package com.ecoeler.app.dto.v1;

import lombok.Data;

/**
 * 定时器
 * @author tang
 * @since 2020/9/25
 */
@Data
public class TimerJobDto {

    /**
     * 任务ID
     */
    private String jobId;

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

}
