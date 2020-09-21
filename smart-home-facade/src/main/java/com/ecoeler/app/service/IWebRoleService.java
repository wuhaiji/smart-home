package com.ecoeler.app.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.app.bean.v1.PageBean;
import com.ecoeler.app.bean.v1.WebRoleBean;
import com.ecoeler.app.dto.v1.BasePageDto;
import com.ecoeler.app.entity.WebRole;


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
     * @param basePageDto
     */
    PageBean<WebRoleBean> selectRoleList(BasePageDto basePageDto);

    /**
     * 查询下拉选择框所有角色
     *
     * @return 角色列表
     */
    List<WebRole> selectRoleComboBoxRoleList();
}
