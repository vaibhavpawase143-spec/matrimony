package com.example.config;

import com.example.repository.UserRepository;
import com.example.service.OnlineUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final UserRepository userRepository;
    private final OnlineUserService onlineUserService;
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    @Transactional
    public void handleConnect(SessionConnectedEvent event) {

        StompHeaderAccessor accessor =
                StompHeaderAccessor.wrap(event.getMessage());

        if (accessor.getUser() == null) {
            return;
        }

        String email = accessor.getUser().getName();

        onlineUserService.userOnline(email);

        userRepository.updateUserStatus(
                email,
                true,
                null
        );

        messagingTemplate.convertAndSend(
                "/topic/status",
                Map.of(
                        "email", email,
                        "online", true,
                        "lastSeen", ""
                )
        );

        System.out.println("🟢 ONLINE : " + email);
    }

    @EventListener
    @Transactional
    public void handleDisconnect(SessionDisconnectEvent event) {

        StompHeaderAccessor accessor =
                StompHeaderAccessor.wrap(event.getMessage());

        if (accessor.getUser() == null) {
            return;
        }

        String email = accessor.getUser().getName();

        LocalDateTime now = LocalDateTime.now();

        onlineUserService.userOffline(email);

        userRepository.updateUserStatus(
                email,
                false,
                now
        );

        messagingTemplate.convertAndSend(
                "/topic/status",
                Map.of(
                        "email", email,
                        "online", false,
                        "lastSeen", now.toString()
                )
        );

        System.out.println("🔴 OFFLINE : " + email);
    }

}