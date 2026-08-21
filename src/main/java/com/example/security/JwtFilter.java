package com.example.security;

import com.example.model.Admin;
import com.example.model.User;
import com.example.repository.AdminRepository;
import com.example.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final SecurityUserDetailsService securityUserDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getServletPath();

        return path.startsWith("/api/auth/")
                || path.startsWith("/api/users/login")
                || path.startsWith("/api/users/register")
                || path.startsWith("/api/admins/login")
                || path.startsWith("/api/admins/refresh")

                // WEBSITE VISITOR ANALYTICS
                || path.equals("/api/analytics/visitor")

                || path.startsWith("/api/image/")
                || path.equals("/api/admins/logout")
                || path.startsWith("/api/kundli/")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/images/")
                || path.startsWith("/ws");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            String accountType = jwtUtil.extractAccountType(token);

            if (username != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                if (!jwtUtil.isValid(token, username)) {
                    response.sendError(
                            HttpServletResponse.SC_UNAUTHORIZED,
                            "Invalid token"
                    );
                    return;
                }

                long jwtStart = System.currentTimeMillis();

                UserDetails userDetails =
                        securityUserDetailsService.loadUserByUsername(username);

                System.out.println(
                        "JWT FILTER = "
                                + (System.currentTimeMillis() - jwtStart)
                                + " ms"
                );

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);
            }

        } catch (Exception e) {

            request.setAttribute(
                    "AUTH_ERROR",
                    "Invalid or expired token"
            );

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            return;
        }

        filterChain.doFilter(request, response);
    }
}