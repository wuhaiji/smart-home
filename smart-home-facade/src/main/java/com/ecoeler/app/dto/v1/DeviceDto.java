package com.ecoeler.app.dto.v1;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author tangcx
 * @since 2020-09-10
 */
@Data

public class DeviceDto  {

    private Long id;


    /**
     * 设备名称
     */
    private String deviceName;

}
