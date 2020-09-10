package com.ecoeler.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
@TableName("sh_floor")
public class Floor implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 楼层名称
     */
    private String floorName;

    /**
     * 家庭ID
     */
    private Long familyId;


}
