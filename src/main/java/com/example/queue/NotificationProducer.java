package com.example.queue;

import com.example.config.RabbitMQConfig;
import com.example.model.NotificationPriority;
import com.example.model.NotificationType;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;
    private final UserRepository userRepository;

    /**
     * Publish an APP notification batch payload to RabbitMQ.
     */
    public void enqueueAppBatch(AppNotificationBatchPayload batchPayload) {
        if (batchPayload.getBatchId() == null) {
            batchPayload.setBatchId(UUID.randomUUID().toString());
        }
        String routingKey = getRoutingKeyForPriority(
                batchPayload.getPriority() != null ? batchPayload.getPriority() : NotificationPriority.MEDIUM
        );

        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.NOTIFICATION_EXCHANGE,
                    routingKey,
                    batchPayload
            );
        } catch (Exception e) {
            log.error("[APP BATCH ENQUEUE FAILED] BatchID={} | StoryID={} | Error={}",
                    batchPayload.getBatchId(), batchPayload.getStoryId(), e.getMessage(), e);
            throw new RuntimeException("Failed to enqueue APP batch: " + e.getMessage(), e);
        }
    }

    /**
     * Publish a bulk email batch payload to RabbitMQ.
     */
    public void enqueueEmailBatch(BulkEmailBatchPayload batchPayload) {
        if (batchPayload.getBatchId() == null) {
            batchPayload.setBatchId(UUID.randomUUID().toString());
        }
        int recipientCount = batchPayload.getRecipients() != null ? batchPayload.getRecipients().size() : 0;

        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.NOTIFICATION_EXCHANGE,
                    RabbitMQConfig.ROUTING_BULK_EMAIL,
                    batchPayload
            );
        } catch (Exception e) {
            log.error("[BULK EMAIL BATCH ENQUEUE FAILED] BatchID={} | JobID={} | Error={}",
                    batchPayload.getBatchId(), batchPayload.getBroadcastJobId(), e.getMessage(), e);
            throw new RuntimeException("Failed to enqueue bulk email batch: " + e.getMessage(), e);
        }
    }

    /**
     * Publish a notification job payload to the appropriate priority-tiered RabbitMQ queue.
     */
    public void enqueueJob(NotificationJobPayload payload) {
        if (payload.getJobId() == null) {
            payload.setJobId(UUID.randomUUID().toString());
        }
        if (payload.getIdempotencyKey() == null) {
            payload.setIdempotencyKey("JOB_" + payload.getJobId());
        }
        if (payload.getPriority() == null) {
            payload.setPriority(NotificationPriority.MEDIUM);
        }

        String routingKey = getRoutingKeyForPayload(payload);

        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.NOTIFICATION_EXCHANGE,
                    routingKey,
                    payload
            );
        } catch (Exception e) {
            log.error("[JOB ENQUEUE FAILED] JobID={} | UserID={} | Error={}",
                    payload.getJobId(), payload.getUserId(), e.getMessage(), e);
            throw new RuntimeException("Failed to enqueue notification job: " + e.getMessage(), e);
        }
    }

    /**
     * Enqueue targeted notifications for explicit receiver IDs asynchronously.
     */
    @Async
    public void enqueueBulkNotifications(List<Long> receiverIds, String title, String message, NotificationType type) {
        if (receiverIds == null || receiverIds.isEmpty()) {
            log.warn("[BULK ENQUEUE] No receiver IDs provided.");
            return;
        }

        log.info("[BULK ENQUEUE START] Enqueueing notification for {} recipients...", receiverIds.size());

        for (Long userId : receiverIds) {
            userRepository.findById(userId).ifPresent(user -> {
                NotificationJobPayload payload = NotificationJobPayload.builder()
                        .jobId(UUID.randomUUID().toString())
                        .idempotencyKey("TARGETED_" + System.currentTimeMillis() + "_" + user.getId())
                        .userId(user.getId())
                        .userEmail(user.getEmail())
                        .userFirstName(user.getFirstName())
                        .title(title)
                        .message(message)
                        .type(type)
                        .channelType(NotificationJobPayload.ChannelType.BOTH)
                        .priority(NotificationPriority.MEDIUM)
                        .retryCount(0)
                        .build();

                enqueueJob(payload);
            });
        }

        log.info("[BULK ENQUEUE COMPLETE] Successfully enqueued notification jobs.");
    }

    private String getRoutingKeyForPayload(NotificationJobPayload payload) {
        if (payload.getChannelType() == NotificationJobPayload.ChannelType.EMAIL && payload.getPriority() == NotificationPriority.LOW) {
            return RabbitMQConfig.ROUTING_BULK_EMAIL;
        }
        return getRoutingKeyForPriority(payload.getPriority());
    }

    private String getRoutingKeyForPriority(NotificationPriority priority) {
        switch (priority) {
            case CRITICAL:
                return RabbitMQConfig.ROUTING_CRITICAL;
            case HIGH:
                return RabbitMQConfig.ROUTING_HIGH;
            case LOW:
                return RabbitMQConfig.ROUTING_LOW;
            case MEDIUM:
            default:
                return RabbitMQConfig.ROUTING_MEDIUM;
        }
    }
}
