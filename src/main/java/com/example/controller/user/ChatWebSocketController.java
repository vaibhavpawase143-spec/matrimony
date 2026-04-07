package com.example.controller.user;

import com.example.dto.request.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/typing")
    public void typing(ChatMessageDTO dto) {

        messagingTemplate.convertAndSendToUser(
                dto.getReceiver(),
                "/queue/typing",
                dto
        );
    }
}