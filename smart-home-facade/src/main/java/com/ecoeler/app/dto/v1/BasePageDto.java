package com.ecoeler.app.dto.v1;

import lombok.Data;

/**
 * 分页
 * @author tcx
 */
@Data
public class BasePageDto {
    private Integer current;
    private Integer size;
}
