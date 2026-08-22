package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String baseUploadPath = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;

        // Preserve chat media subdirectories while securing root photo uploads via ImageUploadController
        registry.addResourceHandler(
                        "/uploads/images/**",
                        "/uploads/audio/**",
                        "/uploads/documents/**",
                        "/uploads/videos/**"
                )
                .addResourceLocations(
                        "file:" + baseUploadPath + "images/",
                        "file:" + baseUploadPath + "audio/",
                        "file:" + baseUploadPath + "documents/",
                        "file:" + baseUploadPath + "videos/"
                );
    }
}