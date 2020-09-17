package com.ecoeler.app.dto.v1;

import lombok.Data;

/**
 * 指定用户分配角色
 *
 * @author tcx
 */
@Data
public class AllocationRoleDto {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 角色id
     */
    private Long id;
    /**
     * 角色
     */
    private String role;
}
