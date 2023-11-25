package org.group23;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
public class SurveyMonkeyAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(SurveyMonkeyAppApplication.class, args);
    }
}
