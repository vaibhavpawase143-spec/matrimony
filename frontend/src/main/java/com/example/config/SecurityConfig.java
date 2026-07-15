package com.example.config;

import com.example.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final SecurityUserDetailsService securityUserDetailsService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(jwtUtil);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .authorizeHttpRequests(auth -> auth

                        // PUBLIC
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/users/login",
                                "/api/users/register",
                                "/api/users/verify",
                                "/api/users/resend-verification",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api/image/**"

                        ).permitAll()

                        // ADMIN
                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")

                        // USER + ADMIN
                        .requestMatchers("/api/users/**")
                        .hasAnyRole("USER", "ADMIN")

                        // EVERYTHING ELSE
                        .anyRequest().authenticated()
                )

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

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