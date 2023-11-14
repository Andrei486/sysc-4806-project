package org.group23.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        // Use a simple controller for the login page
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/login").setViewName("login");
    }

}