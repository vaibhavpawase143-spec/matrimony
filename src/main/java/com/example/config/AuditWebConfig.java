package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring Web MVC Configuration for audit interceptor registration.
 * Registers the SecurityAuditInterceptor to intercept all HTTP requests
 * and automatically set up audit context from security information.
 */
@Configuration
public class AuditWebConfig implements WebMvcConfigurer {

    @Autowired
    private SecurityAuditInterceptor securityAuditInterceptor;

    /**
     * Register the SecurityAuditInterceptor.
     * This interceptor will be applied to all requests before they are processed.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(securityAuditInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/static/**", "/css/**", "/js/**", "/images/**");
    }
}

