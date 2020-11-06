package com.mountain.spring.boot.yaml.controller;

import com.mountain.spring.boot.yaml.config.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {

    @Autowired
    Student student;

    @RequestMapping("world")
    public String helloWorld() {
        return "hello " + student.getName();
    }
}
