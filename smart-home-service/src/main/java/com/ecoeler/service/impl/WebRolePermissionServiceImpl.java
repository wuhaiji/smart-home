package com.ecoeler.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.dto.v1.CustomizationPermissionDto;
import com.ecoeler.app.dto.v1.PermDto;
import com.ecoeler.app.entity.WebPermission;
import com.ecoeler.app.entity.WebRolePermission;
import com.ecoeler.app.mapper.WebPermissionMapper;
import com.ecoeler.app.mapper.WebRolePermissionMapper;
import com.ecoeler.app.service.IWebPermissionService;
import com.ecoeler.app.service.IWebRolePermissionService;
import com.ecoeler.cache.ClearCache;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.model.code.PermissionCode;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.util.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.generic.LineNumberGen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
@Slf4j
@Service
public class WebRolePermissionServiceImpl extends ServiceImpl<WebRolePermissionMapper, WebRolePermission> implements IWebRolePermissionService {
    @Autowired
    private WebPermissionMapper webPermissionMapper;

    /**
     * 定制权限
     *
     * @param customizationPermissionDto 权限
     */
    @ClearCache("PER_WEB#${customizationPermissionDto.roleId},PER_BACK#${customizationPermissionDto.roleId}")
    @Override
    public void customizationPermission(CustomizationPermissionDto customizationPermissionDto) {
        log.info("----start");
        ExceptionUtil.notNull(customizationPermissionDto.getRoleId(), TangCode.NULL_ROLE_ID_EMPTY_ERROR);
        Long roleId = customizationPermissionDto.getRoleId();
        //先将之前的权限删除
        QueryWrapper<WebRolePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        baseMapper.delete(queryWrapper);

        //菜单权限Ids
        List<Long> permissionIds = customizationPermissionDto.getMenuIds();
        List<WebRolePermission> list = new ArrayList<>();
        //找到概览的权限id
        WebPermission webPermission = webPermissionMapper.selectOne(new LambdaQueryWrapper<WebPermission>()
                .eq(WebPermission::getSourceType, 0)
                .eq(WebPermission::getPermissionName, "概览"));
        if (permissionIds != null && !permissionIds.contains(webPermission.getId())) {
            //增加默认的权限
            WebRolePermission webRolePermission = new WebRolePermission();
            webRolePermission.setRoleId(roleId);
            webRolePermission.setPermissionId(webPermission.getId());
            list.add(webRolePermission);
        }
        //按钮权限
        List<String> buttons = customizationPermissionDto.getButtons();
        if (buttons != null && buttons.size() != 0) {
            QueryWrapper<WebPermission> webPermissionQueryWrapper = new QueryWrapper<>();
            webPermissionQueryWrapper.lambda().in(WebPermission::getPermissionName, buttons).select(WebPermission::getId);
            List<Long> permissionButtonIds = webPermissionMapper.selectList(webPermissionQueryWrapper)
                    .stream()
                    .map(WebPermission::getId)
                    .collect(Collectors.toList());
            //按钮权限不为空
            if (permissionButtonIds.size() != 0) {
                if (permissionIds != null) {
                    permissionIds.addAll(permissionButtonIds);
                } else {
                    permissionIds = permissionButtonIds;
                }
            }

        }
        if (permissionIds != null) {
            for (Long permissionId : permissionIds) {
                WebRolePermission webRolePermission = new WebRolePermission();
                webRolePermission.setPermissionId(permissionId);
                webRolePermission.setRoleId(roleId);
                list.add(webRolePermission);
            }
        }
        saveBatch(list);
        log.info("----end");
    }

    @ClearCache("PER_WEB#${dto.roleId},PER_BACK#${dto.roleId}")
    @Override
    public void changeRolePermission(PermDto dto) {
        Long roleId = dto.getRoleId();
        //先将之前的权限删除
        QueryWrapper<WebRolePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        baseMapper.delete(queryWrapper);

        //
        List<String> permissions = dto.getPermissions();
        List<WebRolePermission> list=new ArrayList<>();
        for (String permission:permissions){
            WebRolePermission webRolePermission=new WebRolePermission();
            webRolePermission.setPermission(permission);
            webRolePermission.setRoleId(roleId);
            list.add(webRolePermission);
        }
        saveBatch(list);
    }


}
