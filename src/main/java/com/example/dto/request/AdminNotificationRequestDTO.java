package com.example.dto.request;

import com.example.model.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AdminNotificationRequestDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Message is required")
    private String message;

    @NotNull(message = "Notification type is required")
    private NotificationType type;

    /**
     * Send notification to:
     * - null or empty -> Broadcast to all active users
     * - One ID         -> Send to single user
     * - Multiple IDs   -> Send to selected users
     */
    private List<Long> receiverIds;
}