package com.ecoeler.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.bean.v1.PageBean;
import com.ecoeler.app.bean.v1.WebRoleBean;
import com.ecoeler.app.dto.v1.BasePageDto;
import com.ecoeler.app.entity.WebRole;
import com.ecoeler.app.entity.WebRolePermission;
import com.ecoeler.app.entity.WebUser;
import com.ecoeler.app.mapper.WebRoleMapper;
import com.ecoeler.app.mapper.WebRolePermissionMapper;
import com.ecoeler.app.mapper.WebUserMapper;
import com.ecoeler.app.service.IWebRoleService;
import com.ecoeler.cache.ClearCache;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.model.code.TangCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
@Service
public class WebRoleServiceImpl extends ServiceImpl<WebRoleMapper, WebRole> implements IWebRoleService {
    @Autowired
    private WebRolePermissionMapper webRolePermissionMapper;
    @Autowired
    private WebUserMapper webUserMapper;

    /**
     * 新增用户角色
     *
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
     *
     * @param webRole
     */
    @Override
    public void updateRole(WebRole webRole) {
        baseMapper.updateById(webRole);
    }

    /**
     * 删除角色
     *
     * @param id
     */
    @ClearCache(value = {"PER#${id}","PER_BACK#${id}"})
    @Override
    public void deleteRole(Long id) {
        QueryWrapper<WebUser> webUserQueryWrapper=new QueryWrapper<>();
        webUserQueryWrapper.select("id")
                .eq("role_id",id);
        List<WebUser> webUsers = webUserMapper.selectList(webUserQueryWrapper);
        if (webUsers.size()!=0){
            log.error("还有用户是当前角色,不能删除");
            throw new ServiceException(TangCode.CODE_ROLE_TO_USER_NOT_EMPTY);

        }
        QueryWrapper<WebRolePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", id);

        //删除角色权限表中的
        webRolePermissionMapper.delete(queryWrapper);
        baseMapper.deleteById(id);
    }

    /**
     * 查询所有角色
     *
     * @return
     * @param basePageDto 分页
     */
    @Override
    public PageBean<WebRoleBean> selectRoleList(BasePageDto basePageDto) {
        List<WebRoleBean> beanResult = new ArrayList<>();
        QueryWrapper<WebRole> queryWrapper = new QueryWrapper<>();
        Page<WebRole> page=new Page<>();
        page.setCurrent(basePageDto.getCurrent());
        page.setSize(basePageDto.getSize());
        Page<WebRole> webRolePage = baseMapper.selectPage(page, queryWrapper);
        List<WebRole> webRoles = webRolePage.getRecords();
        PageBean<WebRoleBean> result=new PageBean<>();
        result.setPages(webRolePage.getPages());
        result.setTotal(webRolePage.getTotal());
        if (webRoles != null && webRoles.size() != 0) {
            List<WebRoleBean> webRoleBeans = webUserMapper.selectUserCountByRoleId();
            for (WebRole webRole : webRoles) {
                Long roleId = webRole.getId();
                WebRoleBean webRoleBean = new WebRoleBean();
                BeanUtils.copyProperties(webRole, webRoleBean);
                //封装角色对应的客户数量
                for (WebRoleBean roleBean : webRoleBeans) {
                    if (roleBean.getId() != null && roleBean.getId().equals(roleId)) {
                        webRoleBean.setCount(roleBean.getCount());
                    }
                }
                if (webRoleBean.getCount()==null){
                    webRoleBean.setCount(0);
                }
                beanResult.add(webRoleBean);
            }

        }
        result.setList(beanResult);
        return result;
    }
    /**
     * 查询所有角色下拉选择框
     *
     * @return
     */
    @Override
    public List<WebRole> selectRoleComboBoxRoleList() {
        QueryWrapper<WebRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "role_name");
        return baseMapper.selectList(queryWrapper);
    }


}
