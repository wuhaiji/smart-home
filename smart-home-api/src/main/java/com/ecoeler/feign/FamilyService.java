package com.ecoeler.feign;

import com.ecoeler.app.entity.Family;
import com.ecoeler.model.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "smart-home-service", path = "/family",contextId = "family")
public interface FamilyService {

    @PostMapping("/list/user/family")
    Result<List<Family>> listUserFamily(@RequestParam Long userId);

    @PostMapping("/add/family")
    Result<Long> addFamily(@RequestBody Family family, @RequestParam Long userId, @RequestParam String nickname);

    @PostMapping("/remove/family")
    Result removeFamily(@RequestParam Long id);
}
