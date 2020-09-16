package com.ecoeler.app.service;


import com.baomidou.mybatisplus.extension.service.IService;
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
     * @param permissionIds 权限ids
     * @param roleId        角色id
     */
    void customizationPermission(List<Long> permissionIds, Long roleId);

    /**
     * 根据角色查询权限列表
     *
     * @param roleId 角色id
     * @return 权限Ids
     */
    List<WebRolePermission> selectPermissionList(Long roleId);
}
