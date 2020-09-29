package com.ecoeler.app.controller;


import com.ecoeler.app.dto.v1.UserFamilyDto;
import com.ecoeler.app.utils.PrincipalUtil;
import com.ecoeler.common.NullContentJudge;
import com.ecoeler.feign.AppUserService;
import com.ecoeler.model.code.WJHCode;
import com.ecoeler.model.response.Result;
import com.ecoeler.util.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * 用户
 * @author tang
 * @since 2020/9/17
 */
@RequestMapping("/app-user")
@RestController
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    @RequestMapping("/init")
    public Result init(Principal principal){

        return Result.ok();
    }

    /**
     * 用户离开家庭（指定其他用户为家庭拥有者）
     * @author wujihong
     * @param
     * @since 17:34 2020-09-27
     */
    @RequestMapping("/leave/family")
    public Result leaveFamily(UserFamilyDto userFamilyDto) {
        userFamilyDto.setAppUserId(PrincipalUtil.getUserId());
        ExceptionUtil.notNull(NullContentJudge.isNullContent(UserFamilyDto.class, userFamilyDto), WJHCode.PARAM_EMPTY_ERROR);
        ExceptionUtil.notNull(userFamilyDto.getFamilyId(), WJHCode.FAMILY_ID_EMPTY_ERROR);
        ExceptionUtil.notNull(userFamilyDto.getAppUserId() .equals( userFamilyDto.getNewAppUserOwnerId()), WJHCode.BAN_APPOINT_USER_IS_SELF_ERROR);
        return appUserService.leaveFamily(userFamilyDto);
    }

    /**
     * 用户解散家庭（删除家庭）
     * @author wujihong
     * @param
     * @since 17:37 2020-09-27
     */
    @RequestMapping("/dissolve/family")
    public Result dissolveFamily(UserFamilyDto userFamilyDto) {
        userFamilyDto.setAppUserId(PrincipalUtil.getUserId());
        ExceptionUtil.notNull(NullContentJudge.isNullContent(UserFamilyDto.class, userFamilyDto), WJHCode.PARAM_EMPTY_ERROR);
        ExceptionUtil.notNull(userFamilyDto.getFamilyId(), WJHCode.FAMILY_ID_EMPTY_ERROR);
        return appUserService.dissolveFamily(userFamilyDto);
    }

}
