package com.ecoeler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecoeler.app.entity.Scene;
import com.ecoeler.app.entity.SceneAction;
import com.ecoeler.app.mapper.SceneActionMapper;
import com.ecoeler.app.mapper.SceneMapper;
import com.ecoeler.app.service.ISceneService;
import com.ecoeler.core.scene.SceneExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tang
 * @since 2020-09-21
 */
@Service
public class SceneServiceImpl extends ServiceImpl<SceneMapper, Scene> implements ISceneService {

    @Autowired
    private SceneActionMapper sceneActionMapper;

    @Autowired
    private SceneExecutor sceneExecutor;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteScene(Long id) {
        QueryWrapper<SceneAction> q=new QueryWrapper<>();
        q.eq("scene_id",id);
        sceneActionMapper.delete(q);

        baseMapper.deleteById(id);
    }


    @Async
    @Override
    public void triggerScene(Long id) {
        //获得场景动作
        QueryWrapper<SceneAction> q=new QueryWrapper<>();
        q.eq("scene_id",id);
        //触发场景动作
        sceneExecutor.execute(sceneActionMapper.selectList(q));
    }
}
