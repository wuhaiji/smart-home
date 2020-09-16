package com.ecoeler.app.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.app.bean.v1.MenuWebPermissionBean;
import com.ecoeler.app.entity.WebPermission;

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
    List<MenuWebPermissionBean> selectAllMenuPermission();

    /**
     * 根据角色id查询菜单权限列表
     *
     * @param roleId 角色id
     * @return 菜单权限列表
     */
    List<MenuWebPermissionBean> selectPermissionByRoleId(Long roleId);

    /**
     * 查询后端权限控制的当前用户权限 用字符串表示只返回 数据库permission字段
     *
     * @param roleId 角色id
     * @return permission字段 列表
     */
    Set<String> selectBackPermissionByRoleId(Long roleId);

    /**
     * 根据条件查询权限
     *
     * @param sourceType     权限 类型  菜单或者按钮
     * @param permissionName 权限名字
     * @return 权限
     */
    WebPermission selectPermissionByCondition(Integer sourceType, String permissionName);
}
