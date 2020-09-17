package com.ecoeler.app.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.app.bean.v1.PageBean;
import com.ecoeler.app.bean.v1.WebRoleBean;
import com.ecoeler.app.dto.v1.AllocationRoleDto;
import com.ecoeler.app.dto.v1.WebUserDto;
import com.ecoeler.app.entity.WebRole;
import com.ecoeler.app.entity.WebUser;


import java.util.List;
import java.util.Set;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
public interface IWebUserService extends IService<WebUser> {

    /**
     * 根据角色Id分组查询用户数量
     *
     * @return 角色bean
     */
    List<WebRoleBean> selectUserCountByRoleId();

    /**
     * 新增用户
     *
     * @param webUser 用户信息
     * @return 新增id
     */
    Long addWebUser(WebUser webUser);

    /**
     * 修改用户信息
     *
     * @param webUser 用户信息
     */
    void updateWebUser(WebUser webUser);

    /**
     * 删除用户
     *
     * @param id 用户Id
     */
    void deleteWebUser(Long id);

    /**
     * 分页按条件查询用户列表
     *
     * @param webUserDto 查询条件
     * @return 用户列表
     */
    PageBean<WebUser> queryWebUserList(WebUserDto webUserDto);

    /**
     * 给指定用户分配角色
     *
     * @param allocationRoleDto 指定用户 角色 信息
     */
    void allocationWebUserRole(AllocationRoleDto allocationRoleDto);
}
