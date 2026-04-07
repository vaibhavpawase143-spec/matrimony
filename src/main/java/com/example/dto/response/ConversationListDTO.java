package com.example.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversationListDTO {

    private Long conversationId;

    private Long otherUserId;
    private String otherUsername;

    private String lastMessage;
    private LocalDateTime lastMessageTime;

    private long unreadCount;

    // 🔥 NEW (WhatsApp level)
    private Boolean isOnline;
    private LocalDateTime lastSeen;
}