package com.ecoeler.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.entity.WebRolePermission;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
public interface IWebRolePermissionService extends IService<WebRolePermission> {

    void customizationPermission(List<Long> permissionIds, Long roleId);
}