package com.ecoeler.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@TableName("sh_web_role")
public class WebRole implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String roleName;

    private String role;

    private String description;


}
