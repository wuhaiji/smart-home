package com.ecoeler.app.controller;


import com.ecoeler.app.entity.SceneAction;
import com.ecoeler.feign.SceneActionService;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.model.response.Result;
import com.ecoeler.util.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 场景动作
 * @author tang
 * @since 2020/9/22
 */
@RequestMapping("/scene-action")
@RestController
public class SceneActionController {

    @Autowired
    private SceneActionService sceneActionService;

    @RequestMapping("/add")
    public Result add(SceneAction sceneAction){
        ExceptionUtil.notBlank(sceneAction.getDeviceId(), TangCode.CODE_DEVICE_ID_EMPTY_ERROR);
        ExceptionUtil.notNull(sceneAction.getSceneId(),TangCode.CODE_SCENE_ID_NULL_ERROR);
        ExceptionUtil.notBlank(sceneAction.getProductId(),TangCode.CODE_PRODUCT_ID_EMPTY_ERROR);
        ExceptionUtil.notBlank(sceneAction.getDataKey(),TangCode.CODE_DATA_KEY_EMPTY_ERROR);
        ExceptionUtil.notBlank(sceneAction.getDataValue(),TangCode.CODE_DATA_VALUE_EMPTY_ERROR);
        return sceneActionService.add(sceneAction);
    }

    @RequestMapping("/list")
    public Result list(Long sceneId){
        ExceptionUtil.notNull(sceneId,TangCode.CODE_SCENE_ID_NULL_ERROR);
        return sceneActionService.list(sceneId);
    }

    @RequestMapping("/delete")
    public Result delete(Long id){
        ExceptionUtil.notNull(id,TangCode.CODE_SCENE_ID_NULL_ERROR);
        return sceneActionService.delete(id);
    }

}
