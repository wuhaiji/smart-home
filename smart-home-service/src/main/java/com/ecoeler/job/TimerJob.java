package com.ecoeler.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * 场景定时条件 任务
 * @author tang
 * @since 2020/8/20
 */
@Slf4j
@Component
public class TimerJob {

    @XxlJob("TimerJob")
    public ReturnT<String> timeJob(String param) throws Exception {
        log.info(param);
        return ReturnT.SUCCESS;
    }

}
