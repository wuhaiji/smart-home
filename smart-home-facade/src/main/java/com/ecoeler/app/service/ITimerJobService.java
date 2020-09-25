package com.ecoeler.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.app.entity.DeviceSpace;
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
     * 添加倒计时任务
     * @param timerJob
     * @return
     */
    Long add(TimerJob timerJob, DeviceSpace space);

}
