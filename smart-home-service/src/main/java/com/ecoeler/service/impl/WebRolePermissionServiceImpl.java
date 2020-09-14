package com.ecoeler.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.entity.WebRolePermission;
import com.ecoeler.app.mapper.WebRolePermissionMapper;
import com.ecoeler.app.service.IWebRolePermissionService;
import com.ecoeler.cache.ClearCache;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.model.code.CommonCode;
import com.ecoeler.model.code.PermissionCode;
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
    @ClearCache("PER#${roleId}")
    @Override
    public void customizationPermission(List<Long> permissionIds, Long roleId) {
       try {
           //先将之前的权限删除
           QueryWrapper<WebRolePermission> queryWrapper=new QueryWrapper<>();
           queryWrapper.eq("role_id",roleId);
           baseMapper.delete(queryWrapper);
           if (permissionIds!=null&&permissionIds.size()!=0){
               List<WebRolePermission> list=new ArrayList<>();
               for (Long permissionId : permissionIds) {
                   WebRolePermission webRolePermission=new WebRolePermission();
                   webRolePermission.setPermissionId(permissionId);
                   webRolePermission.setRoleId(roleId);
                   list.add(webRolePermission);
               }
               saveBatch(list);
           }else {
               log.error("未选择权限");
               throw  new ServiceException(CommonCode.INVALID_PARAM);
           }
       }catch (ServiceException e){
           throw  new ServiceException(e.getCode(),e.getMsg());
       }catch (Exception e){
           log.error("定制权限异常");
           throw  new ServiceException(PermissionCode.CUSTOMIZATION);
       }
    }

    /**
     * 根据roleId查询关联数据
     * @param roleId
     * @return
     */
    @Override
    public List<WebRolePermission> selectPermissionList(Long roleId) {
        QueryWrapper<WebRolePermission> queryWrapper=new QueryWrapper<>();
        queryWrapper.select("role_id","permission_id");
        queryWrapper.eq("role_id",roleId);
        return baseMapper.selectList(queryWrapper);
    }
}
