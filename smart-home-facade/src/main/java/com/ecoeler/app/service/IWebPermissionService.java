package com.ecoeler.app.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.app.bean.v1.MenuWebPermissionBean;
import com.ecoeler.app.entity.WebPermission;

import java.util.List;


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

    List<String> selectPermissionByRoleId(Long roleId);
}
