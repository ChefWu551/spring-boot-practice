package com.mountain.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "service-provider")
public interface ProviderClient {

    @GetMapping("/provider/service")
    String service();
}
