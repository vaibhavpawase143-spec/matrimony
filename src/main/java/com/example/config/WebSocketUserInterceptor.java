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

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class WebSocketUserInterceptor implements ChannelInterceptor {

    private final UserRepository userRepository;
    private final OnlineUserService onlineUserService;

    @Override
    public Message<?> preSend(
            Message<?> message,
            MessageChannel channel
    ) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(
                        message,
                        StompHeaderAccessor.class
                );

        if (accessor == null) {
            return message;
        }

        StompCommand command = accessor.getCommand();

        if (command == null) {
            return message;
        }

        try {

            // ================= CONNECT =================

            if (StompCommand.CONNECT.equals(command)) {

                Object usernameObj =
                        accessor.getSessionAttributes()
                                .get("username");

                if (usernameObj != null) {

                    String email = usernameObj.toString();

                    accessor.setUser(new StompPrincipal(email));

                    onlineUserService.userOnline(email);

                    userRepository.updateUserStatus(
                            email,
                            true,
                            null
                    );

                    System.out.println("🟢 USER ONLINE : " + email);

                }

            }



        } catch (Exception e) {

            e.printStackTrace();

        }

        return message;

    }

}