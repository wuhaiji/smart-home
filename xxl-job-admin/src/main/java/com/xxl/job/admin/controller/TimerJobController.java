package com.xxl.job.admin.controller;

import com.xxl.job.admin.controller.annotation.PermissionLimit;
import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import com.xxl.job.core.glue.GlueTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RequestMapping("/timer_job")
@RestController
public class TimerJobController {

    @Autowired
    private JobInfoController controller;

    public XxlJobInfo newJob(String handler){
        XxlJobInfo jobInfo =new XxlJobInfo();
        jobInfo.setJobGroup(1);
        jobInfo.setJobDesc("智能控制时间调度任务");
        jobInfo.setAddTime(new Date());
        jobInfo.setUpdateTime(new Date());
        jobInfo.setAuthor("xxl");
        jobInfo.setAlarmEmail("xxl@qq.com");
        jobInfo.setExecutorRouteStrategy("ROUND");
        jobInfo.setExecutorHandler(handler);
        jobInfo.setExecutorBlockStrategy(ExecutorBlockStrategyEnum.DISCARD_LATER.toString());
        jobInfo.setExecutorTimeout(0);
        jobInfo.setExecutorFailRetryCount(0);
        jobInfo.setGlueType(GlueTypeEnum.BEAN.toString());
        return jobInfo;
    }

    @RequestMapping("/add")
    @PermissionLimit(limit=false)
    public ReturnT addJob(String cron,String param,String handler){
        XxlJobInfo jobInfo=newJob(handler);
        jobInfo.setJobCron(cron);
        jobInfo.setExecutorParam(param);
        ReturnT<String> add = controller.add(jobInfo);
        int id=Integer.parseInt(add.getContent());
        controller.start(id);
        return add;
    }


    @RequestMapping("/update")
    @PermissionLimit(limit=false)
    public ReturnT updateJob(String cron, String param,int jobId , String handler){
        XxlJobInfo jobInfo=newJob(handler);
        jobInfo.setId(jobId);
        jobInfo.setJobCron(cron);
        jobInfo.setUpdateTime(new Date());
        jobInfo.setExecutorParam(param);
        return controller.update(jobInfo);
    }

    @RequestMapping("/remove")
    @PermissionLimit(limit=false)
    public ReturnT removeJob(int jobId){
        return controller.remove(jobId);
    }

    @RequestMapping("/stop")
    @PermissionLimit(limit=false)
    public ReturnT stopJob(int jobId){
        return controller.pause(jobId);
    }

    @RequestMapping("/start")
    @PermissionLimit(limit = false)
    public ReturnT startJob(int jobId){
        return controller.start(jobId);
    }

}
