package com.ecoeler.job;

import com.xxl.job.core.biz.model.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;


/**
 * 任务工具
 * @author tang
 * @since 2020/8/20
 */
@RequestMapping("/job")
@RestController
public class JobTool {

    @Value("${xxl.job.admin.addresses}")
    private String addresses;

    private final RestTemplate restTemplate=new RestTemplate();

    private String [] urlArray;

    private static final String DEFAULT_HANDLER="TimerJob";

    public ReturnT add(String cron,String param){
        return add(cron,param,DEFAULT_HANDLER);
    }

    @RequestMapping("/add")
    public ReturnT add(String cron, String param , String handler){
        String api="/timer_job/add";
        String url=urlArray[ (int)(Math.random()*urlArray.length) ]+api;
        String params="?cron=%s&param=%s&handler=%s";
        String uri = url+String.format(params, cron, param, handler);
        return restTemplate.getForObject(uri,ReturnT.class);
    }


    public ReturnT update(String cron, String param,Integer jobId ){
        return update(cron, param, jobId , DEFAULT_HANDLER);
    }

    @RequestMapping("/update")
    public ReturnT update(String cron, String param,Integer jobId , String handler){
        String api="/timer_job/update";
        String url=urlArray[ (int)(Math.random()*urlArray.length) ]+api;
        String params="?cron=%s&param=%s&jobId=%d&handler=%s";
        String uri = url+String.format(params, cron, param, jobId,handler);
        return restTemplate.getForObject(uri,ReturnT.class);
    }

    @RequestMapping("/remove")
    public ReturnT remove(Integer jobId){
        String api="/timer_job/remove";
        String url=urlArray[ (int)(Math.random()*urlArray.length) ]+api;
        String params="?jobId=%d";
        String uri = url+String.format(params,jobId);
        return restTemplate.getForObject(uri,ReturnT.class);
    }

    @RequestMapping("/stop")
    public ReturnT stop(Integer jobId){
        String api="/timer_job/stop";
        String url=urlArray[ (int)(Math.random()*urlArray.length) ]+api;
        String params="?jobId=%d";
        String uri = url+String.format(params,jobId);
        return restTemplate.getForObject(uri,ReturnT.class);
    }

    @RequestMapping("/start")
    public ReturnT start(Integer jobId){
        String api="/timer_job/start";
        String url=urlArray[ (int)(Math.random()*urlArray.length) ]+api;
        String params="?jobId=%d";
        String uri = url+String.format(params,jobId);
        return restTemplate.getForObject(uri,ReturnT.class);
    }

    @PostConstruct
    public void postConstruct(){
        urlArray = addresses.split(",");
    }

}
