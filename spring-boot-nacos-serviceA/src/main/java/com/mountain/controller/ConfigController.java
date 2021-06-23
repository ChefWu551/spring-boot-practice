package com.mountain.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("config")
@ResponseBody
public class ConfigController {

    @NacosValue(value = "${useLocalCache:false}", autoRefreshed = true)
    private boolean useLocalCache;

    @Value("${server.port}")
    private int port;

    @Value("${serviceAA.value}")
    private String serviceAAValue;

    @Autowired
    ConfigurableApplicationContext applicationContext;

    @RequestMapping(value = "/get", method = GET)
    public boolean get() {
        System.out.println("端口号：" + port);

        String hotValue = applicationContext.getEnvironment().getProperty("hot.update.value");
        System.out.println("上下文获取热更新的配置中心的值：" + hotValue);
        System.out.println("serviceAA配置文件，多个配置文件的配置属性值：" + serviceAAValue);
        return useLocalCache;
    }
}
