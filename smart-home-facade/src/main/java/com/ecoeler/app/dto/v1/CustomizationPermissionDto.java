package com.ecoeler.app.dto.v1;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 定制权限
 * @author tcx
 */
@ToString
@Data
public class CustomizationPermissionDto {
    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单权限
     */
    private List<Long> permissions;
}
