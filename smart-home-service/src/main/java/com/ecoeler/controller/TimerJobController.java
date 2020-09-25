package com.ecoeler.controller;


import com.ecoeler.app.entity.TimerJob;
import com.ecoeler.app.service.ITimerJobService;

import com.ecoeler.model.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 任务控制器
 * @author tang
 * @since 2020/9/25
 */
@RestController
@RequestMapping("/timer-job")
public class TimerJobController {

    @Autowired
    private ITimerJobService timerJobService;

    /**
     * 新增计时任务
     * @param timerJob
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody TimerJob timerJob){
        return Result.ok(timerJobService.add(timerJob));
    }

    /**
     * 更新任务信息（不涉及时间）
     * @param timerJob
     * @return
     */
    @PostMapping("/update")
    public Result update(@RequestBody TimerJob timerJob){
        return Result.ok(timerJobService.updateById(timerJob));
    }

    /**
     * 更新任务的cron！
     * @param id
     * @param jobCron
     * @return
     */
    @PostMapping("/update/cron")
    public Result updateCron(@RequestParam Long id,@RequestParam String jobCron){
        timerJobService.updateCron(id,jobCron);
        return Result.ok();
    }

    /**
     * 删除计时任务
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public Result delete(@RequestParam Long id){
        timerJobService.deleteJob(id);
        return Result.ok();
    }

}
