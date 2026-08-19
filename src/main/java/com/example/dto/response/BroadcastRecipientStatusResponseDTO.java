package com.example.dto.response;

import com.example.model.AppNotificationStatus;
import com.example.model.RecipientEmailStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BroadcastRecipientStatusResponseDTO {
    private Long id;
    private Long broadcastJobId;
    private Long userId;
    private String userEmail;
    private AppNotificationStatus appNotificationStatus;
    private RecipientEmailStatus emailStatus;
    private Integer emailAttemptCount;
    private String emailError;
    private LocalDateTime notificationProcessedAt;
    private LocalDateTime emailQueuedAt;
    private LocalDateTime emailAcceptedAt;
    private LocalDateTime emailDeliveredAt;
    private String lastError;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
