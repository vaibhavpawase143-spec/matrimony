package com.example.queue;

import com.example.config.RabbitMQConfig;
import com.example.dto.response.NotificationResponse;
import com.example.model.AppNotificationStatus;
import com.example.model.Notification;
import com.example.model.NotificationJobStatus;
import com.example.model.RecipientEmailStatus;
import com.example.repository.NotificationJobOutboxRepository;
import com.example.repository.NotificationRepository;
import com.example.repository.UserRepository;
import com.example.service.AdminBroadcastService;
import com.example.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationRepository notificationRepository;
    private final NotificationJobOutboxRepository outboxRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final EmailService emailService;
    private final com.example.provider.EmailProvider emailProvider;
    private final RabbitTemplate rabbitTemplate;
    private final AdminBroadcastService adminBroadcastService;

    private static final int MAX_RETRIES = 3;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_CRITICAL, containerFactory = "criticalRabbitListenerContainerFactory")
    public void processCriticalNotification(NotificationJobPayload payload) {
        processNotificationJob(payload);
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_HIGH)
    public void processHighNotification(NotificationJobPayload payload) {
        processNotificationJob(payload);
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_MEDIUM)
    public void processMediumNotification(NotificationJobPayload payload) {
        processNotificationJob(payload);
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_LOW, containerFactory = "bulkRabbitListenerContainerFactory")
    public void processLowNotification(NotificationJobPayload payload) {
        processNotificationJob(payload);
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_BULK_EMAIL, containerFactory = "bulkEmailRabbitListenerContainerFactory")
    public void processBulkEmailNotification(BulkEmailBatchPayload batchPayload) {
        processBulkEmailBatchJob(batchPayload);
    }

    public void processBulkEmailBatchJob(BulkEmailBatchPayload batchPayload) {
        long startTime = System.currentTimeMillis();
        Long jobId = batchPayload.getBroadcastJobId();
        List<BulkEmailBatchPayload.RecipientItem> recipients = batchPayload.getRecipients();

        log.info("[BULK EMAIL CONSUMER INVOCATION] BatchID={} | JobID={} | Class={} | RecipientsCount={}",
                batchPayload.getBatchId(), jobId, batchPayload.getClass().getName(), recipients != null ? recipients.size() : 0);

        if (recipients == null || recipients.isEmpty()) {
            log.warn("[BULK EMAIL CONSUMER] Empty recipients list for BatchID={} | JobID={}", batchPayload.getBatchId(), jobId);
            return;
        }

        List<com.example.provider.BulkEmailRecipientRequest> requests = recipients.stream()
                .map(r -> new com.example.provider.BulkEmailRecipientRequest(r.getUserId(), r.getEmail(), r.getFirstName()))
                .toList();

        List<Long> allUserIds = recipients.stream().map(BulkEmailBatchPayload.RecipientItem::getUserId).toList();

        try {
            log.info("[BULK EMAIL PROVIDER INVOCATION] BatchID={} | JobID={} | Requesting sendBatch for {} recipients",
                    batchPayload.getBatchId(), jobId, requests.size());

            com.example.provider.BulkEmailBatchResponse response = emailProvider.sendBatch(requests, batchPayload.getTitle(), batchPayload.getMessage());

            log.info("[BULK EMAIL PROVIDER RESULT] BatchID={} | JobID={} | Success={} | Accepted={} | Rejected={} | Message={}",
                    batchPayload.getBatchId(), jobId,
                    response != null && response.isSuccess(),
                    response != null ? response.getAcceptedCount() : 0,
                    response != null ? response.getRejectedCount() : 0,
                    response != null ? response.getProviderMessage() : "null");

            if (response != null && response.isSuccess()) {
                List<Long> failedIds = response.getFailedUserIds() != null ? response.getFailedUserIds() : java.util.Collections.emptyList();
                List<Long> successIds = allUserIds.stream().filter(id -> !failedIds.contains(id)).toList();

                if (!successIds.isEmpty() && jobId != null) {
                    adminBroadcastService.recordEmailRecipientBatchStatus(jobId, successIds, RecipientEmailStatus.PROVIDER_ACCEPTED, null);
                }
                if (!failedIds.isEmpty() && jobId != null) {
                    adminBroadcastService.recordEmailRecipientBatchStatus(jobId, failedIds, RecipientEmailStatus.FAILED, response.getProviderMessage());
                }
            } else {
                String errorMsg = response != null ? response.getProviderMessage() : "Bulk email batch failed";
                if (jobId != null) {
                    adminBroadcastService.recordEmailRecipientBatchStatus(jobId, allUserIds, RecipientEmailStatus.FAILED, errorMsg);
                }
            }
        } catch (Exception e) {
            log.error("[BULK EMAIL BATCH CONSUMER FAILED] BatchID={} | JobID={} | Size={} | Error={}",
                    batchPayload.getBatchId(), jobId, recipients.size(), e.getMessage(), e);
            if (jobId != null) {
                adminBroadcastService.recordEmailRecipientBatchStatus(jobId, allUserIds, RecipientEmailStatus.FAILED, e.getMessage());
            }
        }
        long duration = System.currentTimeMillis() - startTime;
        log.info("[BULK EMAIL BATCH PROCESSED] BatchID={} | JobID={} | Recipients={} | Duration={}ms",
                batchPayload.getBatchId(), jobId, recipients.size(), duration);
    }

    public void processNotificationJob(NotificationJobPayload payload) {
        long startTime = System.currentTimeMillis();
        String idempotencyKey = payload.getIdempotencyKey() != null ? payload.getIdempotencyKey() : "JOB_" + payload.getJobId();
        LocalDateTime now = LocalDateTime.now();

        log.info("[JOB PROCESSING] JobID={} | Key={} | Priority={} | Event={} | UserID={} | Channel={} | Attempt={}",
                payload.getJobId(), idempotencyKey, payload.getPriority(), payload.getEventType(), payload.getUserId(), payload.getChannelType(), payload.getRetryCount() + 1);

        if (outboxRepository.existsByIdempotencyKey(idempotencyKey)) {
            int claimed = outboxRepository.claimJobAtomically(idempotencyKey, now);
            if (claimed == 0) {
                log.info("[IDEMPOTENCY] JobKey={} already claimed or completed by another worker. Skipping.", idempotencyKey);
                return;
            }
        }

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
                notification.setCreatedAt(now);

                Notification saved = notificationRepository.save(notification);

                if (saved != null) {
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

                    messagingTemplate.convertAndSend("/topic/notifications/" + payload.getUserId(), response);
                    
                    if (payload.getBroadcastJobId() != null) {
                        adminBroadcastService.recordAppRecipientStatus(payload.getBroadcastJobId(), payload.getUserId(), AppNotificationStatus.SENT, null);
                    }
                }
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

                if (payload.getBroadcastJobId() != null) {
                    adminBroadcastService.recordEmailRecipientStatus(payload.getBroadcastJobId(), payload.getUserId(), RecipientEmailStatus.PROVIDER_ACCEPTED, null);
                }
            }

            long duration = System.currentTimeMillis() - startTime;
            log.info("[JOB SUCCESS] JobID={} | Key={} | UserID={} | Duration={}ms", payload.getJobId(), idempotencyKey, payload.getUserId(), duration);

            outboxRepository.findByIdempotencyKey(idempotencyKey).ifPresent(outbox -> {
                outbox.setStatus(NotificationJobStatus.COMPLETED);
                outbox.setCompletedAt(LocalDateTime.now());
                outboxRepository.save(outbox);
            });

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.warn("[JOB FAILED] JobID={} | Key={} | UserID={} | Attempt={}/{} | Duration={}ms | Error={}",
                    payload.getJobId(), idempotencyKey, payload.getUserId(), payload.getRetryCount() + 1, MAX_RETRIES, duration, e.getMessage());

            boolean permanent = isPermanentFailure(e);

            if (payload.getRetryCount() + 1 >= MAX_RETRIES || permanent) {
                log.error("[JOB DLQ] JobID={} | Key={} | Permanent={} | Exhausted retries. Routing to DLQ.",
                        payload.getJobId(), idempotencyKey, permanent);

                outboxRepository.findByIdempotencyKey(idempotencyKey).ifPresent(outbox -> {
                    outbox.setStatus(NotificationJobStatus.DLQ);
                    outbox.setLastError(e.getMessage());
                    outboxRepository.save(outbox);
                });

                if (payload.getBroadcastJobId() != null) {
                    if (payload.getChannelType() == NotificationJobPayload.ChannelType.APP) {
                        adminBroadcastService.recordAppRecipientStatus(payload.getBroadcastJobId(), payload.getUserId(), AppNotificationStatus.FAILED, e.getMessage());
                    } else if (payload.getChannelType() == NotificationJobPayload.ChannelType.EMAIL) {
                        adminBroadcastService.recordEmailRecipientStatus(payload.getBroadcastJobId(), payload.getUserId(), RecipientEmailStatus.FAILED, e.getMessage());
                    }
                }

                String dlxRoutingKey = getDlqRoutingKey(payload.getPriority(), payload.getChannelType());
                rabbitTemplate.convertAndSend(RabbitMQConfig.NOTIFICATION_DLX, dlxRoutingKey, payload);
            } else {
                payload.setRetryCount(payload.getRetryCount() + 1);

                outboxRepository.findByIdempotencyKey(idempotencyKey).ifPresent(outbox -> {
                    outbox.setStatus(NotificationJobStatus.FAILED);
                    outbox.setAttemptCount(payload.getRetryCount());
                    outbox.setLastError(e.getMessage());
                    outboxRepository.save(outbox);
                });

                if (payload.getBroadcastJobId() != null && payload.getChannelType() == NotificationJobPayload.ChannelType.EMAIL) {
                    adminBroadcastService.recordEmailRecipientStatus(payload.getBroadcastJobId(), payload.getUserId(), RecipientEmailStatus.QUEUED, e.getMessage());
                }

                enqueueRetryWithBackoff(payload);
            }
        }
    }

    private void enqueueRetryWithBackoff(NotificationJobPayload payload) {
        String routingKey = (payload.getChannelType() == NotificationJobPayload.ChannelType.EMAIL && payload.getPriority() == com.example.model.NotificationPriority.LOW)
                ? RabbitMQConfig.ROUTING_BULK_EMAIL
                : getRoutingKeyForPriority(payload.getPriority());
        rabbitTemplate.convertAndSend(RabbitMQConfig.NOTIFICATION_EXCHANGE, routingKey, payload);
    }

    private boolean isPermanentFailure(Exception e) {
        StringBuilder sb = new StringBuilder();
        if (e.getMessage() != null) sb.append(e.getMessage()).append(" ");
        if (e.getCause() != null && e.getCause().getMessage() != null) sb.append(e.getCause().getMessage());
        
        String msg = sb.toString().toLowerCase();
        return msg.contains("invalid address") || 
               msg.contains("550") || 
               msg.contains("user unknown") || 
               msg.contains("recipient rejected") ||
               msg.contains("mailbox unavailable") ||
               msg.contains("too many login attempts") ||
               msg.contains("454");
    }

    private String getRoutingKeyForPriority(com.example.model.NotificationPriority priority) {
        if (priority == null) return RabbitMQConfig.ROUTING_MEDIUM;
        switch (priority) {
            case CRITICAL: return RabbitMQConfig.ROUTING_CRITICAL;
            case HIGH: return RabbitMQConfig.ROUTING_HIGH;
            case LOW: return RabbitMQConfig.ROUTING_LOW;
            case MEDIUM:
            default: return RabbitMQConfig.ROUTING_MEDIUM;
        }
    }

    private String getDlqRoutingKey(com.example.model.NotificationPriority priority, NotificationJobPayload.ChannelType channelType) {
        if (channelType == NotificationJobPayload.ChannelType.EMAIL && priority == com.example.model.NotificationPriority.LOW) {
            return RabbitMQConfig.ROUTING_BULK_EMAIL + ".dlq";
        }
        if (priority == null) return RabbitMQConfig.ROUTING_MEDIUM + ".dlq";
        switch (priority) {
            case CRITICAL: return RabbitMQConfig.ROUTING_CRITICAL + ".dlq";
            case HIGH: return RabbitMQConfig.ROUTING_HIGH + ".dlq";
            case LOW: return RabbitMQConfig.ROUTING_LOW + ".dlq";
            case MEDIUM:
            default: return RabbitMQConfig.ROUTING_MEDIUM + ".dlq";
        }
    }

    @RabbitListener(queues = {RabbitMQConfig.DLQ_CRITICAL, RabbitMQConfig.DLQ_HIGH, RabbitMQConfig.DLQ_MEDIUM, RabbitMQConfig.DLQ_LOW, RabbitMQConfig.DLQ_BULK_EMAIL})
    public void processDeadLetterNotification(NotificationJobPayload payload) {
        log.error("[DLQ RECEIVED] Permanently failed notification job. JobID={} | Key={} | Priority={} | UserID={} | Title={}",
                payload.getJobId(), payload.getIdempotencyKey(), payload.getPriority(), payload.getUserId(), payload.getTitle());
    }
}
