package com.example.config;

import com.example.repository.AdminRepository;
import com.example.repository.UserRepository;
import com.example.security.CustomAccessDeniedHandler;
import com.example.security.CustomAuthenticationEntryPoint;
import com.example.security.JwtFilter;
import com.example.security.JwtUtil;
import com.example.security.SecurityUserDetailsService;
import com.example.security.TokenRevocationService;
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
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
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
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final TokenRevocationService tokenRevocationService;

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(
                jwtUtil,
                userRepository,
                adminRepository,
                securityUserDetailsService,
                tokenRevocationService
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                // =====================================================
                // SECURITY HEADERS (CSP, Referrer, Permissions, HSTS, Frame)
                // =====================================================
                .headers(headers -> headers
                        .frameOptions(frame -> frame.deny())
                        .contentTypeOptions(contentType -> {})
                        .httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .maxAgeInSeconds(31536000)
                        )
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'self'; script-src 'self'; style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; font-src 'self' data: https://fonts.gstatic.com; img-src 'self' data: blob: https:; connect-src 'self' ws: wss: http: https:; frame-ancestors 'self';")
                        )
                        .referrerPolicy(referrer -> referrer
                                .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                        )
                        .permissionsPolicy(permissions -> permissions
                                .policy("camera=(), microphone=(), geolocation=()")
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // PUBLIC AUTH & MONITORING
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/register",
                                "/api/auth/refresh",
                                "/api/auth/verify",
                                "/api/auth/resend-verification",
                                "/api/auth/send-verification",
                                "/api/auth/forgot-password",
                                "/api/auth/reset-password",
                                "/api/auth/send-otp",
                                "/api/auth/verify-otp",
                                "/api/auth/otp/**",
                                "/api/users/login",
                                "/api/users/register",
                                "/api/users/verify",
                                "/api/users/resend-verification",
                                "/api/users/send-otp",
                                "/api/users/verify-otp",
                                "/api/admins/login",
                                "/api/admins/refresh",
                                "/actuator/health"
                        ).permitAll()

                        // PUBLIC READ-ONLY MASTER DATA & ASSETS
                        .requestMatchers(org.springframework.http.HttpMethod.GET,
                                "/api/master/**",
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
                                "/api/fields-of-study/**",
                                "/api/complexions/**",
                                "/api/body-types/**",
                                "/api/genders/**",
                                "/api/countries/**",
                                "/api/subscription-plans/**",
                                "/api/support-categories/**",
                                "/api/cms/**",
                                "/api/faqs/**",
                                "/api/success-stories/**",
                                "/api/image/**",
                                "/images/**",
                                "/uploads/**"
                        ).permitAll()

                        // WEBSOCKET
                        .requestMatchers("/ws/**").permitAll()

                        // SWAGGER & ACTUATOR (ADMIN ONLY IN PRODUCTION)
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/actuator/**"
                        ).hasAnyRole("ADMIN", "SUPER_ADMIN")

                        // ADMIN ONLY
                        .requestMatchers("/api/admin/**", "/api/admins/**")
                        .hasAnyRole("ADMIN", "SUPER_ADMIN")

                        // USER + ADMIN PROTECTED ACTIONS
                        .requestMatchers(
                                "/api/users/**",
                                "/api/photos/**",
                                "/api/reports/**",
                                "/api/blocks/**",
                                "/api/interests/**"
                        ).hasAnyRole("USER", "ADMIN")

                        .anyRequest()
                        .authenticated()
                )

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .addFilterBefore(
                        jwtFilter(),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}