package com.example.config;

import com.example.security.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public class TestSecurityUtils {

    public static void setMockAuthentication(String username, String role) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                username,
                null,
                List.of(new SimpleGrantedAuthority(role.startsWith("ROLE_") ? role : "ROLE_" + role))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    public static void clearMockAuthentication() {
        SecurityContextHolder.clearContext();
    }

    public static String generateTestToken(JwtUtil jwtUtil, String email, String role, String accountType) {
        return generateTestToken(jwtUtil, email, role, "test-session-id", accountType);
    }

    public static String generateTestToken(JwtUtil jwtUtil, String email, String role, String sessionId, String accountType) {
        return jwtUtil.generateToken(
                email,
                List.of(role.startsWith("ROLE_") ? role : "ROLE_" + role),
                sessionId,
                accountType
        );
    }
}
