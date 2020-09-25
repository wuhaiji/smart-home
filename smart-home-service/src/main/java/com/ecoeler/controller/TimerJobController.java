package com.ecoeler.controller;


import com.ecoeler.app.dto.v1.TimerJobDto;
import com.ecoeler.app.entity.DeviceSpace;
import com.ecoeler.app.entity.TimerJob;
import com.ecoeler.app.service.IDeviceService;
import com.ecoeler.app.service.ITimerJobService;

import com.ecoeler.model.response.Result;
import org.bouncycastle.cms.PasswordRecipientId;
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

    @Autowired
    private IDeviceService deviceService;

    @PostMapping("/add")
    public Result add(@RequestBody TimerJob timerJob){

        DeviceSpace deviceSpace = deviceService.getDeviceSpace(timerJob.getDeviceId());


        return null;

    }

}
