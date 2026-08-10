package com.example.queue;

import com.example.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationJobPayload implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum ChannelType {
        APP,
        EMAIL,
        BOTH
    }

    private String jobId;
    private Long userId;
    private String userEmail;
    private String userFirstName;
    private String title;
    private String message;
    private NotificationType type;
    private ChannelType channelType;
    
    @Builder.Default
    private int retryCount = 0;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
