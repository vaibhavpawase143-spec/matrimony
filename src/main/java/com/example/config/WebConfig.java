package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String uploadPath = System.getProperty("user.dir") + "/static/uploads/";

        registry.addResourceHandler("/static/uploads/**")
                .addResourceLocations("file:" + uploadPath);
    }
}