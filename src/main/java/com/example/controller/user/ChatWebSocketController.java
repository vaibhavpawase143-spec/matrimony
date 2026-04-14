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

        if ("MEDIA".equalsIgnoreCase(dto.getType())) {
            message = chatService.sendMediaMessage(
                    senderEmail,
                    dto.getReceiverId(),
                    dto.getMediaUrl(),
                    dto.getMediaType()
            );
        } else {
            message = chatService.sendMessageByEmail(
                    senderEmail,
                    dto.getReceiverId(),
                    dto.getContent(),
                    dto.getReplyToMessageId()
            );
        }

        ChatMessageDTO response = mapToDTO(message);

        // ✅ SEND TO RECEIVER (EMAIL BASED)
        messagingTemplate.convertAndSendToUser(
                message.getReceiver().getEmail(),
                "/queue/messages",
                response
        );

        // ✅ SEND BACK TO SENDER (for sync)
        messagingTemplate.convertAndSendToUser(
                senderEmail,
                "/queue/messages",
                response
        );
    }

    // ================= ✍️ TYPING =================
    @MessageMapping("/typing")
    public void typing(ChatMessageDTO dto, Principal principal) {

        String senderEmail = principal.getName();

        messagingTemplate.convertAndSendToUser(
                dto.getReceiver(), // must be email
                "/queue/typing",
                ChatMessageDTO.builder()
                        .sender(senderEmail)
                        .receiver(dto.getReceiver())
                        .action("TYPING")
                        .build()
        );
    }

    // ================= 🛑 STOP TYPING =================
    @MessageMapping("/stop-typing")
    public void stopTyping(ChatMessageDTO dto, Principal principal) {

        String senderEmail = principal.getName();

        messagingTemplate.convertAndSendToUser(
                dto.getReceiver(),
                "/queue/typing",
                ChatMessageDTO.builder()
                        .sender(senderEmail)
                        .receiver(dto.getReceiver())
                        .action("STOP_TYPING")
                        .build()
        );
    }

    // ================= 👁 SEEN =================
    @MessageMapping("/seen")
    public void markSeen(ChatMessageDTO dto, Principal principal) {

        String email = principal.getName();

        Long userId = chatService.getUserByEmail(email).getId();

        chatService.markAsSeen(dto.getConversationId(), userId);

        // 🔥 notify sender (EMAIL BASED)
        messagingTemplate.convertAndSendToUser(
                dto.getReceiver(), // sender email
                "/queue/seen",
                dto
        );
    }

    // ================= 🔁 MAPPER =================
    private ChatMessageDTO mapToDTO(Message message) {
        return ChatMessageDTO.builder()
                .sender(message.getSender().getEmail())
                .receiver(message.getReceiver().getEmail())
                .content(message.getContent() != null ? message.getContent() : "[Media]")
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