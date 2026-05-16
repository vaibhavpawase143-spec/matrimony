package com.example.config;

import com.example.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.*;
import org.springframework.web.socket.*;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {

        String authHeader = request.getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ Missing Authorization header");
            return false; // ❌ reject connection
        }

        String token = authHeader.substring(7);

        try {
            String username = jwtUtil.extractUsername(token);

            if (!jwtUtil.isValid(token, username)) {
                System.out.println("❌ Invalid JWT token");
                return false;
            }

            // ✅ store user info
            attributes.put("username", username);

            System.out.println("✅ WebSocket Auth Success: " + username);

        } catch (Exception e) {
            System.out.println("❌ JWT Error: " + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
    }
}