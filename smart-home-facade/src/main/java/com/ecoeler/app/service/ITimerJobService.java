package com.ecoeler.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.app.entity.TimerJob;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tang
 * @since 2020-09-25
 */
public interface ITimerJobService extends IService<TimerJob> {

    /**
     * 添加 计时 任务
     * @param timerJob
     * @return
     */
    Long add(TimerJob timerJob);

    /**
     * 更新 计时 任务
     * @param id
     * @param jobCron
     */
    void updateCron(Long id,String jobCron);

    /**
     * 删除 计时 任务
     * @param id
     */
    void deleteJob(Long id);

}
