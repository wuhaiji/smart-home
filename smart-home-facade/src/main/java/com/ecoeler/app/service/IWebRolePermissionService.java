package com.ecoeler.app.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.app.dto.v1.CustomizationPermissionDto;
import com.ecoeler.app.entity.WebRolePermission;


import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
public interface IWebRolePermissionService extends IService<WebRolePermission> {
    /**
     * 给角色分配权限
     *
     * @param customizationPermissionDto 权限集合 及角色Id
     */
    void customizationPermission(CustomizationPermissionDto customizationPermissionDto);
}
