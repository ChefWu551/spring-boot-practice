package com.mountain.spring.boot.yaml.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
/**
 * 标志当前类为配置类
 */
@Configuration
public class MyAppConfig {

    @Bean
    public DataSource initDataSource() {
        return null;
    }
}
