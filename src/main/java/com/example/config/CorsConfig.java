package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allow specific origins (add your React app URLs)
        configuration.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:3000",    // React dev server
            "http://localhost:3001",    // Alternative React port
            "http://127.0.0.1:3000",   // Alternative localhost
            "http://localhost:5173",    // Vite dev server
            "http://localhost:8080",    // Alternative port
            "https://yourdomain.com",   // Production domain (replace with your actual domain)
            "https://www.yourdomain.com" // Production www domain
        ));

        // Allow all HTTP methods
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));

        // Allow all headers
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Allow credentials (important for JWT authentication)
        configuration.setAllowCredentials(true);

        // How long the browser can cache preflight response
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
