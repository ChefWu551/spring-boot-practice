package com.mountain.webpractice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

@Configuration
public class MyViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        return null;
    }

    @Bean(value = "myViewResolverPlus")
    public ViewResolver myViewResolver(){
        return new MyViewResolver();
    }
}
