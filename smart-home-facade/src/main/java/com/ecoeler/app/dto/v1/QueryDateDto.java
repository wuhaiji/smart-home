package com.ecoeler.app.dto.v1;

import lombok.Data;

/**
 * @author TangCX
 */
@Data
public class QueryDateDto {
    /**
     * 开始时间
     */
    private String startDate;
    /**
     * 结束时间
     */
    private String endDate;
}

