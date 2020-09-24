package com.ecoeler.app.bean.v1;

import lombok.Data;

/**
 * @author TangCX
 */
@Data
public class WebOverviewDataStatisticsBean {
    /**
     * 设备总数
     */
    private int deviceTotalCount;
    /**
     * 设备今日增加量
     */
    private int deviceTodayCount;
    /**
     * 设备日环比
     */
    private float deviceDayCompare;
    /**
     * 用户总数
     */
    private int customerTotalCount;
    /**
     * 用户今日增加量
     */
    private int customerTodayCount;
    /**
     * 用户日环比
     */
    private float customerDayCompare;
}
