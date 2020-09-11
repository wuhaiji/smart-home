package com.ecoeler.app.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.app.bean.v1.PageBean;
import com.ecoeler.app.bean.v1.WebRoleBean;
import com.ecoeler.app.dto.WebUserDto;
import com.ecoeler.app.entity.WebUser;


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
public interface IWebUserService extends IService<WebUser> {


    List<WebRoleBean> selectUserCountByRoleId();

    Long addWebUser(WebUser webUser);

    void updateWebUser(WebUser webUser);

    void deleteWebUser(Long id);

    PageBean<WebUser> queryWebUserList(WebUserDto webUserDto, Page<WebUser> page);
}
