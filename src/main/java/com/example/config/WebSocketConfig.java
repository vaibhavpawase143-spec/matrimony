package com.example.config;

import com.example.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtUtil jwtUtil;
    private final WebSocketUserInterceptor webSocketUserInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        // 🔥 Broker (topics + private queues)
        config.enableSimpleBroker("/topic", "/queue");

        // 🔥 For sending from client → server
        config.setApplicationDestinationPrefixes("/app");

        // 🔥 For sending to specific user (VERY IMPORTANT)
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint("/ws")
                .addInterceptors(new WebSocketAuthInterceptor(jwtUtil)) // JWT check
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {

        // 🔥 Attach user to WebSocket session
        registration.interceptors(webSocketUserInterceptor);
    }
}