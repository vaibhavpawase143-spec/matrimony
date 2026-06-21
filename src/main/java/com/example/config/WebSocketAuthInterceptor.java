package com.example.config;

import com.example.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) {

        try {

            String query = request.getURI().getQuery();

            if (query == null || !query.startsWith("token=")) {

                System.out.println("❌ WebSocket : Missing Token");

                return false;

            }

            String token = query.substring("token=".length());

            String username = jwtUtil.extractUsername(token);

            if (username == null || !jwtUtil.isValid(token, username)) {

                System.out.println("❌ WebSocket : Invalid Token");

                return false;

            }

            // Store username in session attributes
            attributes.put("username", username);

            System.out.println("✅ WebSocket Auth Success : " + username);

            return true;

        } catch (Exception e) {

            System.out.println("❌ WebSocket Handshake Error : " + e.getMessage());

            return false;

        }

    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception
    ) {

        // No implementation required

    }

}