package com.example.security;

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
    private final TokenRevocationService tokenRevocationService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getServletPath();

        return path.startsWith("/api/auth/login")
                || path.startsWith("/api/auth/register")
                || path.startsWith("/api/auth/refresh")
                || path.startsWith("/api/auth/verify")
                || path.startsWith("/api/auth/send-otp")
                || path.startsWith("/api/auth/verify-otp")
                || path.startsWith("/api/users/login")
                || path.startsWith("/api/users/register")
                || path.startsWith("/api/admins/login")
                || path.startsWith("/api/admins/refresh")
                || path.startsWith("/api/image/")
                || path.startsWith("/api/kundli/")
                || path.startsWith("/images/")
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

            // 1. Check Redis-backed access token revocation
            if (tokenRevocationService.isRevoked(token)) {
                SecurityContextHolder.clearContext();
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has been revoked");
                return;
            }

            String username = jwtUtil.extractUsername(token);

            if (username != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                if (!jwtUtil.isValid(token, username)) {
                    SecurityContextHolder.clearContext();
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                    return;
                }

                UserDetails userDetails =
                        securityUserDetailsService.loadUserByUsername(username);

                if (!userDetails.isEnabled()) {
                    SecurityContextHolder.clearContext();
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Account disabled or deleted");
                    return;
                }

                // 2. Validate server-side session for Admin accounts
                String accountType = jwtUtil.extractAccountType(token);
                String tokenSessionId = jwtUtil.extractSessionId(token);

                if ("ADMIN".equalsIgnoreCase(accountType) || userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().startsWith("ROLE_ADMIN"))) {
                    com.example.model.Admin admin = adminRepository.findByEmailIgnoreCase(username).orElse(null);
                    if (admin != null) {
                        String currentSessionId = admin.getSessionId();
                        if (currentSessionId == null || !currentSessionId.equals(tokenSessionId)) {
                            SecurityContextHolder.clearContext();
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Admin session expired or invalid");
                            return;
                        }
                    }
                }

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
            SecurityContextHolder.clearContext();
            request.setAttribute(
                    "AUTH_ERROR",
                    "Invalid or expired token"
            );

            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }
}