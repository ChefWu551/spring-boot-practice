package com.mountain;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// 优先加载serviceA的配置信息，然后在加载后面的信息
@NacosPropertySource(dataId = "serviceA", first = true, groupId = "test", autoRefreshed = true)
@NacosPropertySource(dataId = "serviceAA", before = "serviceA", groupId = "test", autoRefreshed = true)
public class NacosConfigApp {
    public static void main(String[] args) {
        SpringApplication.run(NacosConfigApp.class, args);
    }
}
