package com.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Value("${app.cors.allowed-origins:}")
    private String configuredOrigins;

    /**
     * Returns the list of permitted origins/patterns for CORS and WebSocket connections.
     * Keeps local development origins safe while reading production origins from environment.
     */
    public List<String> getAllowedOrigins() {
        List<String> origins = new ArrayList<>(Arrays.asList(
                "http://localhost:3000",
                "http://127.0.0.1:3000",
                "http://localhost:5173",
                "http://127.0.0.1:5173"
        ));

        if (StringUtils.hasText(configuredOrigins)) {
            for (String origin : configuredOrigins.split(",")) {
                String trimmed = origin.trim();
                if (!trimmed.isEmpty() && !origins.contains(trimmed)) {
                    origins.add(trimmed);
                }
            }
        }
        return origins;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        // =====================================================
        // ALLOWED ORIGINS (Configurable + Localhost Dev)
        // =====================================================
        configuration.setAllowedOriginPatterns(getAllowedOrigins());

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