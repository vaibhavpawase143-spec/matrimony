package com.example.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {

    private String sender;
    private String receiver;

    private String content;
    private String type;

    private Long conversationId;
    private String timestamp;

    private String status; // SENT / DELIVERED / SEEN
    private Long messageId;
}