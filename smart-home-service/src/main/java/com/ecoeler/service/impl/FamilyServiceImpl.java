package com.ecoeler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.entity.Family;
import com.ecoeler.app.entity.UserFamily;
import com.ecoeler.app.mapper.FamilyMapper;
import com.ecoeler.app.mapper.UserFamilyMapper;
import com.ecoeler.app.service.IFamilyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
@Service
public class FamilyServiceImpl extends ServiceImpl<FamilyMapper, Family> implements IFamilyService {

    @Autowired
    private UserFamilyMapper userFamilyMapper;

    @Override
    public List<Family> listUserFamily(Long userId) {
        QueryWrapper<UserFamily> q1=new QueryWrapper<>();
        q1.eq("app_user_id",userId);

        List<Long> familyIds = userFamilyMapper.selectList(q1).stream().map(UserFamily::getFamilyId).collect(Collectors.toList());
        if(familyIds.size()==0){ return new ArrayList<>(); }

        QueryWrapper<Family> q2=new QueryWrapper<>();
        q2.in("id", familyIds);
        return baseMapper.selectList(q2);

    }
}
