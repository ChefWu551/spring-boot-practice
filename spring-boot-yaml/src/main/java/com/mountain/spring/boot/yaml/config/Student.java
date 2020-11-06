package com.mountain.spring.boot.yaml.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 将配置文件中配置的每一个属性值，映射到这个组件中
 * @ConfigurationProperties: 告诉springBoot将本类的所有属性和配置文件中相关的配置进行绑定
 *  prefix 将类映射成对应属性名，若不添加则默认类名
 *
 *  @Component： 声明该配置是springboot的一个组件
 */
@Data
@Component
@ConfigurationProperties(prefix = "student")
public class Student {

    private String name;

    private int age;

    private Map<String, String> achievement;

    private List<String> lesson;

    private Dog dog;

}
