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
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final SecurityUserDetailsService securityUserDetailsService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CorsConfigurationSource corsConfigurationSource;

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
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                .authorizeHttpRequests(auth -> auth

                        // PUBLIC
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/users/login",
                                "/api/users/register",
                                "/api/users/verify",
                                "/api/users/resend-verification",
                                "/api/users/send-otp",
                                "/api/users/verify-otp",
                                "/api/master/religions/**",
                                "/api/cities/**",
                                "/api/states/**",
                                "/api/occupations/**",
                                "/api/education-levels/**",
                                "/api/marital-statuses/**",
                                "/api/mother-tongues/**",
                                "/api/heights/**",
                                "/api/weights/**",
                                "/api/family-values/**",
                                "/api/family-types/**",
                                "/api/family-statuses/**",
                                "/api/employed/**",
                                "/api/incomes/**",
                                "/api/manglik-statuses/**",
                                "/api/interests/**",
                                "/api/image/**",
                                "/ws/**",
                                "/api/users/init-photo-directory",
                                "/api/user-photos/**",
                                "/api/reports/**",

                                "/api/support-categories/**",
                                "/api/blocks/**",
                                "/api/fields-of-study/**",
                                "/api/subscription-plans/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api/complexions/**",
                                "/api/body-types/**",
                                "/api/genders/**",
                                "/api/countries/**",
                                "/api/image/**",
                                "/ws/**",
                                "/uploads/**"

                        ).permitAll()

                        // ADMIN LOGIN
                        .requestMatchers("/api/admin/auth/login", "/api/admin/auth/refresh","/api/master/**",
                                "/api/admins/*/castes/**")
                        .permitAll()

                        // ADMIN
                        .requestMatchers("/api/admin/**", "/api/admins/**")
                        .hasAnyRole("ADMIN", "SUPER_ADMIN")

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
}

