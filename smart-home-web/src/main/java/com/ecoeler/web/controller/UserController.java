package com.ecoeler.web.controller;


import com.ecoeler.app.dto.v1.AllocationRoleDto;
import com.ecoeler.app.dto.v1.WebUserDto;
import com.ecoeler.app.entity.WebUser;
import com.ecoeler.feign.WebUserService;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.model.response.Result;
import com.ecoeler.util.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户端口
 *
 * @author tangcx
 * @since 2020/9/16
 */
@Slf4j
@RestController
@RequestMapping("/web/user")
public class UserController {
    @Autowired
    private WebUserService webUserService;

    @RequestMapping("/user")
    public Result user(String account) {
        log.info("smart-home-web->UserController->begin query webUser by account");
        Result<WebUser> user = webUserService.getUser(account);
        if(user!=null){
            WebUser data = user.getData();
            if (data!=null){
                user.getData().setPassword(null);
            }
        }
        return user;
    }

    /**
     * 新增用户
     *
     * @param webUser
     * @return 新增id
     */
    @PreAuthorize("hasAuthority('user:AddUser')")
    @RequestMapping("/save")
    public Result saveWebUser(WebUser webUser) {
        log.info("smart-home-web->UserController->begin save webUser");
        ExceptionUtil.notBlank(webUser.getUserName(), TangCode.CODE_USERNAME_EMPTY_ERROR);
        ExceptionUtil.notBlank(webUser.getPassword(), TangCode.CODE_PASSWORD_EMPTY_ERROR);
        if (webUser.getEmail() != null) {
            ExceptionUtil.notMatch(webUser.getEmail(), "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", TangCode.EMAIL_NOT_MATCH_ERROR);
        }
        ExceptionUtil.notBlank(webUser.getPhoneNumber(), TangCode.BLANK_PHONE_NUMBER_EMPTY_ERROR);
        ExceptionUtil.notInRange(webUser.getPassword(), 6, 16, TangCode.PASSWORD_NOT_IN_RANGE_ERROR);
        return webUserService.saveWebUser(webUser);
    }

    /**
     * 修改用户
     *
     * @param webUser 用户信息
     * @return void
     */
    @PreAuthorize("hasAuthority('user:Update')")
    @RequestMapping("/update")
    public Result updateWebUser(WebUser webUser) {
        log.info("smart-home-web->UserController->begin update webUser");
        return webUserService.updateWebUser(webUser);
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('user:Delete')")
    @RequestMapping("/delete")
    public Result deleteWebUser(Long id) {
        log.info("smart-home-web->UserController->begin delete webUser");
        return webUserService.deleteWebUser(id);
    }

    /**
     * 查询用户列表
     *
     * @param webUserDto 查询条件
     * @return
     */

    @PreAuthorize("hasAuthority('UserManagement')")
    @RequestMapping("/query/list")
    public Result queryWebUserList(WebUserDto webUserDto) {
        log.error(webUserDto.toString());
        log.info("smart-home-web->UserController->begin query webUser list");
        return webUserService.queryWebUserList(webUserDto);
    }

    /***
     * 根据userId权限拦截 后台控制权限
     * @return
     */
   // @RequestMapping("/query/by/user/id")
    public Result queryPermissionByUserId(Long userId) {
        log.info("smart-home-web->UserController->begin query back permission for webUser");
        return webUserService.getPerm(userId);
    }

    /**
     * 分配角色
     *
     * @param allocationRoleDto 用户角色信息
     * @return
     */
//    @RequestMapping("/allocation/role")
//    public Result allocationWebUserRole(AllocationRoleDto allocationRoleDto) {
//        log.info("smart-home-web->UserController->begin allocation role for webUser");
//        return  webUserService.allocationWebUserRole(allocationRoleDto);
//    }

}
