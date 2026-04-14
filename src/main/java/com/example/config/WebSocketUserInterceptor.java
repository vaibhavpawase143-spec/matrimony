package com.example.config;

import com.example.repository.UserRepository;
import com.example.service.OnlineUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
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

        Principal user = accessor.getUser();
        if (user == null) return message;

        String email = user.getName();

        if (StompCommand.CONNECT.equals(command)) {

            onlineUserService.userOnline(email);
            userRepository.updateUserStatus(email, true, null);

            System.out.println("🟢 CONNECTED: " + email);
        }

        if (StompCommand.DISCONNECT.equals(command)) {

            onlineUserService.userOffline(email);
            userRepository.updateUserStatus(email, false, LocalDateTime.now());

            System.out.println("🔴 DISCONNECTED: " + email);
        }

        return message;
    }
}