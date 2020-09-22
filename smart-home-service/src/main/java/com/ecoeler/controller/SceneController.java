package com.ecoeler.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecoeler.app.entity.Scene;
import com.ecoeler.app.service.ISceneService;
import com.ecoeler.model.response.Result;
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
    private ISceneService sceneService;

    @PostMapping("/add")
    public Result add(@RequestBody Scene scene){
        sceneService.save(scene);
        return Result.ok(scene.getId());
    }

    @PostMapping("/update")
    public Result update(@RequestBody Scene scene){
        sceneService.updateById(scene);
        return Result.ok();
    }

    @PostMapping("/list")
    public Result list(@RequestParam Long familyId){
        QueryWrapper<Scene> q=new QueryWrapper<>();
        q.eq("family_id",familyId);
        return Result.ok(sceneService.list(q));
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Long id){
        sceneService.deleteScene(id);
        return Result.ok();
    }

    @PostMapping("/trigger")
    public Result trigger(@RequestParam Long id){
        sceneService.triggerScene(id);
        return Result.ok();
    }


}
