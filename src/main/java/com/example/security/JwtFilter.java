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
                || path.startsWith("/api/image/")
                || path.equals("/api/admins/logout")
                || path.startsWith("/api/kundli/")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/ws");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            String tokenSessionId = jwtUtil.extractSessionId(token);
            String accountType = jwtUtil.extractAccountType(token);

            if (username != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                if (!jwtUtil.isValid(token, username)) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                    return;
                }

                String dbSessionId = null;

                if ("ADMIN".equals(accountType)) {

                    Admin admin = adminRepository
                            .findByEmailIgnoreCase(username)
                            .orElseThrow(() ->
                                    new RuntimeException("Admin not found"));

                    dbSessionId = admin.getSessionId();

                } else if ("USER".equals(accountType)) {

                    User user = userRepository
                            .findByEmail(username)
                            .orElseThrow(() ->
                                    new RuntimeException("User not found"));

                    dbSessionId = user.getSessionId();

                } else {

                    response.sendError(
                            HttpServletResponse.SC_UNAUTHORIZED,
                            "Invalid account type."
                    );
                    return;
                }                if (dbSessionId == null || !dbSessionId.equals(tokenSessionId)) {
                    System.out.println("===== SESSION CHECK =====");
                    System.out.println("Username       : " + username);
                    System.out.println("Token Session  : " + tokenSessionId);
                    System.out.println("DB Session     : " + dbSessionId);
                    System.out.println("=========================");
                    response.sendError(
                            HttpServletResponse.SC_UNAUTHORIZED,
                            "Session expired. Please login again."
                    );
                    return;
                }

                UserDetails userDetails =
                        securityUserDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception e) {

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Invalid or expired token"
            );
            return;
        }

        filterChain.doFilter(request, response);
    }
}