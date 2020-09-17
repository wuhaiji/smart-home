package com.ecoeler.app.dto.v1;

import lombok.Data;

/**
 * 分页和查询时间
 * @author tcx
 */
@Data
public class BasePageQueryTimeDto extends QueryTimeDto {
    /**
     * 页码
     */
    private Integer current;
    /**
     * 每页数量
     */
    private Integer size;

}
