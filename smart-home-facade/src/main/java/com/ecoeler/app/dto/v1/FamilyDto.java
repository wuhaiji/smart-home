package com.ecoeler.app.dto.v1;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FamilyDto {

    private Long id;

    /**
     * 家庭名称
     */
    private String familyName;

    /**
     * APP首页家庭图片
     */
    private String familyImage;

    /**
     * 家庭类型：0别墅，1住宅
     */
    private Integer familyType;

    /**
     * 位置名称
     */
    private String positionName;

    /**
     * 坐标
     */
    private String coordinate;

    /**
     * 坐标类型
     */
    private String coordinateType;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;


}
