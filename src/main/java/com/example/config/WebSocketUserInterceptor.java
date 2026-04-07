package com.example.config;

import com.example.service.OnlineUserService;
import com.example.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class WebSocketUserInterceptor implements ChannelInterceptor {

    private final OnlineUserService onlineUserService;
    private final UserRepository userRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) return message;

        StompCommand command = accessor.getCommand();

        if (command == null) return message;

        // ================= CONNECT =================
        if (StompCommand.CONNECT.equals(command)) {

            String username = getUsernameFromSession(accessor);

            if (username != null) {

                // Set Principal for further use
                accessor.setUser(new StompPrincipal(username));

                // Mark online
                onlineUserService.userOnline(username);

                // Update DB
                userRepository.updateUserStatus(username, true, null);

                System.out.println("🟢 USER CONNECTED: " + username);
            }
        }

        // ================= DISCONNECT =================
        if (StompCommand.DISCONNECT.equals(command)) {

            Principal user = accessor.getUser();

            if (user != null) {

                String username = user.getName();

                // Mark offline
                onlineUserService.userOffline(username);

                // Update DB with last seen
                userRepository.updateUserStatus(
                        username,
                        false,
                        LocalDateTime.now()
                );

                System.out.println("🔴 USER DISCONNECTED: " + username);
            }
        }

        return message;
    }

    // ================= HELPER =================
    private String getUsernameFromSession(StompHeaderAccessor accessor) {

        Object usernameObj = accessor.getSessionAttributes() != null
                ? accessor.getSessionAttributes().get("username")
                : null;

        return usernameObj != null ? usernameObj.toString() : null;
    }
}