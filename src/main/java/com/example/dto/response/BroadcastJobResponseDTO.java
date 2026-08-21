package com.example.dto.response;

import com.example.model.BroadcastJobStatus;
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
public class BroadcastJobResponseDTO {

    private Long id;
    private String title;
    private String message;
    private NotificationType notificationType;
    private BroadcastJobStatus status;
    private Long totalRecipients;
    private Long enqueuedRecipients;
    private Long processedRecipients;
    private Long successfulRecipients;
    private Long failedRecipients;
    private double progressPercentage;
    private Double currentThroughput;
    private Long estimatedRemainingSeconds;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private String lastError;
    private Long createdByAdminId;
    private Boolean isTestMode;
    private Integer testRecipientLimit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
