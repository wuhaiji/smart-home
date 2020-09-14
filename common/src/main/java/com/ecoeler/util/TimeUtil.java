package com.ecoeler.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    private TimeUtil(){}
    private static DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * 字符串转为时间
     */
    public static LocalDateTime timeFormat(String formatTime){
        return  LocalDateTime.parse(formatTime,dateTimeFormatter);
    }

}
