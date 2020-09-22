package com.ecoeler.feign;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecoeler.app.entity.Scene;
import com.ecoeler.model.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 场景服务
 * @author tang
 * @since 2020/9/18
 */
@FeignClient(value = "smart-home-service", path = "/scene",contextId = "scene")
public interface SceneService {

    @PostMapping("/add")
    Result<Long> add(@RequestBody Scene scene);

    @PostMapping("/update")
    Result update(@RequestBody Scene scene);

    @PostMapping("/list")
    Result<List<Scene>> list(@RequestParam Long familyId);

    @PostMapping("/delete")
    Result delete(@RequestParam Long id);

    @PostMapping("/trigger")
    Result trigger(@RequestParam Long id);
}
