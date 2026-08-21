package com.example.queue;

import com.example.model.NotificationPriority;
import com.example.model.NotificationType;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppNotificationBatchPayload implements Serializable {

    private String batchId;
    private Long storyId;
    private Integer storyVersion;
    private Long broadcastJobId;
    private String title;
    private String message;
    private NotificationType type;
    private NotificationPriority priority;
    private String eventType;
    private Long referenceId;
    private List<RecipientItem> recipients;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecipientItem implements Serializable {
        private Long userId;
    }
}
