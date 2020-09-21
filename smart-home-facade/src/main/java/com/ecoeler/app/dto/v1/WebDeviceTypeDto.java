package com.ecoeler.app.dto.v1;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
@Data

public class WebDeviceTypeDto extends BasePageDto {
    /**
     * 类型名称
     */
    private String typeName;
    /**
     * 产品ID
     */
    private String productId;

}
