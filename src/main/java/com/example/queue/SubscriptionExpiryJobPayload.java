package com.example.queue;

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
public class SubscriptionExpiryJobPayload implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long jobId;
    private Long subscriptionId;
    private Long userId;
    private String userEmail;
    private String userFirstName;
    private String eventType;
    private String idempotencyKey;

    @Builder.Default
    private int attemptCount = 0;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
