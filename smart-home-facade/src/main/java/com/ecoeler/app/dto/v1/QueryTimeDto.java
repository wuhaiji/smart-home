package com.ecoeler.app.dto.v1;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 查询时间
 *
 * @author tcx
 */
@Data
public class QueryTimeDto {
    /**
     * 查询时间段类型 0-online_time 1-offline_time 2-create_time 3-update_time
     */
    private Integer timeType;
    /**
     * 开始时间
     */
    //@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date startTime;
    /**
     * 结束时间
     */
    //@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date endTime;

}
