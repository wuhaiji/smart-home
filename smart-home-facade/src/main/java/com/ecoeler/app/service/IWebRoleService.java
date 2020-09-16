package com.ecoeler.app.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.app.bean.v1.WebRoleBean;
import com.ecoeler.app.entity.WebRole;


import javax.management.relation.Role;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
public interface IWebRoleService extends IService<WebRole> {
    /**
     * 新增角色
     *
     * @param webRole 角色信息
     * @return 新增id
     */
    Long addRole(WebRole webRole);

    /**
     * 修改角色
     *
     * @param webRole 角色信息
     */
    void updateRole(WebRole webRole);

    /**
     * 删除角色
     *
     * @param id 角色id
     */
    void deleteRole(Long id);

    /**
     * 查询所有角色
     *
     * @return 所有角色Bean列表
     */
    List<WebRoleBean> selectRoleList();

    /**
     * 查询除了当前角色的其它角色列表
     *
     * @param roleId 当前角色
     * @return 角色列表
     */
    List<WebRole> queryRoleListExceptById(Long roleId);
}
