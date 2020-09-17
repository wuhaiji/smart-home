package com.ecoeler.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.bean.v1.MenuWebPermissionBean;
import com.ecoeler.app.bean.v1.WebEchoUserPermissionBean;
import com.ecoeler.app.bean.v1.WebUserPermissionBean;
import com.ecoeler.app.entity.WebPermission;
import com.ecoeler.app.entity.WebRolePermission;
import com.ecoeler.app.mapper.WebPermissionMapper;
import com.ecoeler.app.mapper.WebRolePermissionMapper;
import com.ecoeler.app.mapper.WebUserMapper;
import com.ecoeler.app.service.IWebPermissionService;
import com.ecoeler.cache.SetCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
@Service
public class WebPermissionServiceImpl extends ServiceImpl<WebPermissionMapper, WebPermission> implements IWebPermissionService {
    @Autowired
    private WebUserMapper webUserMapper;
    @Autowired
    private WebRolePermissionMapper webRolePermissionMapper;

    /**
     * 查询所菜单权限
     *
     * @return
     */
    @Override
    public List<MenuWebPermissionBean> selectAllMenuPermission() {
        QueryWrapper<WebPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "permission_name", "parent_id");
        queryWrapper.eq("source_type", 0);
        //查询所有菜单权限
        return getTree(baseMapper.selectList(queryWrapper));
    }

    /**
     * 根据角色id查询权限
     *
     * @param roleId
     * @return
     */
    @SetCache("PER_WEB#${roleId}")
    @Override
    public WebUserPermissionBean selectPermissionByRoleId(Long roleId) {
        WebUserPermissionBean bean = new WebUserPermissionBean();
        List<WebRolePermission> permissions = selectPermissionList(roleId);
        if (permissions != null && permissions.size() != 0) {
            List<Long> ids = permissions.stream().map(WebRolePermission::getPermissionId).collect(Collectors.toList());
            //菜单权限
            LambdaQueryWrapper<WebPermission> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(WebPermission::getSourceType, 0)
                    .in(WebPermission::getId, ids);
            List<MenuWebPermissionBean> menuWebPermissionBeans = getTree(baseMapper.selectList(queryWrapper));
            bean.setMenus(menuWebPermissionBeans);
            //按钮权限
            LambdaQueryWrapper<WebPermission> queryButtonWrapper = new LambdaQueryWrapper<>();
            queryButtonWrapper.eq(WebPermission::getSourceType, 1)
                    .in(WebPermission::getId, ids)
                    .select(WebPermission::getPermissionName);
            List<WebPermission> webButtonPermissions = baseMapper.selectList(queryButtonWrapper);
            if (webButtonPermissions != null && webButtonPermissions.size() != 0) {
                List<String> buttons = webButtonPermissions
                        .stream()
                        .map(WebPermission::getPermissionName)
                        .collect(Collectors.toList());
                bean.setButtons(buttons);
            }
        }
        //封装菜单权限
        return bean;
    }

    /**
     * 根据角色id查询权限 用户后台权限控制
     *
     * @param roleId
     * @return
     */
    @SetCache("PER_BACK#${roleId}")
    @Override
    public Set<String> selectBackPermissionByRoleId(Long roleId) {
        List<WebRolePermission> permissions = selectPermissionList(roleId);
        QueryWrapper<WebPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "permission", "source_type");
        queryWrapper.in("id", permissions.stream()
                .map(WebRolePermission::getPermissionId)
                .collect(Collectors.toSet()));
        return baseMapper.selectList(queryWrapper).stream().map(WebPermission::getPermission).collect(Collectors.toSet());

    }

    /**
     * 根据roleId查询对应的permissionIds
     *
     * @param roleId
     * @return
     */
    @Override
    public List<WebRolePermission> selectPermissionList(Long roleId) {
        QueryWrapper<WebRolePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("role_id", "permission_id");
        queryWrapper.eq("role_id", roleId);
        return webRolePermissionMapper.selectList(queryWrapper);
    }

    /**
     * 根据用户id查询权限
     *
     * @param userId 指定用户id
     * @return 权限
     */
    @Override
    public Set<String> getPerByUserId(Long userId) {
        return selectBackPermissionByRoleId(webUserMapper.selectById(userId).getRoleId());
    }

    @Override
    public WebEchoUserPermissionBean selectEchoPermissionByRoleId(Long roleId) {
        WebEchoUserPermissionBean webEchoUserPermissionBean = new WebEchoUserPermissionBean();
        List<WebRolePermission> permissions = webRolePermissionMapper.selectList(
                new QueryWrapper<WebRolePermission>()
                        .lambda()
                        .select(WebRolePermission::getPermissionId)
                        .eq(WebRolePermission::getRoleId, roleId));
        if (permissions != null && permissions.size() != 0) {
            List<Long> permissionIds = permissions.stream()
                    .map(WebRolePermission::getPermissionId)
                    .collect(Collectors.toList());
            //菜单权限
            QueryWrapper<WebPermission> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .select(WebPermission::getId)
                    .in(WebPermission::getId, permissionIds)
                    //菜单权限sourceType=0
                    .eq(WebPermission::getSourceType, 0);
            List<WebPermission> webMenuPermissions = baseMapper.selectList(queryWrapper);
            List<Long> menuIds = null;
            if (webMenuPermissions != null && webMenuPermissions.size() != 0) {
                menuIds = webMenuPermissions.stream().map(WebPermission::getId).collect(Collectors.toList());
            }
            //按钮权限
            QueryWrapper<WebPermission> queryButtonWrapper = new QueryWrapper<>();
            queryButtonWrapper.lambda()
                    .select(WebPermission::getPermissionName)
                    .in(WebPermission::getId, permissionIds)
                    //按钮权限sourceType=1
                    .eq(WebPermission::getSourceType, 1);
            List<WebPermission> webButtonPermissions = baseMapper.selectList(queryButtonWrapper);
            List<String> buttons = null;
            if (webButtonPermissions != null && webButtonPermissions.size() != 0) {
                buttons = webButtonPermissions.stream().map(WebPermission::getPermissionName).collect(Collectors.toList());
            }

            webEchoUserPermissionBean.setMenus(menuIds);
            webEchoUserPermissionBean.setButtons(buttons);
            return webEchoUserPermissionBean;
        }
        return webEchoUserPermissionBean;
    }

    /**
     * 封装菜单权限 包括权限下的子菜单
     */
    private List<MenuWebPermissionBean> getTree(List<WebPermission> webPermissions) {
        if (webPermissions != null && webPermissions.size() != 0) {
            Map<Long, MenuWebPermissionBean> menuWebIdToPermissionMap = new HashMap<>(6);
            List<MenuWebPermissionBean> root = new ArrayList<>();
            //分类 将统一父节点的分到同一组 Long 父节点的id
            Map<Long, List<MenuWebPermissionBean>> menuWebPermissionBeanMap = new HashMap<>(6);
            for (WebPermission webPermission : webPermissions) {
                Long parentId = webPermission.getParentId();
                List<MenuWebPermissionBean> children = null;
                //子节点
                if (parentId != null) {
                    if (menuWebPermissionBeanMap.get(parentId) != null) {
                        children = menuWebPermissionBeanMap.get(parentId);
                    } else {
                        children = new ArrayList<>();
                    }
                    MenuWebPermissionBean bean = new MenuWebPermissionBean(webPermission.getId(), webPermission.getPermissionName());
                    menuWebIdToPermissionMap.put(bean.getId(), bean);
                    children.add(bean);
                    menuWebPermissionBeanMap.put(parentId, children);
                } else {
                    //根节点
                    MenuWebPermissionBean bean = new MenuWebPermissionBean(webPermission.getId(), webPermission.getPermissionName());
                    root.add(bean);
                    menuWebIdToPermissionMap.put(bean.getId(), bean);
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
    }


}

