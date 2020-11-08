package com.mountain.spring.boot.yaml;

import com.mountain.spring.boot.yaml.config.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class SpringBootYamlApplicationTests {

	@Autowired
	Student student;

	@Autowired
	ApplicationContext ioc;

	@Test
	void contextLoads() {
		System.out.println(student.toString());
	}

	@Test
	void beanInitTest() {
		System.out.println(ioc.containsBean("helloService"));
	}

}
