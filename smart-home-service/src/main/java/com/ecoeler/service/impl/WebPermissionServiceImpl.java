package com.ecoeler.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.bean.v1.*;
import com.ecoeler.app.dto.v1.CustomizationPermissionDto;
import com.ecoeler.app.entity.WebPermission;
import com.ecoeler.app.entity.WebRolePermission;
import com.ecoeler.app.mapper.WebPermissionMapper;
import com.ecoeler.app.mapper.WebRolePermissionMapper;
import com.ecoeler.app.mapper.WebUserMapper;
import com.ecoeler.app.service.IWebPermissionService;
import com.ecoeler.cache.ClearCache;
import com.ecoeler.cache.SetCache;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.util.ExceptionUtil;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private IWebPermissionService iWebPermissionService;


    /**
     * 查询所菜单权限
     *
     * @return
     */
    @Override
    public PermissionBean selectAllPermission() {
        QueryWrapper<WebPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "permission", "permission_name", "parent_id", "source_type");
        return getTree(baseMapper.selectList(queryWrapper));
    }


    /**
     * 根据用id限查询前端用户权限
     *
     * @param userId 用户id
     * @return
     */
    @Override
    public WebUserPermissionBean selectWebPermissionByUserId(Long userId) {
        WebUserPermissionBean bean = iWebPermissionService.selectWebPermissionByRoleId
                (webUserMapper.selectById(userId).getRoleId());
        bean.setUserId(userId);
        return bean;
    }

    /**
     * 根据角色id查询权限
     *
     * @param roleId
     * @return
     */
    @SetCache("PER_WEB#${roleId}")
    @Override
    public WebUserPermissionBean selectWebPermissionByRoleId(Long roleId) {
        WebUserPermissionBean bean = new WebUserPermissionBean();
        List<WebRolePermission> permissions = iWebPermissionService.selectRolePermissionIds(roleId);
        if (permissions != null && permissions.size() != 0) {
            List<Long> ids = permissions.stream().map(WebRolePermission::getPermissionId).collect(Collectors.toList());
            LambdaQueryWrapper<WebPermission> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(WebPermission::getId, ids)
                    .select(WebPermission::getPermission,
                            WebPermission::getPermissionName,
                            WebPermission::getId,
                            WebPermission::getSourceType,
                            WebPermission::getParentId);
            List<WebPermission> webAllPermissions = baseMapper.selectList(queryWrapper);
            if (webAllPermissions != null && webAllPermissions.size() != 0) {
                //菜单权限
                List<WebPermission> webMenuPermissions = webAllPermissions.stream().filter(item -> 0 == item.getSourceType()).collect(Collectors.toList());
                if (webMenuPermissions.size() != 0) {
                    List<MenuPermissionBean> menusS = getTree(webMenuPermissions).getMenus();
                    List<MenuWebPermissionBean> menus = new ArrayList<>();
                    for (MenuPermissionBean menuPermissionBean : menusS) {
                        MenuWebPermissionBean permissionBean = new MenuWebPermissionBean();
                        permissionBean.setParentMenuName(menuPermissionBean.getMenuPermissionName());
                        List<String> cString = new ArrayList<>();
                        List<MenuPermissionBean> children = menuPermissionBean.getChildren();
                        if (children != null) {
                            for (MenuPermissionBean child : children) {
                                cString.add(child.getMenuPermissionName());
                            }
                            permissionBean.setChildrenNames(cString);
                        }
                        menus.add(permissionBean);
                    }
                    bean.setMenus(menus);
                    //按钮权限
                    List<WebPermission> webButtonPermissions = webAllPermissions.stream().filter(item -> 1 == item.getSourceType()).collect(Collectors.toList());
                    if (webButtonPermissions.size() != 0) {
                        List<String> buttons = webButtonPermissions
                                .stream()
                                .map(WebPermission::getPermission)
                                .collect(Collectors.toList());
                        bean.setButtons(buttons);
                    } else {
                        bean.setButtons(new ArrayList<>());
                    }
                }
            } else {
                bean.setButtons(new ArrayList<>());
                bean.setMenus(new ArrayList<>());
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
        List<WebRolePermission> permissions = iWebPermissionService.selectRolePermissionIds(roleId);
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
    @SetCache("ROLE_PER_IDS#${roleId}")
    @Override
    public List<WebRolePermission> selectRolePermissionIds(Long roleId) {
        QueryWrapper<WebRolePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "role_id", "permission_id");
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
        return iWebPermissionService.selectBackPermissionByRoleId(webUserMapper.selectById(userId).getRoleId());
    }

    /**
     * 定制权限
     *
     * @param customizationPermissionDto 权限
     */
    @ClearCache(value = {"PER_WEB#${customizationPermissionDto.roleId}", "PER_BACK#${customizationPermissionDto.roleId}", "ROLE_PER_IDS#${customizationPermissionDto.roleId}"})
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void customizationPermission(CustomizationPermissionDto customizationPermissionDto) {
        ExceptionUtil.notNull(customizationPermissionDto.getRoleId(), TangCode.NULL_ROLE_ID_EMPTY_ERROR);
        Long roleId = customizationPermissionDto.getRoleId();
        //先将之前的权限删除
        List<WebRolePermission> rolePermissions = iWebPermissionService.selectRolePermissionIds(roleId);
        if (rolePermissions != null && rolePermissions.size() != 0) {
            List<Long> ids = rolePermissions.stream().map(WebRolePermission::getId).collect(Collectors.toList());
            webRolePermissionMapper.deleteBatchIds(ids);
        }
        //菜单权限Ids
        List<Long> permissionIds = customizationPermissionDto.getPermissions();
        List<WebRolePermission> list = new ArrayList<>();
        if (permissionIds != null) {
            for (Long permissionId : permissionIds) {
                WebRolePermission webRolePermission = new WebRolePermission();
                webRolePermission.setPermissionId(permissionId);
                webRolePermission.setRoleId(roleId);
                list.add(webRolePermission);
            }
        }
        webRolePermissionMapper.batchInsert(list);
        //saveBatch(list);
    }

    /**
     * 查询回显权限
     *
     * @param roleId 角色id
     * @return
     */
    @Override
    public WebEchoUserPermissionBean selectEchoPermissionByRoleId(Long roleId) {
        WebEchoUserPermissionBean webEchoUserPermissionBean = new WebEchoUserPermissionBean();
        List<WebRolePermission> permissions = iWebPermissionService.selectRolePermissionIds(roleId);
        if (permissions != null && permissions.size() != 0) {
            //所有权限
            List<Long> permissionIds = permissions.stream()
                    .map(WebRolePermission::getPermissionId)
                    .collect(Collectors.toList());
            //菜单权限
            LambdaQueryWrapper<WebPermission> webPermissionQueryWrapper = new LambdaQueryWrapper<>();
            webPermissionQueryWrapper.select(WebPermission::getId)
                    .in(WebPermission::getId, permissionIds)
                    .eq(WebPermission::getSourceType, 0);
            List<WebPermission> webPermissions = baseMapper.selectList(webPermissionQueryWrapper);
            List<Long> menuIds = new ArrayList<>();
            if (webPermissions != null && webPermissions.size() != 0) {
                menuIds = webPermissions.stream().map(WebPermission::getId).collect(Collectors.toList());
            }
            //按钮权限
            permissionIds.removeAll(menuIds);
            webEchoUserPermissionBean.setMenus(menuIds);
            webEchoUserPermissionBean.setButtons(permissionIds);
            return webEchoUserPermissionBean;
        }
        webEchoUserPermissionBean.setMenus(new ArrayList<>());
        webEchoUserPermissionBean.setButtons(new ArrayList<>());
        return webEchoUserPermissionBean;
    }


    /**
     * 封装菜单权限 包括权限下的子菜单
     */
    private PermissionBean getTree(List<WebPermission> webAllPermissions) {
        PermissionBean permissionBean = new PermissionBean();
        if (webAllPermissions != null && webAllPermissions.size() != 0) {
            //菜单权限
            List<WebPermission> webPermissions = webAllPermissions.stream().filter(item -> 0 == item.getSourceType()).collect(Collectors.toList());
            List<WebPermission> webButtonPermissions = webAllPermissions.stream().filter(item -> 1 == item.getSourceType()).collect(Collectors.toList());
            //根节点
            List<MenuPermissionBean> root = new ArrayList<>();
            Map<Long, MenuPermissionBean> menuWebIdToPermissionMap = new HashMap<>(6);
            if (webPermissions.size() != 0) {
                //分类 将统一父节点的分到同一组 Long 父节点的id
                Map<Long, List<MenuPermissionBean>> menuWebPermissionBeanMap = new HashMap<>(6);
                for (WebPermission webPermission : webPermissions) {
                    Long parentId = webPermission.getParentId();
                    List<MenuPermissionBean> children;
                    //子节点
                    if (parentId != null) {
                        if (menuWebPermissionBeanMap.get(parentId) != null) {
                            children = menuWebPermissionBeanMap.get(parentId);
                        } else {
                            children = new ArrayList<>();
                        }
                        MenuPermissionBean bean = new MenuPermissionBean(webPermission.getId(), webPermission.getPermission(), webPermission.getPermissionName());
                        menuWebIdToPermissionMap.put(bean.getId(), bean);
                        children.add(bean);
                        menuWebPermissionBeanMap.put(parentId, children);
                    } else {
                        //根节点
                        MenuPermissionBean bean = new MenuPermissionBean(webPermission.getId(), webPermission.getPermission(), webPermission.getPermissionName());
                        root.add(bean);
                        menuWebIdToPermissionMap.put(bean.getId(), bean);
                    }
                }
                //将所有子节点挂载到响应的父节点上
                for (Map.Entry<Long, List<MenuPermissionBean>> entry : menuWebPermissionBeanMap.entrySet()) {
                    MenuPermissionBean bean = menuWebIdToPermissionMap.get(entry.getKey());
                    bean.setChildren(entry.getValue());
                }
                permissionBean.setMenus(root);
            }
            if (webButtonPermissions.size() != 0) {
                //父页面id对应的button
                Map<Long, List<ButtonPermissionBean>> menuIdToButtons = new HashMap<>(6);
                for (WebPermission buttonPermission : webButtonPermissions) {
                    Long parentId = buttonPermission.getParentId();
                    List<ButtonPermissionBean> children;
                    if (menuIdToButtons.get(parentId) != null) {
                        children = menuIdToButtons.get(parentId);
                    } else {
                        children = new ArrayList<>();
                    }
                    ButtonPermissionBean buttonPermissionBean = new ButtonPermissionBean(buttonPermission.getId(), buttonPermission.getPermissionName());
                    children.add(buttonPermissionBean);
                    menuIdToButtons.put(parentId, children);
                }
                List<ButtonWebPermissionBean> buttons = new ArrayList<>();
                for (Map.Entry<Long, List<ButtonPermissionBean>> entry : menuIdToButtons.entrySet()) {
                    ButtonWebPermissionBean buttonWebPermissionBean = new ButtonWebPermissionBean();
                    buttonWebPermissionBean.setParentMenuName(menuWebIdToPermissionMap
                            .get(entry.getKey())
                            .getMenuPermission());
                    buttonWebPermissionBean.setButtons(entry.getValue());
                    buttons.add(buttonWebPermissionBean);
                }
                permissionBean.setButtons(buttons);
            }
        }
        return permissionBean;
    }


}

