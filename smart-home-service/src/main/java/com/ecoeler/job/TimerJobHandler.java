package com.ecoeler.job;

import com.alibaba.fastjson.JSONObject;
import com.ecoeler.app.entity.Device;
import com.ecoeler.app.entity.TimerJob;
import com.ecoeler.app.msg.OrderInfo;
import com.ecoeler.app.service.IDeviceService;
import com.ecoeler.app.service.ITimerJobService;
import com.ecoeler.constant.TimerJobConst;
import com.ecoeler.core.DeviceEvent;
import com.ecoeler.utils.SpringUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * 场景定时条件 任务
 * @author tang
 * @since 2020/8/20
 */
@Slf4j
@Component
public class TimerJobHandler {

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private ITimerJobService timerJobService;

    @Autowired
    private JobTool jobTool;

    @XxlJob("TimerJob")
    public ReturnT<String> timeJob(String param) throws Exception {

        log.info("asdadasd");

        //找到定时信息
        Long timerJobId=Long.parseLong(param);
        TimerJob timerJob = timerJobService.getById(timerJobId);
        //找到设备，及其处理 类
        Device device = deviceService.getById(timerJob.getDeviceId());
        if(device==null){
            log.warn("---- timer job，device not find!");
            return ReturnT.SUCCESS;
        }
        DeviceEvent deviceEvent = (DeviceEvent)SpringUtil.getBean(device.getEventClass());
        //拼装命令 并下发
        OrderInfo orderInfo=new OrderInfo();
        orderInfo.setDeviceId(timerJob.getDeviceId());
        orderInfo.setProductId(timerJob.getProductId());
        orderInfo.setMsg(JSONObject.parseObject( timerJob.getCmd()) );
        deviceEvent.order(orderInfo);

        if(timerJob.getType() == TimerJobConst.COUNTDOWN){
            jobTool.remove(timerJob.getJobId());
            timerJobService.deleteJob(timerJobId);
            return ReturnT.SUCCESS;
        }

        //修改定时信息
        timerJob.setNextTime(new CronSequenceGenerator(timerJob.getJobCron()).next(new Date()));
        timerJobService.updateById(timerJob);
        return ReturnT.SUCCESS;
    }

}
