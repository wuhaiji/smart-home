package com.ecoeler.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.entity.WebRolePermission;
import com.ecoeler.app.mapper.WebRolePermissionMapper;
import com.ecoeler.app.service.IWebRolePermissionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */

@Service
public class WebRolePermissionServiceImpl extends ServiceImpl<WebRolePermissionMapper, WebRolePermission> implements IWebRolePermissionService {
    /**
     * 定制权限
     * @param permissionIds 权限ids
     * @param roleId 角色id
     */
    @Override
    public void customizationPermission(List<Long> permissionIds, Long roleId) {
        if (permissionIds!=null&&permissionIds.size()!=0){
            List<WebRolePermission> list=new ArrayList<>();
            for (Long permissionId : permissionIds) {
                WebRolePermission webRolePermission=new WebRolePermission();
                webRolePermission.setPermissionId(permissionId);
                webRolePermission.setRoleId(roleId);
                list.add(webRolePermission);
            }
            //批量插入
            saveBatch(list);
        }else {
            //throw new Exception("未选择权限");
        }

    }
}
