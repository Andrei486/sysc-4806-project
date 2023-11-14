package org.group23.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * Adds simple default controllers for specified paths and views.
     * @param registry the ViewControllerRegistry keeping track of controllers
     */
    public void addViewControllers(ViewControllerRegistry registry) {
        // Use a simple controller for the login and index page
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/login").setViewName("login");
    }

}