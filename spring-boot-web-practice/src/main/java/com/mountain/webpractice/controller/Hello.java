package com.mountain.webpractice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {

    @RequestMapping("world")
    public String helloWorld() {
        return "hello world";
    }
}
