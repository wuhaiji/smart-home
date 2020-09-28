package com.ecoeler.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecoeler.app.dto.v1.UserFamilyDto;
import com.ecoeler.app.entity.AppUser;
import com.ecoeler.app.service.IAppUserService;
import com.ecoeler.app.service.IEmailCodeService;
import com.ecoeler.common.NullContentJudge;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.model.code.WJHCode;
import com.ecoeler.model.response.Result;
import com.ecoeler.util.ExceptionUtil;
import com.ecoeler.utils.HutoolCaptchaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * APP用户控制器
 * @author tang
 * @since 2020/9/10
 */
@Slf4j
@RequestMapping("/app-user")
@RestController
public class AppUserController {

    @Autowired
    private IAppUserService appUserService;

    @Autowired
    private HutoolCaptchaUtil hutoolCaptchaUtil;

    @Autowired
    private IEmailCodeService emailCodeService;

    /**
     * 图片验证码
     * @param email
     * @return
     */
    @PostMapping("/captcha")
    public Result captcha(@RequestParam String email) {
        return Result.ok(hutoolCaptchaUtil.getCaptchaImage(email));
    }

    /**
     * 图形验证码验证
     * @param email
     * @param code
     * @return
     */
    @PostMapping("/verify")
    public Result verify(@RequestParam String email, @RequestParam String code){
        if(hutoolCaptchaUtil.verify(email,code)){
            return Result.ok();
        }
        return Result.error(TangCode.CODE_CAPTCHA_ERROR);
    }


    /**
     * 注册
     * @param appUser
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody AppUser appUser, @RequestParam String emailCode){
        if(!emailCodeService.verify(appUser.getEmail(),emailCode)){
            throw new ServiceException(TangCode.CODE_EMAIL_CODE_ERROR);
        }
        return Result.ok(appUserService.createUser(appUser));
    }


    /**
     * 获得邮箱验证码
     * @param email
     * @return
     */
    @PostMapping("/email_code")
    public Result emailCode(@RequestParam String email,@RequestParam String ip){
        emailCodeService.filter(ip);
        emailCodeService.sendCode(email);
        return Result.ok();
    }


    /**
     * 根据邮箱获得APP用户信息
     * @param email
     * @return
     */
    @PostMapping("/user")
    public Result user(@RequestParam String email){
        QueryWrapper<AppUser> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("email",email);
        return Result.ok(appUserService.getOne(queryWrapper));
    }

    /**
     * 用户离开家庭（指定其他用户为家庭拥有者）
     * @author wujihong
     * @param
     * @since 17:34 2020-09-27
     */
    @RequestMapping("/leave/family")
    public Result leaveFamily(@RequestBody UserFamilyDto userFamilyDto) {
        ExceptionUtil.notNull(NullContentJudge.isNullContent(UserFamilyDto.class, userFamilyDto), WJHCode.PARAM_EMPTY_ERROR);
        ExceptionUtil.notNull(userFamilyDto.getFamilyId(), WJHCode.FAMILY_ID_EMPTY_ERROR);
        ExceptionUtil.notNull(userFamilyDto.getAppUserId(), WJHCode.APP_USER_ID_EMPTY_ERROR);
        return Result.ok(appUserService.leaveFamily(userFamilyDto));
    }

    /**
     * 用户解散家庭（删除家庭）
     * @author wujihong
     * @param
     * @since 17:37 2020-09-27
     */
    @RequestMapping("/dissolve/family")
    public Result dissolveFamily(@RequestBody UserFamilyDto userFamilyDto) {
        ExceptionUtil.notNull(NullContentJudge.isNullContent(UserFamilyDto.class, userFamilyDto), WJHCode.PARAM_EMPTY_ERROR);
        ExceptionUtil.notNull(userFamilyDto.getFamilyId(), WJHCode.FAMILY_ID_EMPTY_ERROR);
        ExceptionUtil.notNull(userFamilyDto.getAppUserId(), WJHCode.APP_USER_ID_EMPTY_ERROR);
        return Result.ok(appUserService.dissolveFamily(userFamilyDto));
    }

}
