package com.mountain.spring.boot.yaml;

import com.mountain.spring.boot.yaml.config.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBootYamlApplicationTests {

	@Autowired
	Student student;

	@Test
	void contextLoads() {
		System.out.println(student.toString());
	}

}
