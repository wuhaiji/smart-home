package com.ecoeler.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecoeler.app.entity.SceneAction;
import com.ecoeler.app.service.ISceneActionService;
import com.ecoeler.model.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 场景动作
 * @author tang
 * @since 2020/9/22
 */
@RestController
@RequestMapping("/scene-action")
public class SceneActionController {

    @Autowired
    private ISceneActionService sceneActionService;

    @PostMapping("/add")
    public Result add(@RequestBody SceneAction sceneAction){
        return Result.ok(sceneActionService.addSceneAction(sceneAction));
    }

    @PostMapping("/list")
    public Result list(@RequestParam Long sceneId){
        QueryWrapper<SceneAction> q=new QueryWrapper<>();
        q.eq("scene_id",sceneId);
        return Result.ok(sceneActionService.list(q));
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Long id){
        sceneActionService.removeById(id);
        return Result.ok();
    }

}
