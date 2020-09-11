package com.ecoeler.app.dto;

import lombok.Data;


@Data
public class WebUserDto {
    private String userName;
    private String email;
    private String phoneNumber;
    /**
     * 查询时间段类型
     */
    private Integer timeType;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;

}
