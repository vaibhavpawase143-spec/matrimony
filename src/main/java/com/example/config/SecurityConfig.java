package com.example.config;

import com.example.security.JwtFilter;
import com.example.security.JwtUtil;
import com.example.security.CustomAuthenticationEntryPoint;
import com.example.security.CustomAccessDeniedHandler;
import com.example.security.SecurityUserDetailsService;

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

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final SecurityUserDetailsService securityUserDetailsService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    // ================= AUTH MANAGER =================
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // ================= JWT FILTER =================
    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(jwtUtil);
    }

    // ================= SECURITY CONFIG =================
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})

                .authorizeHttpRequests(auth -> auth

                        // 🔥 WEBSOCKET
                        .requestMatchers("/ws/**").permitAll()

                        // 🔥 IMAGE APIs (IMPORTANT FIX ✅)
                        .requestMatchers("/api/image/**").permitAll()

                        // 🔓 PUBLIC APIs
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/users/login",
                                "/api/users/register",
                                "/api/admins/login",
                                "/api/admins/refresh",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // 👑 ADMIN ONLY
                        .requestMatchers("/api/admin/**")
                        .hasAnyRole("ADMIN", "SUPER_ADMIN")

                        // 🔥 MASTER TABLE (ADMIN ONLY)
                        .requestMatchers("/api/master/**")
                        .hasAnyRole("ADMIN", "SUPER_ADMIN")

                        // 👤 USER FEATURES (LOGIN REQUIRED)
                        .requestMatchers("/api/chat/**").authenticated()
                        .requestMatchers("/api/user/**").authenticated()

                        // 🔐 EVERYTHING ELSE
                        .anyRequest().authenticated()
                )

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 🔥 JWT FILTER
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ================= PASSWORD ENCODER =================
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}