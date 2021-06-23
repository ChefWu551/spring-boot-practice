package com.mountain.webpractice.controller;

import com.mountain.webpractice.config.HelloController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@HelloController
@RestController
@Controller
public class Hello{

    @RequestMapping("world")
    public String helloWorld() {
        return "hello world";
    }

    @RequestMapping("attribute")
    public void attributeGet(@RequestAttribute String condition) {
        System.out.println(condition);
    }
}
