package com.example.serviceimpl;

import com.example.dto.response.BroadcastJobResponseDTO;
import com.example.dto.response.BroadcastRecipientStatusResponseDTO;
import com.example.model.*;
import com.example.queue.NotificationJobPayload;
import com.example.queue.NotificationProducer;
import com.example.repository.BroadcastJobRepository;
import com.example.repository.BroadcastRecipientStatusRepository;
import com.example.repository.UserRepository;
import com.example.repository.projection.UserBroadcastProjection;
import com.example.service.AdminBroadcastService;
import com.example.service.AdminNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminBroadcastServiceImpl implements AdminBroadcastService {

    private final BroadcastJobRepository broadcastJobRepository;
    private final BroadcastRecipientStatusRepository recipientStatusRepository;
    private final UserRepository userRepository;
    private final NotificationProducer notificationProducer;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    @Lazy
    private AdminNotificationService adminNotificationService;

    @Qualifier("bulkEmailExecutor")
    private final Executor bulkEmailExecutor;

    @Value("${broadcast.test-mode:false}")
    private boolean testMode;

    @Value("${broadcast.test-recipient-limit:100}")
    private int testRecipientLimit;

    @Value("${broadcast.chunk-size:1000}")
    private int configuredChunkSize = 1000;

    @Value("${broadcast.email.batch-size:500}")
    private int emailBatchSize = 500;

    private static final long WS_THROTTLE_INTERVAL_MS = 500;

    private final Map<Long, AtomicLong> progressPublishTimes = new ConcurrentHashMap<>();

    @Override
    @Transactional
    public Long initiateBroadcast(String title, String message, NotificationType type, Long adminId) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Broadcast title cannot be blank.");
        }
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Broadcast message cannot be blank.");
        }

        Optional<BroadcastJob> existingJob = broadcastJobRepository.findFirstByTitleAndMessageAndStatusIn(
                title.trim(),
                message.trim(),
                List.of(BroadcastJobStatus.PENDING, BroadcastJobStatus.IN_PROGRESS)
        );

        if (existingJob.isPresent()) {
            BroadcastJob job = existingJob.get();
            log.warn("[BROADCAST IDEMPOTENCY] Active broadcast job already exists with ID={}. Returning existing ID.", job.getId());
            return job.getId();
        }

        Long totalActive = userRepository.countByIsActiveTrueAndIsDeletedFalse();
        Long targetRecipients = totalActive;
        if (testMode) {
            targetRecipients = Math.min(totalActive, (long) testRecipientLimit);
            log.info("[BROADCAST TEST-MODE ACTIVE] Recipient limit set to {} (Total active in DB: {})", targetRecipients, totalActive);
        } else {
            log.info("[BROADCAST DYNAMIC DB PRODUCTION] Total active recipients counted dynamically from DB: {}", totalActive);
        }

        LocalDateTime now = LocalDateTime.now();

        BroadcastJob job = BroadcastJob.builder()
                .title(title.trim())
                .message(message.trim())
                .notificationType(type != null ? type : NotificationType.ANNOUNCEMENT)
                .status(BroadcastJobStatus.IN_PROGRESS)
                .startedAt(now)
                .lastProcessedUserId(0L)
                .totalRecipients(targetRecipients)
                .enqueuedRecipients(0L)
                .processedRecipients(0L)
                .successfulRecipients(0L)
                .failedRecipients(0L)
                .createdByAdminId(adminId)
                .isTestMode(testMode)
                .build();

        BroadcastJob saved = broadcastJobRepository.save(job);
        log.info("[BROADCAST CREATED] JobID={} | TotalRecipients={} | Status=IN_PROGRESS | TestMode={}. Returning HTTP response immediately.", saved.getId(), targetRecipients, testMode);

        publishWebSocketProgress(toResponseDTO(saved));

        if (adminNotificationService != null) {
            adminNotificationService.publishBroadcastLifecycleNotification(
                    saved.getId(),
                    BroadcastJobStatus.IN_PROGRESS,
                    saved.getTitle(),
                    "Notification broadcast in progress..."
            );
        }

        final Long jobIdToProcess = saved.getId();
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    log.info("[BROADCAST ASYNC SUBMIT] Submitting BroadcastJobID={} to background executor thread.", jobIdToProcess);
                    CompletableFuture.runAsync(() -> processBroadcastJob(jobIdToProcess), bulkEmailExecutor);
                }
            });
        } else {
            CompletableFuture.runAsync(() -> processBroadcastJob(jobIdToProcess), bulkEmailExecutor);
        }

        return saved.getId();
    }

    @org.springframework.context.event.EventListener(org.springframework.boot.context.event.ApplicationReadyEvent.class)
    @Transactional
    public void handleApplicationStartup() {
        int interruptedCount = broadcastJobRepository.markActiveJobsAsInterruptedNative();
        if (interruptedCount > 0) {
            log.warn("[BROADCAST APPLICATION STARTUP] Interrupted {} active/pending broadcast jobs from prior application session. Set status=INTERRUPTED.", interruptedCount);
        }
    }

    @Async("bulkEmailExecutor")
    public void processBroadcastAsync(Long broadcastJobId) {
        processBroadcastJob(broadcastJobId);
    }

    @Scheduled(fixedDelay = 60000)
    public void processPendingBroadcasts() {
        List<BroadcastJob> pending = broadcastJobRepository.findByStatusIn(List.of(BroadcastJobStatus.PENDING, BroadcastJobStatus.IN_PROGRESS));

        for (BroadcastJob job : pending) {
            if (job.getProcessedRecipients() < job.getTotalRecipients()) {
                log.info("[BROADCAST PENDING PICKUP] Processing BroadcastJobID={} on background scheduler.", job.getId());
                CompletableFuture.runAsync(() -> processBroadcastJob(job.getId()), bulkEmailExecutor);
            }
        }
    }

    @Override
    @Transactional
    public BroadcastJobResponseDTO resumeBroadcastJob(Long jobId) {
        if (jobId == null) {
            throw new IllegalArgumentException("Job ID cannot be null");
        }

        int updatedRows = broadcastJobRepository.tryResumeInterruptedJobNative(jobId);
        if (updatedRows != 1) {
            BroadcastJob existing = broadcastJobRepository.findById(jobId).orElse(null);
            if (existing != null && existing.getStatus() == BroadcastJobStatus.IN_PROGRESS) {
                log.warn("[BROADCAST CONCURRENT RESUME] JobID={} is already IN_PROGRESS.", jobId);
                return toResponseDTO(existing);
            }
            throw new IllegalStateException("Broadcast job " + jobId + " is not in INTERRUPTED state and cannot be resumed.");
        }

        BroadcastJob job = broadcastJobRepository.findById(jobId).orElseThrow(
                () -> new IllegalStateException("Broadcast job " + jobId + " not found")
        );

        log.info("[BROADCAST RESUMED] JobID={} | Resuming from LastUserId={} | TotalRecipients={}",
                job.getId(), job.getLastProcessedUserId(), job.getTotalRecipients());

        publishWebSocketProgress(toResponseDTO(job));

        if (adminNotificationService != null) {
            adminNotificationService.publishBroadcastLifecycleNotification(
                    job.getId(),
                    BroadcastJobStatus.IN_PROGRESS,
                    job.getTitle(),
                    "Notification broadcast resumed by admin."
            );
        }

        final Long jobIdToProcess = job.getId();
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    CompletableFuture.runAsync(() -> processBroadcastJob(jobIdToProcess), bulkEmailExecutor);
                }
            });
        } else {
            CompletableFuture.runAsync(() -> processBroadcastJob(jobIdToProcess), bulkEmailExecutor);
        }

        return toResponseDTO(job);
    }

    @Override
    @Transactional
    public BroadcastJobResponseDTO cancelBroadcastJob(Long jobId) {
        if (jobId == null) {
            throw new IllegalArgumentException("Job ID cannot be null");
        }

        int updatedRows = broadcastJobRepository.tryCancelInterruptedOrActiveJobNative(jobId);
        if (updatedRows != 1) {
            BroadcastJob existing = broadcastJobRepository.findById(jobId).orElse(null);
            if (existing != null && existing.getStatus() == BroadcastJobStatus.CANCELLED) {
                return toResponseDTO(existing);
            }
            throw new IllegalStateException("Broadcast job " + jobId + " cannot be cancelled.");
        }

        BroadcastJob job = broadcastJobRepository.findById(jobId).orElseThrow();

        log.info("[BROADCAST CANCELLED] JobID={} | Processed={}/{}", job.getId(), job.getProcessedRecipients(), job.getTotalRecipients());

        publishWebSocketProgress(toResponseDTO(job));

        if (adminNotificationService != null) {
            adminNotificationService.publishBroadcastLifecycleNotification(
                    job.getId(),
                    BroadcastJobStatus.CANCELLED,
                    job.getTitle(),
                    "Notification broadcast cancelled by admin."
            );
        }

        return toResponseDTO(job);
    }

    public void processBroadcastJob(Long jobId) {
        BroadcastJob job = broadcastJobRepository.findById(jobId).orElse(null);
        if (job == null || job.getStatus() == BroadcastJobStatus.COMPLETED
                || job.getStatus() == BroadcastJobStatus.COMPLETED_WITH_FAILURES
                || job.getStatus() == BroadcastJobStatus.FAILED
                || job.getStatus() == BroadcastJobStatus.INTERRUPTED
                || job.getStatus() == BroadcastJobStatus.CANCELLED) {
            return;
        }

        boolean isJobTestMode = Boolean.TRUE.equals(job.getIsTestMode());
        log.info("[BACKGROUND WORKER RUNNING] JobID={} executing on thread: {}. JobTestMode={}", jobId, Thread.currentThread().getName(), isJobTestMode);

        if (job.getStatus() != BroadcastJobStatus.IN_PROGRESS) {
            job.setStatus(BroadcastJobStatus.IN_PROGRESS);
        }
        if (job.getStartedAt() == null) {
            job.setStartedAt(LocalDateTime.now());
        }
        broadcastJobRepository.save(job);

        Long lastId = job.getLastProcessedUserId() != null ? job.getLastProcessedUserId() : 0L;
        boolean hasMore = true;
        int currentFetchSize = isJobTestMode ? Math.min(configuredChunkSize, testRecipientLimit) : configuredChunkSize;
        long totalEnqueued = recipientStatusRepository.countByBroadcastJobIdNative(jobId);

        log.info("[BROADCAST START] JobID={} | TotalRecipients={} | Resuming from LastUserId={} | InitialEnqueued={} | JobTestMode={}",
                jobId, job.getTotalRecipients(), lastId, totalEnqueued, isJobTestMode);

        try {
            while (hasMore) {
                if (isJobTestMode && totalEnqueued >= testRecipientLimit) {
                    log.info("[BROADCAST TEST-MODE LIMIT REACHED] JobID={} reached recipient limit of {}. Stopping enqueuing.", jobId, testRecipientLimit);
                    break;
                }

                List<UserBroadcastProjection> userChunk = userRepository.findActiveUsersProjectionChunkAfterId(lastId, PageRequest.of(0, currentFetchSize));

                if (userChunk.isEmpty()) {
                    hasMore = false;
                    break;
                }

                if (isJobTestMode) {
                    if (totalEnqueued + userChunk.size() > testRecipientLimit) {
                        int remaining = (int) (testRecipientLimit - totalEnqueued);
                        if (remaining <= 0) {
                            hasMore = false;
                            break;
                        }
                        userChunk = userChunk.subList(0, remaining);
                        hasMore = false;
                    }
                }

                // Step 1: Bulk insert recipient tracking records in PostgreSQL using single native ON CONFLICT SQL
                List<Long> userIds = userChunk.stream().map(UserBroadcastProjection::getId).toList();
                if (!userIds.isEmpty()) {
                    recipientStatusRepository.bulkInsertRecipientStatusesOnConflict(jobId, userIds);
                }

                // Step 2: Enqueue APP payloads & EMAIL batch payloads safely
                List<com.example.queue.BulkEmailBatchPayload.RecipientItem> batchItems = new java.util.ArrayList<>();

                for (UserBroadcastProjection user : userChunk) {
                    NotificationJobPayload appPayload = NotificationJobPayload.builder()
                            .jobId(UUID.randomUUID().toString())
                            .broadcastJobId(jobId)
                            .idempotencyKey("BC_APP_" + jobId + "_U_" + user.getId())
                            .userId(user.getId())
                            .userEmail(user.getEmail())
                            .userFirstName(user.getFirstName())
                            .title(job.getTitle())
                            .message(job.getMessage())
                            .type(job.getNotificationType())
                            .channelType(NotificationJobPayload.ChannelType.APP)
                            .priority(NotificationPriority.LOW)
                            .eventType("ADMIN_BROADCAST")
                            .retryCount(0)
                            .build();

                    try {
                        notificationProducer.enqueueJob(appPayload);
                    } catch (Exception e) {
                        log.error("[BROADCAST APP ENQUEUE FAILED] JobID={} | UserID={} | Error={}", jobId, user.getId(), e.getMessage());
                        recipientStatusRepository.updateAppRecipientStatusNative(jobId, user.getId(), AppNotificationStatus.FAILED.name(), e.getMessage());
                        checkAndTriggerAtomicRecipientCompletion(jobId, user.getId());
                    }

                    if (user.getEmail() != null && !user.getEmail().isBlank()) {
                        batchItems.add(new com.example.queue.BulkEmailBatchPayload.RecipientItem(user.getId(), user.getEmail(), user.getFirstName()));
                        if (batchItems.size() >= emailBatchSize) {
                            enqueueBulkEmailBatch(jobId, job.getTitle(), job.getMessage(), batchItems);
                            batchItems = new java.util.ArrayList<>();
                        }
                    } else {
                        checkAndTriggerAtomicRecipientCompletion(jobId, user.getId());
                    }

                    lastId = user.getId();
                }

                if (!batchItems.isEmpty()) {
                    enqueueBulkEmailBatch(jobId, job.getTitle(), job.getMessage(), batchItems);
                }

                totalEnqueued = recipientStatusRepository.countByBroadcastJobIdNative(jobId);
                broadcastJobRepository.updateEnqueuedRecipientsNative(jobId, totalEnqueued, lastId);

                BroadcastJob updatedJob = broadcastJobRepository.findById(jobId).orElse(null);
                if (updatedJob != null) {
                    publishWebSocketProgress(toResponseDTO(updatedJob));
                }

                log.info("[BROADCAST ENQUEUE PROGRESS] JobID={} | Chunk Enqueued. LastUserId={} | Enqueued={}/{}",
                        jobId, lastId, totalEnqueued, job.getTotalRecipients());
            }

            totalEnqueued = recipientStatusRepository.countByBroadcastJobIdNative(jobId);
            if (isJobTestMode) {
                Long totalTarget = Math.min(totalEnqueued, (long) testRecipientLimit);
                broadcastJobRepository.updateEnqueuedRecipientsNative(jobId, totalTarget, lastId);
            } else {
                broadcastJobRepository.updateEnqueuedRecipientsNative(jobId, totalEnqueued, lastId);
            }

            log.info("[BROADCAST ENQUEUING COMPLETE] JobID={} | Total Enqueued={}/{} | Waiting for consumers to finish recipient status updates.",
                    jobId, totalEnqueued, job.getTotalRecipients());

        } catch (Exception e) {
            log.error("[BROADCAST FATAL FAILURE] JobID={} | Error={}", jobId, e.getMessage(), e);
            job.setStatus(BroadcastJobStatus.FAILED);
            job.setLastError(e.getMessage());
            job.setCompletedAt(LocalDateTime.now());
            job.setUpdatedAt(LocalDateTime.now());
            broadcastJobRepository.save(job);
            progressPublishTimes.remove(jobId);

            BroadcastJobResponseDTO failedDto = toResponseDTO(job);
            publishWebSocketProgress(failedDto);

            if (adminNotificationService != null) {
                adminNotificationService.publishBroadcastLifecycleNotification(
                        jobId,
                        BroadcastJobStatus.FAILED,
                        job.getTitle(),
                        "Notification broadcast failed: " + e.getMessage()
                );
            }
        }
    }

    private void enqueueBulkEmailBatch(Long jobId, String title, String message, List<com.example.queue.BulkEmailBatchPayload.RecipientItem> items) {
        com.example.queue.BulkEmailBatchPayload batchPayload = com.example.queue.BulkEmailBatchPayload.builder()
                .batchId(UUID.randomUUID().toString())
                .broadcastJobId(jobId)
                .title(title)
                .message(message)
                .recipients(items)
                .build();
        try {
            notificationProducer.enqueueEmailBatch(batchPayload);
        } catch (Exception e) {
            log.error("[BROADCAST EMAIL BATCH ENQUEUE FAILED] JobID={} | Size={} | Error={}", jobId, items.size(), e.getMessage());
            List<Long> failedIds = items.stream().map(com.example.queue.BulkEmailBatchPayload.RecipientItem::getUserId).toList();
            recordEmailRecipientBatchStatus(jobId, failedIds, RecipientEmailStatus.FAILED, e.getMessage());
        }
    }

    @Override
    public void recordRecipientSuccess(Long broadcastJobId) {
        // Maintained for backward compatibility interface contract
    }

    @Override
    public void recordRecipientFailure(Long broadcastJobId, String errorMessage) {
        if (broadcastJobId == null) return;
        broadcastJobRepository.incrementJobCountersNative(broadcastJobId, 0, 1);
        int completedRows = broadcastJobRepository.tryMarkJobCompletedNative(broadcastJobId);
        if (completedRows == 1) {
            handleJobCompletion(broadcastJobId);
        } else {
            throttledProgressUpdate(broadcastJobId);
        }
    }

    @Override
    public void recordAppRecipientStatus(Long broadcastJobId, Long userId, AppNotificationStatus status, String error) {
        if (broadcastJobId == null || userId == null) return;
        recipientStatusRepository.updateAppRecipientStatusNative(broadcastJobId, userId, status.name(), error);
        checkAndTriggerAtomicRecipientCompletion(broadcastJobId, userId);
    }

    @Override
    public void recordEmailRecipientStatus(Long broadcastJobId, Long userId, RecipientEmailStatus status, String error) {
        if (broadcastJobId == null || userId == null) return;
        recipientStatusRepository.updateEmailRecipientStatusNative(broadcastJobId, userId, status.name(), error);
        checkAndTriggerAtomicRecipientCompletion(broadcastJobId, userId);
    }

    @Override
    public void recordAppRecipientBatchStatus(Long broadcastJobId, List<Long> userIds, AppNotificationStatus status, String error) {
        if (broadcastJobId == null || userIds == null || userIds.isEmpty()) return;
        recipientStatusRepository.updateAppRecipientStatusBatchNative(broadcastJobId, userIds, status.name(), error);
        checkAndTriggerAtomicBatchRecipientCompletion(broadcastJobId, userIds);
    }

    @Override
    public void recordEmailRecipientBatchStatus(Long broadcastJobId, List<Long> userIds, RecipientEmailStatus status, String error) {
        if (broadcastJobId == null || userIds == null || userIds.isEmpty()) return;
        recipientStatusRepository.updateEmailRecipientStatusBatchNative(broadcastJobId, userIds, status.name(), error);
        checkAndTriggerAtomicBatchRecipientCompletion(broadcastJobId, userIds);
    }

    private void checkAndTriggerAtomicBatchRecipientCompletion(Long broadcastJobId, List<Long> userIds) {
        if (broadcastJobId == null || userIds == null || userIds.isEmpty()) return;

        List<Long> transitionedUserIds = recipientStatusRepository.tryMarkAggregateProcessedBatchNative(broadcastJobId, userIds);

        if (transitionedUserIds != null && !transitionedUserIds.isEmpty()) {
            long successCount = recipientStatusRepository.countSuccessRecipientsInBatchNative(broadcastJobId, transitionedUserIds);
            long failCount = transitionedUserIds.size() - successCount;

            broadcastJobRepository.incrementJobCountersNative(broadcastJobId, (int) successCount, (int) failCount);

            int completedJobRows = broadcastJobRepository.tryMarkJobCompletedNative(broadcastJobId);
            if (completedJobRows == 1) {
                handleJobCompletion(broadcastJobId);
            } else {
                throttledProgressUpdate(broadcastJobId);
            }
        }
    }

    private void checkAndTriggerAtomicRecipientCompletion(Long broadcastJobId, Long userId) {
        // Perform atomic transition: aggregate_processed = false -> true
        int transition = recipientStatusRepository.tryMarkAggregateProcessedNative(broadcastJobId, userId);
        if (transition == 1) {
            // Exactly ONE thread wins the atomic transition for this recipient
            Boolean isSuccess = recipientStatusRepository.isRecipientSuccessNative(broadcastJobId, userId);
            boolean success = Boolean.TRUE.equals(isSuccess);

            // Native atomic counter update on broadcast_jobs table in PostgreSQL
            broadcastJobRepository.incrementJobCountersNative(broadcastJobId, success ? 1 : 0, success ? 0 : 1);

            // Check if all recipients have finished and attempt atomic job completion
            int completedJobRows = broadcastJobRepository.tryMarkJobCompletedNative(broadcastJobId);
            if (completedJobRows == 1) {
                handleJobCompletion(broadcastJobId);
            } else {
                throttledProgressUpdate(broadcastJobId);
            }
        }
    }

    private void throttledProgressUpdate(Long jobId) {
        long now = System.currentTimeMillis();
        AtomicLong lastPublishTime = progressPublishTimes.computeIfAbsent(jobId, k -> new AtomicLong(0L));
        long lastPublish = lastPublishTime.get();
        if (now - lastPublish >= WS_THROTTLE_INTERVAL_MS) {
            if (lastPublishTime.compareAndSet(lastPublish, now)) {
                broadcastJobRepository.findById(jobId).ifPresent(job -> {
                    publishWebSocketProgress(toResponseDTO(job));
                });
            }
        }
    }

    private void handleJobCompletion(Long broadcastJobId) {
        BroadcastJob currentJob = broadcastJobRepository.findById(broadcastJobId).orElse(null);
        if (currentJob == null) return;

        progressPublishTimes.remove(broadcastJobId);

        BroadcastJobResponseDTO finalDto = toResponseDTO(currentJob);
        publishWebSocketProgress(finalDto);

        if (adminNotificationService != null) {
            String messageSummary = currentJob.getStatus() == BroadcastJobStatus.COMPLETED
                    ? "Notification broadcast completed successfully (" + currentJob.getSuccessfulRecipients() + "/" + currentJob.getTotalRecipients() + ")"
                    : "Notification broadcast completed with failures (" + currentJob.getSuccessfulRecipients() + " successful, " + currentJob.getFailedRecipients() + " failed)";

            adminNotificationService.publishBroadcastLifecycleNotification(
                    broadcastJobId,
                    currentJob.getStatus(),
                    currentJob.getTitle(),
                    messageSummary
            );
        }

        log.info("[BROADCAST ALL RECIPIENTS FINISHED] JobID={} | Status={} | Processed={}/{} | Success={} | Failed={}",
                broadcastJobId, currentJob.getStatus(), currentJob.getProcessedRecipients(), currentJob.getTotalRecipients(), currentJob.getSuccessfulRecipients(), currentJob.getFailedRecipients());
    }

    private void publishWebSocketProgress(BroadcastJobResponseDTO dto) {
        if (dto != null) {
            try {
                messagingTemplate.convertAndSend("/topic/admin/broadcast-progress", dto);
            } catch (Exception e) {
                log.warn("[WEBSOCKET PUBLISH FAILED] JobID={} | Error={}", dto.getId(), e.getMessage());
            }
        }
    }

    @Override
    public BroadcastJobResponseDTO getActiveBroadcastJob() {
        Optional<BroadcastJob> jobOpt = broadcastJobRepository.findFirstByStatusInOrderByCreatedAtDesc(
                List.of(BroadcastJobStatus.IN_PROGRESS, BroadcastJobStatus.PENDING, BroadcastJobStatus.INTERRUPTED)
        );
        return jobOpt.map(this::toResponseDTO).orElse(null);
    }

    @Override
    public BroadcastJobResponseDTO getBroadcastJobById(Long jobId) {
        return broadcastJobRepository.findById(jobId).map(this::toResponseDTO).orElse(null);
    }

    @Override
    public Page<BroadcastJobResponseDTO> getBroadcastHistory(Pageable pageable) {
        Page<BroadcastJob> page = broadcastJobRepository.findAllByOrderByCreatedAtDesc(pageable);
        List<BroadcastJobResponseDTO> dtos = page.getContent().stream().map(this::toResponseDTO).toList();
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    public Page<BroadcastRecipientStatusResponseDTO> getBroadcastRecipients(
            Long broadcastJobId,
            String search,
            AppNotificationStatus appStatus,
            RecipientEmailStatus emailStatus,
            Pageable pageable) {

        Page<BroadcastRecipientStatus> page = recipientStatusRepository.searchRecipients(
                broadcastJobId, search, appStatus, emailStatus, pageable
        );

        List<BroadcastRecipientStatusResponseDTO> dtos = page.getContent().stream()
                .map(this::toRecipientDTO)
                .toList();

        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    private BroadcastJobResponseDTO toResponseDTO(BroadcastJob job) {
        long total = job.getTotalRecipients() != null ? job.getTotalRecipients() : 0L;
        long rawEnqueued = job.getEnqueuedRecipients() != null ? job.getEnqueuedRecipients() : 0L;
        long rawProcessed = job.getProcessedRecipients() != null ? job.getProcessedRecipients() : 0L;
        long rawSuccessful = job.getSuccessfulRecipients() != null ? job.getSuccessfulRecipients() : 0L;
        long rawFailed = job.getFailedRecipients() != null ? job.getFailedRecipients() : 0L;

        // Mathematical invariants: enqueued <= total, processed <= enqueued, processed = successful + failed
        long enqueued = (total > 0) ? Math.min(total, rawEnqueued) : rawEnqueued;
        long processed = (total > 0) ? Math.min(enqueued, Math.min(total, rawProcessed)) : rawProcessed;
        long successful = Math.min(processed, rawSuccessful);
        long failed = Math.min(processed - successful, rawFailed);

        double pct = (total > 0)
                ? ((double) processed / total) * 100.0
                : 0.0;

        long elapsedSeconds = 0;
        if (job.getStartedAt() != null) {
            LocalDateTime end = job.getCompletedAt() != null ? job.getCompletedAt() : LocalDateTime.now();
            elapsedSeconds = java.time.Duration.between(job.getStartedAt(), end).getSeconds();
        }

        double currentThroughput = (elapsedSeconds > 0 && processed > 0)
                ? (double) processed / elapsedSeconds
                : 0.0;

        long remaining = Math.max(0L, total - processed);
        Long estimatedRemainingSeconds = (currentThroughput > 0 && remaining > 0)
                ? (long) Math.ceil(remaining / currentThroughput)
                : 0L;

        boolean isJobTestMode = Boolean.TRUE.equals(job.getIsTestMode());

        return BroadcastJobResponseDTO.builder()
                .id(job.getId())
                .title(job.getTitle())
                .message(job.getMessage())
                .notificationType(job.getNotificationType())
                .status(job.getStatus())
                .totalRecipients(total)
                .enqueuedRecipients(enqueued)
                .processedRecipients(processed)
                .successfulRecipients(successful)
                .failedRecipients(failed)
                .progressPercentage(Math.min(100.0, Math.round(pct * 100.0) / 100.0))
                .currentThroughput(Math.round(currentThroughput * 10.0) / 10.0)
                .estimatedRemainingSeconds(estimatedRemainingSeconds)
                .startedAt(job.getStartedAt())
                .completedAt(job.getCompletedAt())
                .lastError(job.getLastError())
                .createdByAdminId(job.getCreatedByAdminId())
                .isTestMode(isJobTestMode)
                .testRecipientLimit(isJobTestMode ? testRecipientLimit : null)
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .build();
    }

    private BroadcastRecipientStatusResponseDTO toRecipientDTO(BroadcastRecipientStatus r) {
        return BroadcastRecipientStatusResponseDTO.builder()
                .id(r.getId())
                .broadcastJobId(r.getBroadcastJobId())
                .userId(r.getUserId())
                .userEmail(r.getUserEmail())
                .appNotificationStatus(r.getAppNotificationStatus())
                .emailStatus(r.getEmailStatus())
                .emailAttemptCount(r.getEmailAttemptCount())
                .lastError(r.getLastError())
                .emailError(r.getEmailError())
                .notificationProcessedAt(r.getNotificationProcessedAt())
                .emailQueuedAt(r.getEmailQueuedAt())
                .emailAcceptedAt(r.getEmailAcceptedAt())
                .emailDeliveredAt(r.getEmailDeliveredAt())
                .createdAt(r.getCreatedAt())
                .build();
    }
}
