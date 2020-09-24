package com.ecoeler.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.app.entity.SceneAction;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tang
 * @since 2020-09-21
 */
public interface ISceneActionService extends IService<SceneAction> {

    /**
     * 添加场景动作
     * @param sceneAction
     * @return
     */
    Long addSceneAction(SceneAction sceneAction);

}
