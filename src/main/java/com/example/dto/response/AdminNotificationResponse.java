package com.example.dto.response;

import com.example.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminNotificationResponse {

    private Long id;

    private Long adminId;

    private String title;

    private String message;

    private NotificationType type;

    private Boolean read;

    private Boolean deleted;

    private LocalDateTime createdAt;
}