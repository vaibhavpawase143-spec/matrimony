package com.example.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    // =========================
    // ✅ Skip public endpoints
    // =========================
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        return path.startsWith("/api/auth")
                || path.startsWith("/api/admins/login")   // 🔥 IMPORTANT (your login API)
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/swagger-ui.html");
    }

    // =========================
    // 🔥 MAIN FILTER LOGIC
    // =========================
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String authHeader = request.getHeader("Authorization");

            String token = null;
            String username = null;

            // ✅ Extract token
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }

            // ✅ Extract username/email
            if (token != null) {
                try {
                    username = jwtUtil.extractEmail(token); // or extractUsername()
                } catch (Exception e) {
                    // invalid token → ignore
                }
            }

            // ✅ Authenticate user
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // ✅ Validate token
                if (jwtUtil.isValid(token, userDetails.getUsername())) {

                    List<SimpleGrantedAuthority> authorities = Collections.emptyList();

                    // 🔥 Extract roles
                    try {
                        List<String> roles = jwtUtil.extractRoles(token);

                        if (roles != null && !roles.isEmpty()) {
                            authorities = roles.stream()
                                    .map(role -> "ROLE_" + role)
                                    .map(SimpleGrantedAuthority::new)
                                    .collect(Collectors.toList());
                        }
                    } catch (Exception e) {
                        // roles missing → ignore
                    }

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    authorities.isEmpty() ? userDetails.getAuthorities() : authorities
                            );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

        } catch (Exception e) {
            // ❗ Prevent crash
        }

        filterChain.doFilter(request, response);
    }
}