package com.mountain.controller;

import com.mountain.client.ProviderClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("consumer")
@ResponseBody
public class ConsumerController {

    @Autowired
    ProviderClient providerClient;

    @GetMapping("service")
    public String service() {
        String providerResult = providerClient.service();
        return "consumer invoke | " + providerResult;
    }
}
