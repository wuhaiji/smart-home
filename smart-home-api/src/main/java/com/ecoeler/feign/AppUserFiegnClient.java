package com.ecoeler.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "blbs-service-control", path = "/big/screen/v1")
public interface AppUserFiegnClient {
}
