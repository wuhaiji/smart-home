package com.ecoeler.observer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecoeler.app.dto.v1.FamilyDto;
import com.ecoeler.app.dto.v1.UserFamilyDto;
import com.ecoeler.app.entity.Scene;
import com.ecoeler.app.mapper.SceneMapper;
import com.ecoeler.app.service.ISceneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wujihong
 */
@Component
public class SceneFamilyEventObserver implements FamilyEventObserver {

    @Autowired
    private SceneMapper sceneMapper;

    @Autowired
    private ISceneService sceneService;

    /**
     * 当删除家庭时，场景需要做的删除操作
     * @author wujihong
     * @param userFamilyDto
     * @since 16:34 2020-09-28
     */
    @Override
    public void whenUserDeleteFamily(UserFamilyDto userFamilyDto) {
        QueryWrapper<Scene> sceneQueryWrapper = new QueryWrapper<>();
        List<Long> sceneIdList;
        // 删除场景
        sceneQueryWrapper.eq("family_id", userFamilyDto.getFamilyId());
        sceneQueryWrapper.select("id");
        sceneIdList = sceneMapper.selectList(sceneQueryWrapper).stream().map(Scene::getId).collect(Collectors.toList());
        for (Long sceneId: sceneIdList) {
            sceneService.deleteScene(sceneId);
        }
    }

    /**
     * 当用户离开家庭时，场景需要做的操作
     * @author wujihong
     * @param userFamilyDto
     * @since 16:28 2020-09-28
     */
    @Override
    public void whenUserLeaveFamily(UserFamilyDto userFamilyDto) {

    }
}
