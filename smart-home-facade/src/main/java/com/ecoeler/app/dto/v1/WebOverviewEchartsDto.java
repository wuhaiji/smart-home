package com.ecoeler.app.dto.v1;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author TangCX
 */
@Data
public class WebOverviewEchartsDto {
    public static final int ECHARTS_DEFAULT_DATES=14-1;
    public static final int ECHARTS_MAX_DAY=60;
    private String tableName;
    private LocalDate startDate;
    private LocalDate endDate;
}
