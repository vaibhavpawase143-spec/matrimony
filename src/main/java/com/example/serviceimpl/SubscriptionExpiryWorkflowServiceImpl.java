package com.example.serviceimpl;

import com.example.model.*;
import com.example.queue.SubscriptionExpiryJobPayload;
import com.example.queue.SubscriptionExpiryProducer;
import com.example.repository.SubscriptionExpiryJobRepository;
import com.example.repository.UserSubscriptionRepository;
import com.example.service.ProfilePremiumSyncService;
import com.example.service.SubscriptionExpiryWorkflowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionExpiryWorkflowServiceImpl implements SubscriptionExpiryWorkflowService {

    private final UserSubscriptionRepository userSubscriptionRepository;
    private final SubscriptionExpiryJobRepository jobRepository;
    private final ProfilePremiumSyncService profilePremiumSyncService;
    private final SubscriptionExpiryProducer producer;

    @Override
    @Transactional
    public List<SubscriptionExpiryJob> processExpiringSubscriptions() {
        LocalDateTime now = LocalDateTime.now();
        log.info("[EXPIRY WORKFLOW] Checking for active subscriptions expiring on or before: {}", now);

        List<UserSubscription> expiringSubscriptions = userSubscriptionRepository.findActiveSubscriptionsDueForExpiry(now);

        if (expiringSubscriptions.isEmpty()) {
            log.info("[EXPIRY WORKFLOW] No active subscriptions due for expiry.");
            return List.of();
        }

        log.info("[EXPIRY WORKFLOW] Found {} active subscription(s) due for expiry.", expiringSubscriptions.size());
        List<SubscriptionExpiryJob> createdJobs = new ArrayList<>();

        for (UserSubscription subscription : expiringSubscriptions) {
            try {
                // 1. Transition ACTIVE -> EXPIRED
                subscription.setIsActive(false);
                subscription.setStatus("EXPIRED");
                userSubscriptionRepository.save(subscription);

                // 2. Synchronize user profile to FREE
                profilePremiumSyncService.sync(subscription.getUser(), subscription);

                // 3. Create Outbox / Job Record with Idempotency Key
                String idempotencyKey = "EXPIRY_EMAIL_SUB_" + subscription.getId();

                if (jobRepository.existsByIdempotencyKey(idempotencyKey)) {
                    log.warn("[EXPIRY WORKFLOW] Idempotency key {} already exists. Skipping job creation for SubscriptionID={}",
                            idempotencyKey, subscription.getId());
                    continue;
                }

                SubscriptionExpiryJob job = SubscriptionExpiryJob.builder()
                        .subscription(subscription)
                        .user(subscription.getUser())
                        .jobType(SubscriptionExpiryJobType.EXPIRY_EMAIL)
                        .status(SubscriptionExpiryJobStatus.PENDING)
                        .attemptCount(0)
                        .idempotencyKey(idempotencyKey)
                        .scheduledAt(now)
                        .build();

                SubscriptionExpiryJob savedJob = jobRepository.save(job);
                createdJobs.add(savedJob);

                log.info("[EXPIRY WORKFLOW] Expired SubscriptionID={} for UserID={} and created JobID={}",
                        subscription.getId(), subscription.getUser().getId(), savedJob.getId());

            } catch (DataIntegrityViolationException e) {
                log.warn("[EXPIRY WORKFLOW] Data integrity exception for SubscriptionID={}: {}. Idempotency active.",
                        subscription.getId(), e.getMessage());
            } catch (Exception e) {
                log.error("[EXPIRY WORKFLOW FAILED] Failed processing SubscriptionID={}: {}",
                        subscription.getId(), e.getMessage(), e);
            }
        }

        return createdJobs;
    }

    @Override
    public void processAndPublishExpiringSubscriptions() {
        // 1. Run DB Transaction to expire & create outbox jobs
        List<SubscriptionExpiryJob> createdJobs = processExpiringSubscriptions();

        // 2. Publish created outbox jobs to RabbitMQ
        for (SubscriptionExpiryJob job : createdJobs) {
            publishJobToQueue(job);
        }

        // 3. Guarantee outbox delivery for any missed PENDING jobs
        publishPendingOutboxJobs();
    }

    @Override
    @Transactional(readOnly = true)
    public void publishPendingOutboxJobs() {
        List<SubscriptionExpiryJob> pendingJobs = jobRepository.findByStatus(SubscriptionExpiryJobStatus.PENDING);
        if (!pendingJobs.isEmpty()) {
            log.info("[OUTBOX PUBLISHER] Found {} pending outbox job(s) to publish.", pendingJobs.size());
            for (SubscriptionExpiryJob job : pendingJobs) {
                publishJobToQueue(job);
            }
        }
    }

    private void publishJobToQueue(SubscriptionExpiryJob job) {
        try {
            SubscriptionExpiryJobPayload payload = SubscriptionExpiryJobPayload.builder()
                    .jobId(job.getId())
                    .subscriptionId(job.getSubscription().getId())
                    .userId(job.getUser().getId())
                    .userEmail(job.getUser().getEmail())
                    .userFirstName(job.getUser().getFirstName())
                    .eventType("SUBSCRIPTION_EXPIRED")
                    .idempotencyKey(job.getIdempotencyKey())
                    .attemptCount(job.getAttemptCount())
                    .build();

            producer.enqueueJob(payload);
            log.info("[OUTBOX PUBLISHED] Published JobID={} to RabbitMQ queue.", job.getId());
        } catch (Exception e) {
            log.error("[OUTBOX PUBLISH FAILED] Failed to publish JobID={} to RabbitMQ: {}", job.getId(), e.getMessage(), e);
        }
    }
}
