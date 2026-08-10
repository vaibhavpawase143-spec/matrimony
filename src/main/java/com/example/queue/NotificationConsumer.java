package com.example.queue;

import com.example.config.RabbitMQConfig;
import com.example.dto.response.NotificationResponse;
import com.example.model.Notification;
import com.example.repository.NotificationRepository;
import com.example.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final EmailService emailService;
    private final RabbitTemplate rabbitTemplate;

    private static final int MAX_RETRIES = 3;

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    @Transactional
    public void processNotificationJob(NotificationJobPayload payload) {
        log.info("[JOB PROCESSING] JobID={} | UserID={} | Channel={} | Attempt={}",
                payload.getJobId(), payload.getUserId(), payload.getChannelType(), payload.getRetryCount() + 1);

        try {
            // 1. Process App / WebSocket Notification
            if (payload.getChannelType() == NotificationJobPayload.ChannelType.APP || 
                payload.getChannelType() == NotificationJobPayload.ChannelType.BOTH) {
                
                Notification notification = new Notification();
                notification.setSenderId(null);
                notification.setReceiverId(payload.getUserId());
                notification.setTitle(payload.getTitle());
                notification.setMessage(payload.getMessage());
                notification.setType(payload.getType());
                notification.setRead(false);
                notification.setDeleted(false);
                notification.setCreatedAt(LocalDateTime.now());

                Notification saved = notificationRepository.save(notification);

                NotificationResponse response = new NotificationResponse();
                response.setId(saved.getId());
                response.setSenderId(null);
                response.setReceiverId(payload.getUserId());
                response.setSenderName("System");
                response.setTitle(saved.getTitle());
                response.setMessage(saved.getMessage());
                response.setType(saved.getType().name());
                response.setRead(saved.isRead());
                response.setCreatedAt(saved.getCreatedAt());

                messagingTemplate.convertAndSend(
                        "/topic/notifications/" + payload.getUserId(),
                        response
                );
            }

            // 2. Process Email Notification
            if ((payload.getChannelType() == NotificationJobPayload.ChannelType.EMAIL || 
                 payload.getChannelType() == NotificationJobPayload.ChannelType.BOTH) 
                && payload.getUserEmail() != null && !payload.getUserEmail().isBlank()) {
                
                emailService.sendAnnouncementEmail(
                        payload.getUserEmail(),
                        payload.getUserFirstName() != null ? payload.getUserFirstName() : "User",
                        payload.getTitle(),
                        payload.getMessage()
                );
            }

            log.info("[JOB SUCCESS] JobID={} | UserID={}", payload.getJobId(), payload.getUserId());

        } catch (Exception e) {
            log.warn("[JOB FAILED] JobID={} | UserID={} | Retry={}/{} | Error={}",
                    payload.getJobId(), payload.getUserId(), payload.getRetryCount() + 1, MAX_RETRIES, e.getMessage());

            if (payload.getRetryCount() < MAX_RETRIES - 1) {
                payload.setRetryCount(payload.getRetryCount() + 1);
                
                // Exponential Backoff Delay (1s, 2s, 4s)
                long backoffMs = (long) Math.pow(2, payload.getRetryCount()) * 1000;
                try {
                    Thread.sleep(backoffMs);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }

                // Re-enqueue job with incremented retry count
                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.NOTIFICATION_EXCHANGE,
                        RabbitMQConfig.NOTIFICATION_ROUTING_KEY,
                        payload
                );
            } else {
                log.error("[JOB DEAD-LETTERED] JobID={} | UserID={} | Exhausted all {} retries. Forwarding to DLQ.",
                        payload.getJobId(), payload.getUserId(), MAX_RETRIES);
                
                // Route to Dead Letter Queue (DLQ)
                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.NOTIFICATION_DLX,
                        RabbitMQConfig.NOTIFICATION_DLQ_ROUTING_KEY,
                        payload
                );
            }
        }
    }

    /**
     * Listener for Dead Letter Queue to monitor permanently failed notification jobs.
     */
    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_DLQ)
    public void processDeadLetterNotification(NotificationJobPayload payload) {
        log.error("[DLQ RECEIVED] Permanently failed job. JobID={} | UserID={} | Title={}",
                payload.getJobId(), payload.getUserId(), payload.getTitle());
    }
}
