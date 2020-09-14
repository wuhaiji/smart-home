package com.ecoeler.app.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.app.bean.v1.WebRoleBean;
import com.ecoeler.app.entity.WebRole;


import javax.management.relation.Role;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
public interface IWebRoleService extends IService<WebRole> {

    Long addRole(WebRole webRole);

    void updateRole(WebRole webRole);

    void deleteRole(Long id);

    List<WebRoleBean> selectRoleList();
}