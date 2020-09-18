package com.ecoeler.app.controller;


import com.ecoeler.app.entity.Family;
import com.ecoeler.app.utils.PrincipalUtil;
import com.ecoeler.feign.FamilyService;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.model.response.Result;
import com.ecoeler.util.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * 家庭
 * @author tang
 * @since 2020/9/18
 */
@RestController
@RequestMapping("/family")
public class FamilyController {

    @Autowired
    private FamilyService familyService;

    @PostMapping("/list/user/family")
    public Result listUserFamily(Principal principal){
        return familyService.listUserFamily(PrincipalUtil.getUserId(principal));
    }

    @PostMapping("/add/family")
    public Result addFamily( Family family, String nickname ,Principal principal){
        ExceptionUtil.notBlank(family.getFamilyName(), TangCode.CODE_FAMILY_NAME_EMPTY_ERROR);
        ExceptionUtil.notNull(family.getFamilyType(), TangCode.CODE_FAMILY_TYPE_NULL_ERROR);
        return familyService.addFamily(family, PrincipalUtil.getUserId(principal),nickname);
    }

}
