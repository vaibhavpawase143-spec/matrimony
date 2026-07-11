package com.example.config;

import com.example.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtUtil jwtUtil;
    private final WebSocketUserInterceptor webSocketUserInterceptor;

    @Bean
    public ThreadPoolTaskScheduler websocketTaskScheduler() {

        ThreadPoolTaskScheduler scheduler =
                new ThreadPoolTaskScheduler();

        scheduler.setPoolSize(1);

        scheduler.setThreadNamePrefix("wss-heartbeat-");

        scheduler.initialize();

        return scheduler;

    }

    @Override
    public void configureMessageBroker(
            MessageBrokerRegistry registry
    ) {

        registry.enableSimpleBroker(
                        "/topic",
                        "/queue"
                )
                .setHeartbeatValue(
                        new long[]{10000,10000}
                )
                .setTaskScheduler(
                        websocketTaskScheduler()
                );

        registry.setApplicationDestinationPrefixes(
                "/app"
        );

        registry.setUserDestinationPrefix(
                "/user"
        );

    }

    @Override
    public void registerStompEndpoints(
            StompEndpointRegistry registry
    ) {

        registry.addEndpoint("/ws")

                .addInterceptors(
                        new WebSocketAuthInterceptor(
                                jwtUtil
                        )
                )

                .setAllowedOriginPatterns("*")

                .withSockJS()

                .setHeartbeatTime(10000)

                .setSessionCookieNeeded(false);

    }

    @Override
    public void configureClientInboundChannel(
            ChannelRegistration registration
    ) {

        registration.interceptors(
                webSocketUserInterceptor
        );

    }

}