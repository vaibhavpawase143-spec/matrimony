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

        // =====================================================
        // ALLOWED ORIGINS
        // =====================================================

        configuration.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:3000",
                "http://127.0.0.1:3000",
                "http://localhost:5173",

                // LAN Access (Current)
                "http://192.168.75.1:3000",

                // Any local LAN IP (Development)
                "http://192.168.*:*",

                // Production
                "https://yourdomain.com",
                "https://www.yourdomain.com"
        ));

        // =====================================================
        // ALLOWED METHODS
        // =====================================================

        configuration.setAllowedMethods(Arrays.asList(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));

        // =====================================================
        // ALLOWED HEADERS
        // =====================================================

        configuration.setAllowedHeaders(Arrays.asList("*"));

        // =====================================================
        // EXPOSED HEADERS
        // =====================================================

        configuration.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Disposition"
        ));

        // =====================================================
        // CREDENTIALS
        // =====================================================

        configuration.setAllowCredentials(true);

        // =====================================================
        // PREFLIGHT CACHE
        // =====================================================

        configuration.setMaxAge(3600L);

        // =====================================================
        // REGISTER CONFIGURATION
        // =====================================================

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}