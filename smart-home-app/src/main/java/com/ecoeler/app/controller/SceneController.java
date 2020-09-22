package com.ecoeler.app.controller;


import com.ecoeler.app.entity.Scene;
import com.ecoeler.feign.SceneService;
import com.ecoeler.model.code.TangCode;
import com.ecoeler.model.response.Result;
import com.ecoeler.util.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 场景
 * @author tang
 * @since 2020/9/22
 */
@RequestMapping("/scene")
@RestController
public class SceneController {

    @Autowired
    private SceneService sceneService;

    @RequestMapping("/add")
    public Result add(Scene scene){
        ExceptionUtil.notBlank(scene.getSceneName(), TangCode.CODE_SCENE_NAME_EMPTY_ERROR);
        ExceptionUtil.notNull(scene.getFamilyId(),TangCode.CODE_FAMILY_ID_NULL_ERROR);
        return sceneService.add(scene);
    }

    @RequestMapping("/update")
    public Result update(Scene scene){
        ExceptionUtil.notNull(scene.getId(),TangCode.CODE_SCENE_ID_NULL_ERROR);
        scene.setFamilyId(null);
        return sceneService.update(scene);
    }

    @RequestMapping("/list")
    public Result list(Long familyId){
        ExceptionUtil.notNull(familyId,TangCode.CODE_FAMILY_ID_NULL_ERROR);
        return sceneService.list(familyId);
    }

    @RequestMapping("/delete")
    public Result delete(Long id){
        ExceptionUtil.notNull(id,TangCode.CODE_SCENE_ID_NULL_ERROR);
        return sceneService.delete(id);
    }

    @RequestMapping("/trigger")
    public Result trigger(Long id){
        ExceptionUtil.notNull(id,TangCode.CODE_SCENE_ID_NULL_ERROR);
        return sceneService.trigger(id);
    }

}
