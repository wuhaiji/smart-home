package com.ecoeler.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.bean.v1.WebRoleBean;
import com.ecoeler.app.entity.WebRole;
import com.ecoeler.app.entity.WebRolePermission;
import com.ecoeler.app.mapper.WebRoleMapper;
import com.ecoeler.app.service.IWebRolePermissionService;
import com.ecoeler.app.service.IWebRoleService;
import com.ecoeler.app.service.IWebUserService;
import com.ecoeler.cache.ClearCache;
import org.springframework.beans.BeanUtils;
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
public class WebRoleServiceImpl extends ServiceImpl<WebRoleMapper, WebRole> implements IWebRoleService {
    @Autowired
    private IWebRolePermissionService iWebRolePermissionService;
    @Autowired
    private IWebUserService iWebUserService;

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
    @ClearCache("PER#${id}")
    @Override
    public void deleteRole(Long id) {
        QueryWrapper<WebRolePermission> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("role_id",id);
        //删除角色权限表中的
        iWebRolePermissionService.remove(queryWrapper);
        baseMapper.deleteById(id);
    }

    /**
     * 查询所有角色
     * @return
     */
    @Override
    public List<WebRoleBean> selectRoleList() {
        List<WebRoleBean> result=new ArrayList<>();
        QueryWrapper<WebRole> queryWrapper=new QueryWrapper<>();
        List<WebRole> webRoles = baseMapper.selectList(queryWrapper);
        if (webRoles!=null&&webRoles.size()!=0){
            List<WebRoleBean> webRoleBeans = iWebUserService.selectUserCountByRoleId();
            for (WebRole webRole : webRoles) {
                Long roleId=webRole.getId();
                WebRoleBean webRoleBean=new WebRoleBean();
                BeanUtils.copyProperties(webRole,webRoleBean);
                //封装角色对应的客户数量
                for (WebRoleBean roleBean : webRoleBeans) {
                    if (roleBean.getId().equals(roleId)){
                        webRoleBean.setCount(roleBean.getCount());
                    }
                }
                result.add(webRoleBean);
            }
        }
        return result;
    }

}
