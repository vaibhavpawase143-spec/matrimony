package com.example.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationResponse {

    private Long id;

    private Long senderId;

    private Long receiverId;

    private String senderName;

    private String message;

    private Long matchedUserId;

    private Integer matchPercentage;

    private String preview;

    private boolean isRead;

    private String type;
    private String title;
    private LocalDateTime createdAt;
}