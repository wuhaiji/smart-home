package com.ecoeler.app.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.app.bean.v1.PermissionBean;
import com.ecoeler.app.bean.v1.WebEchoUserPermissionBean;
import com.ecoeler.app.bean.v1.WebUserPermissionBean;
import com.ecoeler.app.entity.WebPermission;
import com.ecoeler.app.entity.WebRolePermission;

import java.util.List;
import java.util.Set;


/**
 * <p>
 * 服务类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
public interface IWebPermissionService extends IService<WebPermission> {
    /**
     * 查询所有菜单权限
     *
     * @return 菜单权限列表
     */
    PermissionBean selectAllPermission();

    /**
     * 根据用户id查询菜单权限列表
     *
     * @param userId 用户id
     * @return 菜单权限列表
     */
    WebUserPermissionBean selectWebPermissionByUserId(Long userId);

    /**
     * 根据用户id查询菜单权限列表
     *
     * @param roleId 用户id
     * @return 菜单权限列表
     */
    WebUserPermissionBean selectWebPermissionByRoleId(Long roleId);

    /**
     * 查询后端权限控制的当前用户权限 用字符串表示只返回 数据库permission字段
     *
     * @param roleId 角色id
     * @return permission字段 列表
     */
    Set<String> selectBackPermissionByRoleId(Long roleId);



    /**
     * 根据角色查询权限列表
     *
     * @param roleId 角色id
     * @return 权限Ids
     */
    List<WebRolePermission> selectPermissionList(Long roleId);

    /**
     * 根据用户ID查询后台控制权限
     * @param userId
     * @return
     */
    Set<String> getPerByUserId(Long userId);

    /**
     * 角色回显权限
     * @param roleId 角色id
     * @return
     */
    WebEchoUserPermissionBean selectEchoPermissionByRoleId(Long roleId);
}
