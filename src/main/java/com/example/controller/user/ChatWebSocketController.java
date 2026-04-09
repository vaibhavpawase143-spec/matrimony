package com.example.controller.user;

import com.example.dto.request.ChatMessageDTO;
import com.example.model.Message;
import com.example.service.ChatService;

import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    // ================= 💬 SEND MESSAGE =================
    @MessageMapping("/chat")
    public void sendMessage(ChatMessageDTO dto, Principal principal) {

        String senderEmail = principal.getName();

        Message message;

        // 🔥 MEDIA MESSAGE
        if ("MEDIA".equalsIgnoreCase(dto.getType())) {
            message = chatService.sendMediaMessage(
                    senderEmail,
                    dto.getReceiverId(),
                    dto.getMediaUrl(),
                    dto.getMediaType()
            );
        }
        // 🔥 TEXT / REPLY MESSAGE
        else {
            message = chatService.sendMessageByEmail(
                    senderEmail,
                    dto.getReceiverId(),
                    dto.getContent(),
                    dto.getReplyToMessageId()
            );
        }

        // ✅ Convert to DTO response
        ChatMessageDTO response = mapToDTO(message);

        // 📤 Send to receiver
        messagingTemplate.convertAndSendToUser(
                response.getReceiver(),
                "/queue/messages",
                response
        );
    }

    // ================= ✍️ TYPING =================
    @MessageMapping("/typing")
    public void typing(ChatMessageDTO dto, Principal principal) {

        String sender = principal.getName();

        ChatMessageDTO response = ChatMessageDTO.builder()
                .sender(sender)
                .receiver(dto.getReceiver())
                .action("TYPING")
                .build();

        messagingTemplate.convertAndSendToUser(
                dto.getReceiver(),
                "/queue/typing",
                response
        );
    }

    // ================= 🛑 STOP TYPING =================
    @MessageMapping("/stop-typing")
    public void stopTyping(ChatMessageDTO dto, Principal principal) {

        String sender = principal.getName();

        ChatMessageDTO response = ChatMessageDTO.builder()
                .sender(sender)
                .receiver(dto.getReceiver())
                .action("STOP_TYPING")
                .build();

        messagingTemplate.convertAndSendToUser(
                dto.getReceiver(),
                "/queue/typing",
                response
        );
    }

    // ================= 🔁 MAPPER =================
    private ChatMessageDTO mapToDTO(Message message) {
        return ChatMessageDTO.builder()
                .sender(message.getSender().getEmail())
                .receiver(message.getReceiver().getEmail())

                // ✅ Safe content
                .content(
                        message.getContent() != null
                                ? message.getContent()
                                : "[Media]"
                )

                .type(message.getMessageType())
                .conversationId(message.getConversation().getId())
                .timestamp(message.getCreatedAt().toString())
                .status(message.getStatus().name())
                .messageId(message.getId())

                .replyToMessageId(
                        message.getReplyTo() != null ? message.getReplyTo().getId() : null
                )
                .replyToContent(
                        message.getReplyTo() != null ? message.getReplyTo().getContent() : null
                )

                .mediaUrl(message.getMediaUrl())
                .mediaType(message.getMediaType())

                .build();
    }
}