package com.example.queue;

import com.example.config.RabbitMQConfig;
import com.example.model.SubscriptionExpiryJob;
import com.example.model.SubscriptionExpiryJobStatus;
import com.example.repository.SubscriptionExpiryJobRepository;
import com.example.service.EmailService;
import com.example.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionExpiryConsumer {

    private final SubscriptionExpiryJobRepository jobRepository;
    private final EmailService emailService;
    private final NotificationService notificationService;
    private final RabbitTemplate rabbitTemplate;

    @Value("${subscription.expiry.max-attempts:3}")
    private int maxAttempts = 3;

    @RabbitListener(queues = RabbitMQConfig.SUBSCRIPTION_EXPIRY_EMAIL_QUEUE)
    @Transactional
    public void processSubscriptionExpiryJob(SubscriptionExpiryJobPayload payload) {
        log.info("[SUB EXPIRY CONSUMER] Processing message. JobID={} | SubID={} | Attempt={}",
                payload.getJobId(), payload.getSubscriptionId(), payload.getAttemptCount() + 1);

        Optional<SubscriptionExpiryJob> jobOptional = jobRepository.findById(payload.getJobId());
        if (jobOptional.isEmpty()) {
            log.warn("[SUB EXPIRY CONSUMER] Job record not found in DB for JobID={}. Stopping.", payload.getJobId());
            return;
        }

        SubscriptionExpiryJob job = jobOptional.get();

        // 1. Idempotency Check: Skip if already COMPLETED
        if (job.getStatus() == SubscriptionExpiryJobStatus.COMPLETED) {
            log.info("[SUB EXPIRY CONSUMER] Idempotency check: JobID={} already COMPLETED. Skipping duplicate email execution.", job.getId());
            return;
        }

        // 2. Mark PROCESSING
        job.setStatus(SubscriptionExpiryJobStatus.PROCESSING);
        job.setStartedAt(LocalDateTime.now());
        jobRepository.save(job);

        try {
            // 3. Send Expiry Email
            log.info("[SUB EXPIRY CONSUMER] Sending expiry email to {} for SubscriptionID={}", payload.getUserEmail(), payload.getSubscriptionId());
            emailService.sendPremiumExpiredEmail(payload.getUserEmail(), payload.getUserFirstName());

            // 4. Trigger In-App Notification
            notificationService.createSubscriptionExpiredNotification(payload.getUserId(), payload.getSubscriptionId());

            // 5. Mark COMPLETED
            job.setStatus(SubscriptionExpiryJobStatus.COMPLETED);
            job.setCompletedAt(LocalDateTime.now());
            jobRepository.save(job);

            log.info("[SUB EXPIRY CONSUMER] JobID={} completed successfully for SubscriptionID={}", job.getId(), payload.getSubscriptionId());

        } catch (Exception e) {
            log.error("[SUB EXPIRY CONSUMER FAILED] JobID={} | Error: {}", job.getId(), e.getMessage(), e);

            int currentAttempts = job.getAttemptCount() + 1;
            job.setAttemptCount(currentAttempts);
            job.setLastError(e.getMessage());

            boolean isPermanentError = isPermanentEmailFailure(e);

            if (currentAttempts >= maxAttempts || isPermanentError) {
                log.error("[SUB EXPIRY CONSUMER DLQ] JobID={} failed permanently (Attempts={}/{}, Permanent={}). Moving to DLQ.",
                        job.getId(), currentAttempts, maxAttempts, isPermanentError);
                
                job.setStatus(SubscriptionExpiryJobStatus.DLQ);
                jobRepository.save(job);

                // Send to RabbitMQ DLQ
                payload.setAttemptCount(currentAttempts);
                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.SUBSCRIPTION_EXPIRY_EMAIL_DLX,
                        RabbitMQConfig.SUBSCRIPTION_EXPIRY_EMAIL_DLQ_ROUTING_KEY,
                        payload
                );
            } else {
                log.warn("[SUB EXPIRY CONSUMER RETRY] JobID={} failed attempt {}/{}. Will retry.", job.getId(), currentAttempts, maxAttempts);
                job.setStatus(SubscriptionExpiryJobStatus.PENDING);
                jobRepository.save(job);
                
                payload.setAttemptCount(currentAttempts);
                
                // Exponential Backoff Delay before retry
                long backoffMs = (long) Math.pow(2, currentAttempts) * 1000;
                try {
                    Thread.sleep(backoffMs);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }

                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.SUBSCRIPTION_EXPIRY_EXCHANGE,
                        RabbitMQConfig.SUBSCRIPTION_EXPIRY_EMAIL_ROUTING_KEY,
                        payload
                );
            }
        }
    }

    @RabbitListener(queues = RabbitMQConfig.SUBSCRIPTION_EXPIRY_EMAIL_DLQ)
    public void processDeadLetterSubscriptionExpiry(SubscriptionExpiryJobPayload payload) {
        log.error("[SUB EXPIRY DLQ RECEIVED] Permanently failed job. JobID={} | SubID={} | UserEmail={}",
                payload.getJobId(), payload.getSubscriptionId(), payload.getUserEmail());
    }

    private boolean isPermanentEmailFailure(Exception e) {
        String msg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
        return msg.contains("invalid address") || msg.contains("550") || msg.contains("user unknown") || msg.contains("recipient rejected");
    }
}
