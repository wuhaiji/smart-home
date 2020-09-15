package com.ecoeler.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.entity.WebPermission;
import com.ecoeler.app.entity.WebRolePermission;
import com.ecoeler.app.mapper.WebRolePermissionMapper;
import com.ecoeler.app.service.IWebPermissionService;
import com.ecoeler.app.service.IWebRolePermissionService;
import com.ecoeler.cache.ClearCache;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.model.code.PermissionCode;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private IWebPermissionService iWebPermissionService;
    /**
     * 定制权限
     * @param permissionIds 权限ids
     * @param roleId 角色id
     */
    @ClearCache("PER_WEB#${roleId},PER_BACK#${roleId}")
    @Override
    public void customizationPermission(List<Long> permissionIds, Long roleId) {
       try {
           //先将之前的权限删除
           QueryWrapper<WebRolePermission> queryWrapper=new QueryWrapper<>();
           queryWrapper.eq("role_id",roleId);
           baseMapper.delete(queryWrapper);
           List<WebRolePermission> list=new ArrayList<>();
           //找到概览的权限id
           WebPermission webPermission = iWebPermissionService.selectPermissionByCondition(0, "概览");
           if (!permissionIds.contains(webPermission.getId())){
               //增加默认的权限
               WebRolePermission webRolePermission=new WebRolePermission();
               webRolePermission.setRoleId(roleId);
               webRolePermission.setPermissionId(webPermission.getId());
               list.add(webRolePermission);
           }
           if (permissionIds.size()!=0){
               for (Long permissionId : permissionIds) {
                   WebRolePermission webRolePermission=new WebRolePermission();
                   webRolePermission.setPermissionId(permissionId);
                   webRolePermission.setRoleId(roleId);
                   list.add(webRolePermission);
               }
           }
           saveBatch(list);
       }catch (Exception e){
           log.error("定制权限异常");
           throw  new ServiceException(PermissionCode.CUSTOMIZATION);
       }
    }

    /**
     * 根据roleId查询对应的permissionIds
     * @param roleId
     * @return
     */
    @Override
    public List<WebRolePermission> selectPermissionList(Long roleId) {
       try {
           QueryWrapper<WebRolePermission> queryWrapper=new QueryWrapper<>();
           queryWrapper.select("role_id","permission_id");
           queryWrapper.eq("role_id",roleId);
           return baseMapper.selectList(queryWrapper);
       }catch (Exception e){
           log.error("定制权限异常");
           throw  new ServiceException(PermissionCode.SELECT_PERMISSION_BY_ROLE_ID);
       }
    }
}
