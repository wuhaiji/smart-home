package com.ecoeler.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author tangcx
 * @since 2020-09-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sh_web_statistics")
public class WebStatistics implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     *用户数量
     */
    private Integer userNumber;

    /**
     * 设备数量
     */
    private Integer deviceNumber;

    /**
     * 日期
     */
    private String  date;

    public WebStatistics() {
    }

    public WebStatistics(String date) {
        this.date = date;
    }

    public WebStatistics(Integer userNumber, Integer deviceNumber) {
        this.userNumber = userNumber;
        this.deviceNumber = deviceNumber;
    }

    public WebStatistics(Integer userNumber, Integer deviceNumber, String date) {
        this.userNumber = userNumber;
        this.deviceNumber = deviceNumber;
        this.date = date;
    }
}
