package com.ecoeler.feign;

import com.ecoeler.app.entity.SceneAction;
import com.ecoeler.model.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 场景动作服务
 * @author tang
 * @since 2020/9/18
 */
@FeignClient(value = "smart-home-service", path = "/scene-action",contextId = "scene-action")
public interface SceneActionService {

    @PostMapping("/add")
    Result<Long> add(@RequestBody SceneAction sceneAction);

    @PostMapping("/list")
    Result<List<SceneAction>> list(@RequestParam Long sceneId);

    @PostMapping("/delete")
    Result delete(@RequestParam Long id);

}
