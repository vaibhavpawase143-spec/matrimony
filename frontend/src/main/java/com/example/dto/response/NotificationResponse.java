package com.example.dto.response;

import java.time.LocalDateTime;

public class NotificationResponse {
    private Long id;
    private String message;
    private boolean isRead;
    private String type;
    private LocalDateTime createdAt;
}