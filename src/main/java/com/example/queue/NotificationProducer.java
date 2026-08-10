package com.example.queue;

import com.example.config.RabbitMQConfig;
import com.example.model.NotificationType;
import com.example.model.User;
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
     * Publish a single notification job to RabbitMQ queue.
     */
    public void enqueueJob(NotificationJobPayload payload) {
        if (payload.getJobId() == null) {
            payload.setJobId(UUID.randomUUID().toString());
        }
        log.info("[JOB ENQUEUED] JobID={} | ReceiverID={} | Channel={} | Title={}",
                payload.getJobId(), payload.getUserId(), payload.getChannelType(), payload.getTitle());

        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.NOTIFICATION_EXCHANGE,
                    RabbitMQConfig.NOTIFICATION_ROUTING_KEY,
                    payload
            );
        } catch (Exception e) {
            log.error("[JOB ENQUEUE FAILED] JobID={} | ReceiverID={} | Error={}",
                    payload.getJobId(), payload.getUserId(), e.getMessage(), e);
            throw new RuntimeException("Failed to enqueue notification job: " + e.getMessage(), e);
        }
    }

    /**
     * Asynchronously enqueue bulk notifications for selected or all users.
     */
    @Async
    public void enqueueBulkNotifications(List<Long> receiverIds, String title, String message, NotificationType type) {
        log.info("[BULK ENQUEUE START] Preparing notification jobs...");
        
        List<User> targetUsers;
        if (receiverIds != null && !receiverIds.isEmpty()) {
            targetUsers = userRepository.findAllById(receiverIds);
        } else {
            targetUsers = userRepository.findByIsActiveTrue();
        }

        log.info("[BULK ENQUEUE] Total recipients to enqueue: {}", targetUsers.size());
        
        int enqueuedCount = 0;
        for (User user : targetUsers) {
            NotificationJobPayload payload = NotificationJobPayload.builder()
                    .jobId(UUID.randomUUID().toString())
                    .userId(user.getId())
                    .userEmail(user.getEmail())
                    .userFirstName(user.getFirstName())
                    .title(title)
                    .message(message)
                    .type(type)
                    .channelType(NotificationJobPayload.ChannelType.BOTH)
                    .retryCount(0)
                    .build();

            enqueueJob(payload);
            enqueuedCount++;
        }

        log.info("[BULK ENQUEUE COMPLETE] Successfully enqueued {} notification jobs.", enqueuedCount);
    }
}
