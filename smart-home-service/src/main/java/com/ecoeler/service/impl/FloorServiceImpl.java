package com.ecoeler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.entity.Family;
import com.ecoeler.app.entity.Floor;
import com.ecoeler.app.mapper.FamilyMapper;
import com.ecoeler.app.mapper.FloorMapper;
import com.ecoeler.app.service.IFloorService;
import com.ecoeler.constant.FamilyTypeConst;
import com.ecoeler.exception.ServiceException;
import com.ecoeler.model.code.TangCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tang
 * @since 2020-09-10
 */
@Service
public class FloorServiceImpl extends ServiceImpl<FloorMapper, Floor> implements IFloorService {

    @Autowired
    private FamilyMapper familyMapper;

    @Override
    public List<Floor> listFamilyFloor(Long familyId) {

        Family family = familyMapper.selectById(familyId);

        if(FamilyTypeConst.VILLA !=family.getFamilyType()){
            throw new ServiceException(TangCode.CODE_FAMILY_NOT_VILLA);
        }

        QueryWrapper<Floor> q=new QueryWrapper<>();
        q.eq("family_id",family.getId());
        return baseMapper.selectList(q);

    }
}
