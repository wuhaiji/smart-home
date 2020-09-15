package com.ecoeler.app.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.app.bean.v1.MenuWebPermissionBean;
import com.ecoeler.app.entity.WebPermission;

import java.util.List;
import java.util.Set;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
public interface IWebPermissionService extends IService<WebPermission> {

    List<MenuWebPermissionBean> selectAllMenuPermission();

    List<MenuWebPermissionBean> selectPermissionByRoleId(Long roleId);

    Set<String> selectBackPermissionByRoleId(Long roleId);

    WebPermission selectPermissionByCondition(Integer sourceType,String permissionName );
}
