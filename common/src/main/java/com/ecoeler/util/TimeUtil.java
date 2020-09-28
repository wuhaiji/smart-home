package com.ecoeler.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecoeler.app.dto.v1.QueryTimeDto;
import com.ecoeler.model.code.TangCode;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 时间工具
 * @author tangcx
 */
public class TimeUtil {
    public static  final  String START="start";
    public static  final  String END="end";
    private TimeUtil(){}
    private static DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * 字符串转为时间
     */
    public static LocalDateTime timeFormat(String formatTime){
        return  LocalDateTime.parse(formatTime,dateTimeFormatter);
    }

    /**
     * 封装查询的时间段 并验证查询时间段
     * @param queryTimeDto 查询时间段
     * @return 查询时间段
     */
    public static Map<String, LocalDateTime> verifyQueryTime(QueryTimeDto queryTimeDto) {
        Map<String, LocalDateTime> result=new HashMap<>(4);
        //String start = Optional.ofNullable(queryTimeDto.getStartTime()).orElse("");
        //String end = Optional.ofNullable(queryTimeDto.getEndTime()).orElse("");
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;


       /* if (!"".equals(start)) {
            startTime = TimeUtil.timeFormat(start);
        }
        if (!"".equals(end)) {
            endTime = TimeUtil.timeFormat(end);
        }*/
        if (startTime != null && endTime != null) {
            ExceptionUtil.startIsAfterEnd(startTime, endTime, TangCode.START_TIME_AFTER_END_TIME);
        }
        result.put("start",startTime);
        result.put("end",endTime);
        return result;
    }

    /**
     * 一天的最开始的日期
     * @param queryTime 查询日期
     * @return 查询一天最早的时间
     */
    public static LocalDateTime getEarliestTimeOfDay(LocalDateTime queryTime){
        return LocalDateTime.of(queryTime.toLocalDate(), LocalTime.MIN);
    }

    /**
     * 一天的最后日期
     * @param queryTime 查询日期
     * @return 查询一天最晚的时间
     */
    public static LocalDateTime getLatestTimeOfDay(LocalDateTime queryTime){
        return LocalDateTime.of(queryTime.toLocalDate(),LocalTime.MAX);
    }


}
