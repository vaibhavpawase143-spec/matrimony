package com.example.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {

    // ================= BASIC =================
    private String sender;      // email (from JWT)
    private String receiver;    // email (for WebSocket routing)

    private Long receiverId;    // 🔥 IMPORTANT for DB operations

    private String content;
    private String type;        // TEXT / MEDIA / FORWARD

    private Long conversationId;
    private String timestamp;

    // ================= REPLY =================
    private Long replyToMessageId;
    private String replyToContent;

    // ================= STATUS =================
    private String status; // SENT / DELIVERED / SEEN
    private Long messageId;

    // ================= MEDIA =================
    private String mediaUrl;
    private String mediaType; // IMAGE / VIDEO / AUDIO

    // ================= EXTRA (REAL-TIME) =================
    private String action; // TYPING / STOP_TYPING / MESSAGE

}