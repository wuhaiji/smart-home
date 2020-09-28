package com.ecoeler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.dto.v1.FamilyDto;
import com.ecoeler.app.dto.v1.FloorDto;
import com.ecoeler.app.dto.v1.RoomDto;
import com.ecoeler.app.dto.v1.UserFamilyDto;
import com.ecoeler.app.entity.Family;
import com.ecoeler.app.entity.Scene;
import com.ecoeler.app.entity.TimerJob;
import com.ecoeler.app.entity.UserFamily;
import com.ecoeler.app.mapper.FamilyMapper;
import com.ecoeler.app.mapper.SceneMapper;
import com.ecoeler.app.mapper.TimerJobMapper;
import com.ecoeler.app.mapper.UserFamilyMapper;
import com.ecoeler.app.service.*;
import com.ecoeler.constant.FamilyRoleConst;
import com.ecoeler.constant.FamilyTypeConst;
import com.ecoeler.observer.FamilyEventObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private FamilyMapper familyMapper;

    @Autowired
    private List<FamilyEventObserver> familyEventObserverList;

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

    @Override
    public Long addFamily(Family family, Long userId ,String nickname) {
        baseMapper.insert(family);
        UserFamily userFamily=new UserFamily();
        userFamily.setRole(FamilyRoleConst.OWNER);
        userFamily.setAppUserId(userId);
        userFamily.setNickName(nickname);
        userFamily.setFamilyId(family.getId());
        userFamilyMapper.insert(userFamily);
        return family.getId();
    }

//    @Override
//    public Boolean removeFamily(Long id) {
//        Boolean result = false;
//        List<Long> sceneIdList;
//        List<Long> timerJobIdList;
//        QueryWrapper<Family> familyQueryWrapper = new QueryWrapper<>();
//        QueryWrapper<Scene> sceneQueryWrapper = new QueryWrapper<>();
//        QueryWrapper<TimerJob> timerJobQueryWrapper = new QueryWrapper<>();
//
//        // 1.查询设备类型（0别墅，1住宅）
//        familyQueryWrapper.eq("id", id);
//        Family family = familyMapper.selectOne(familyQueryWrapper);
//        // 2.删除家庭下的相关数据（空间位置、重置设备）
//        // 当家庭为普通住宅时，删除房间和软删除房间下的设备
//        if (family.getFamilyType() == FamilyTypeConst.HOUSE) {
//            roomService.removeRoom(new RoomDto().setFamilyId(id).setRemoveFamilyBool(true));
//        } else {
//            // 当家庭为别墅住宅时，删除楼层、房间和软删除房间下的设备
//            floorService.removeFloor(new FloorDto().setFamilyId(id).setRemoveFamilyBool(true));
//        }
//
//        // 3.删除场景
//        sceneQueryWrapper.eq("family_id", id);
//        sceneQueryWrapper.select("id");
//        sceneIdList = sceneMapper.selectList(sceneQueryWrapper).stream().map(Scene::getId).collect(Collectors.toList());
//        for (Long sceneId: sceneIdList) {
//                sceneService.deleteScene(sceneId);
//        }
//
//        // 4.删除定时任务
//        timerJobQueryWrapper.eq("family_id", id);
//        timerJobQueryWrapper.select("id");
//        timerJobIdList = timerJobMapper.selectList(timerJobQueryWrapper).stream().map(TimerJob::getId).collect(Collectors.toList());
//        for (Long timerJobId: timerJobIdList) {
//            timerJobService.deleteJob(timerJobId);
//        }
//
//        // 5.删除家庭
//        if (familyMapper.deleteById(id) > 0) {
//            result = true;
//        }
//
//        return result;
//    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeFamily(Long id) {
        Boolean result = false;
        for (FamilyEventObserver familyEventObserver : familyEventObserverList) {
            familyEventObserver.whenUserDeleteFamily(new UserFamilyDto().setFamilyId(id));
        }
        // 最后删除家庭
        if (familyMapper.deleteById(id) > 0) {
            result = true;
        }
        return result;
    }
}
