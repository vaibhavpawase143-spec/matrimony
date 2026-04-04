package com.example.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    // 🔥 IMPORTANT: this will now receive SecurityUserDetailsService (Admin + User)
    private final UserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getServletPath();

        return path.startsWith("/api/auth/")
                || path.startsWith("/api/admins/")
                || path.startsWith("/api/users/login")   // ✅ FIX
                || path.startsWith("/api/users/register")// ✅ FIX
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // ❌ No token → skip
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 🔥 Extract token
            String token = authHeader.substring(7);

            // 🔥 Extract username (email)
            String username = jwtUtil.extractUsername(token);

            if (username != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                // 🔥 Load from SecurityUserDetailsService (Admin + User)
                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(username);

                // 🔥 Validate token
                if (jwtUtil.isValid(token, username)) {

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);

                } else {
                    response.sendError(401, "Invalid token");
                    return;
                }
            }

        } catch (Exception e) {
            response.sendError(401, "Invalid or expired token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}