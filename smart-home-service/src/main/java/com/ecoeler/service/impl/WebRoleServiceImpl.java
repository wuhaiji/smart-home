package com.ecoeler.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.entity.WebRole;
import com.ecoeler.app.entity.WebRolePermission;
import com.ecoeler.app.mapper.WebRoleMapper;
import com.ecoeler.app.service.IWebRolePermissionService;
import com.ecoeler.app.service.IWebRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
@Service
public class WebRoleServiceImpl extends ServiceImpl<WebRoleMapper, WebRole> implements IWebRoleService {
    @Autowired
    private IWebRolePermissionService iWebRolePermissionService;

    /**
     * 新增用户角色
     * @param webRole
     * @return 新增id
     */
    @Override
    public Long addRole(WebRole webRole) {
        baseMapper.insert(webRole);
        return webRole.getId();
    }

    /**
     * 修改角色
     * @param webRole
     */
    @Override
    public void updateRole(WebRole webRole) {
        baseMapper.selectById(webRole);
    }

    /**
     * 删除角色
     * @param id
     */
    @Override
    public void deleteRole(Long id) {
        QueryWrapper<WebRolePermission> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("role_id",id);
        //删除角色权限表中的
        iWebRolePermissionService.remove(queryWrapper);
        baseMapper.deleteById(id);
    }

}
