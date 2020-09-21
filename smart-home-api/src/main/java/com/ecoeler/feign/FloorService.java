package com.ecoeler.feign;


import com.ecoeler.app.entity.Floor;
import com.ecoeler.model.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "smart-home-service", path = "/floor",contextId = "floor")
public interface FloorService {

    @PostMapping("/add/floor")
    Result<Long> addFloor(@RequestBody Floor floor);

    @PostMapping("/list/family/floor")
    Result<List<Floor>> listFamilyFloor(@RequestParam Long familyId);

    @PostMapping("/update/floor")
    Result updateFloor(@RequestBody Floor floor);
}
