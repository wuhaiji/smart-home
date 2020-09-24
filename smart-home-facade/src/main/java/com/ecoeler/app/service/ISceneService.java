package com.ecoeler.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecoeler.app.entity.Scene;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tang
 * @since 2020-09-21
 */
public interface ISceneService extends IService<Scene> {

    /**
     * 删除场景
     * @param id
     */
    void deleteScene(Long id);

    /**
     * 触发场景
     * @param id
     */
    void triggerScene(Long id);


}
