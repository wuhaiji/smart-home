package com.ecoeler.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.bean.v1.MenuWebPermissionBean;
import com.ecoeler.app.entity.WebPermission;
import com.ecoeler.app.entity.WebRolePermission;
import com.ecoeler.app.mapper.WebPermissionMapper;
import com.ecoeler.app.service.IWebPermissionService;
import com.ecoeler.app.service.IWebRolePermissionService;
import com.ecoeler.cache.SetCache;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.model.code.PermissionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
@Service
public class WebPermissionServiceImpl extends ServiceImpl<WebPermissionMapper, WebPermission> implements IWebPermissionService {
    @Autowired
    private IWebRolePermissionService iWebRolePermissionService;
    /**
     * 查询所菜单权限
     * @return
     */
    @Override
    public List<MenuWebPermissionBean> selectAllMenuPermission() {
       try {
           QueryWrapper<WebPermission> queryWrapper=new QueryWrapper<>();
           queryWrapper.select("id","permission_name","parent_id");
           queryWrapper.eq("source_type",0);
           //查询所有菜单权限
           List<WebPermission> webPermissions = baseMapper.selectList(queryWrapper);
           if (webPermissions!=null&&webPermissions.size()!=0){
               Map<Long,MenuWebPermissionBean> menuWebIdToPermissionMap=new HashMap<>();
               List<MenuWebPermissionBean> root=new ArrayList<>();
               //分类 将统一父节点的分到同一组 Long 父节点的id
               Map<Long,List<MenuWebPermissionBean>> menuWebPermissionBeanMap=new HashMap<>();

               for (WebPermission webPermission : webPermissions) {
                   Long parentId = webPermission.getParentId();
                   List<MenuWebPermissionBean> children=null;
                   //有父节点
                   if (parentId!=null){
                       if (menuWebPermissionBeanMap.get(parentId)!=null){
                           children=menuWebPermissionBeanMap.get(parentId);
                       }else {
                           children=new ArrayList<>();
                       }
                       MenuWebPermissionBean bean=new MenuWebPermissionBean(webPermission.getId(),webPermission.getPermissionName());
                       menuWebIdToPermissionMap.put(bean.getId(),bean);
                       children.add(bean);
                       menuWebPermissionBeanMap.put(parentId,children);
                   }else {
                       //根节点
                       MenuWebPermissionBean bean=new MenuWebPermissionBean(webPermission.getId(),webPermission.getPermissionName());
                       root.add(bean);
                       menuWebIdToPermissionMap.put(bean.getId(),bean);
                   }
               }
               //将所有子节点挂载到响应的父节点上
               for (Map.Entry<Long, List<MenuWebPermissionBean>> entry : menuWebPermissionBeanMap.entrySet()) {
                   MenuWebPermissionBean bean = menuWebIdToPermissionMap.get(entry.getKey());
                   bean.setChildren(entry.getValue());
               }
               return root;
           }
           return null;
       }catch (Exception e){
           log.error(Optional.ofNullable(e.getMessage()).orElse(""), Optional.ofNullable(e.getCause()).orElse(e));
           throw new ServiceException(PermissionCode.SELECT_ALL_MENU_PERMISSION);
       }
    }

    /**
     * 根据角色id查询权限
     * @param roleId
     * @return
     */
    @SetCache("PER#${roleId}")
    @Override
    public List<String> selectPermissionByRoleId(Long roleId) {
       try {
           List<WebRolePermission> permissions= iWebRolePermissionService.selectPermissionList(roleId);
           List<WebPermission> webPermissions = baseMapper.selectBatchIds(permissions.stream().map(WebRolePermission::getPermissionId).collect(Collectors.toSet()));
           if (webPermissions!=null&&webPermissions.size()!=0){
               return webPermissions.stream().map(WebPermission::getPermissionName).collect(Collectors.toList());
           }
           return null;
       }catch (Exception e){
           log.error(Optional.ofNullable(e.getMessage()).orElse(""), Optional.ofNullable(e.getCause()).orElse(e));
           throw new ServiceException(PermissionCode.SELECT_PERMISSION_BY_ROLE_ID);
       }
    }


}

