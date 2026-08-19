package com.example.serviceimpl;

import com.example.dto.request.SuccessStoryCreateRequestDTO;
import com.example.dto.request.SuccessStoryUpdateRequestDTO;
import com.example.dto.response.SuccessStoryResponseDTO;
import com.example.model.NotificationPriority;
import com.example.model.NotificationType;
import com.example.model.SuccessStory;
import com.example.queue.NotificationJobPayload;
import com.example.queue.NotificationProducer;
import com.example.repository.SuccessStoryRepository;
import com.example.repository.UserRepository;
import com.example.repository.projection.UserBroadcastProjection;
import com.example.service.AdminAuditLogService;
import com.example.service.AdminNotificationService;
import com.example.service.SuccessStoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import java.time.LocalDateTime;

import com.example.model.BroadcastJob;
import com.example.model.BroadcastJobStatus;
import com.example.queue.AppNotificationBatchPayload;
import com.example.repository.BroadcastJobRepository;
import com.example.repository.BroadcastRecipientStatusRepository;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class SuccessStoryServiceImpl implements SuccessStoryService {

    private final SuccessStoryRepository successStoryRepository;
    private final AdminAuditLogService adminAuditLogService;
    private final UserRepository userRepository;
    private final NotificationProducer notificationProducer;
    private final AdminNotificationService adminNotificationService;
    private final BroadcastJobRepository broadcastJobRepository;
    private final BroadcastRecipientStatusRepository recipientStatusRepository;

    @Value("${broadcast.chunk-size:1000}")
    private int configuredChunkSize = 1000;

    @Override
    @Transactional
    public SuccessStoryResponseDTO createSuccessStory(SuccessStoryCreateRequestDTO dto, Long adminId) {
        validateCreateDto(dto);

        SuccessStory story = SuccessStory.builder()
                .partnerOneName(dto.getPartnerOneName().trim())
                .partnerTwoName(dto.getPartnerTwoName().trim())
                .partnerOneImageUrl(dto.getPartnerOneImageUrl())
                .partnerTwoImageUrl(dto.getPartnerTwoImageUrl())
                .coupleImageUrl(dto.getCoupleImageUrl())
                .shortStory(dto.getShortStory().trim())
                .fullStory(dto.getFullStory() != null ? dto.getFullStory().trim() : null)
                .weddingDate(dto.getWeddingDate())
                .location(dto.getLocation() != null ? dto.getLocation().trim() : null)
                .consentGiven(Boolean.TRUE.equals(dto.getConsentGiven()))
                .isPublished(false)
                .displayOrder(dto.getDisplayOrder() != null ? dto.getDisplayOrder() : 0)
                .publishVersion(0)
                .createdBy(adminId)
                .updatedBy(adminId)
                .build();

        SuccessStory saved = successStoryRepository.save(story);

        // Audit Log (Creating story -> NO notification)
        if (adminId != null) {
            adminAuditLogService.log(
                    adminId,
                    "SUCCESS_STORIES",
                    "CREATE_SUCCESS_STORY",
                    "SuccessStory",
                    saved.getId(),
                    "Created success story for " + saved.getPartnerOneName() + " & " + saved.getPartnerTwoName(),
                    null,
                    saved.getPartnerOneName() + " & " + saved.getPartnerTwoName()
            );
        }

        return mapToResponseDto(saved, true);
    }

    @Override
    @Transactional
    public SuccessStoryResponseDTO updateSuccessStory(Long id, SuccessStoryUpdateRequestDTO dto, Long adminId) {
        validateUpdateDto(dto);

        SuccessStory story = successStoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Success story not found with ID: " + id));

        String oldSummary = story.getPartnerOneName() + " & " + story.getPartnerTwoName();

        story.setPartnerOneName(dto.getPartnerOneName().trim());
        story.setPartnerTwoName(dto.getPartnerTwoName().trim());
        story.setPartnerOneImageUrl(dto.getPartnerOneImageUrl());
        story.setPartnerTwoImageUrl(dto.getPartnerTwoImageUrl());
        story.setCoupleImageUrl(dto.getCoupleImageUrl());
        story.setShortStory(dto.getShortStory().trim());
        story.setFullStory(dto.getFullStory() != null ? dto.getFullStory().trim() : null);
        story.setWeddingDate(dto.getWeddingDate());
        story.setLocation(dto.getLocation() != null ? dto.getLocation().trim() : null);
        story.setDisplayOrder(dto.getDisplayOrder() != null ? dto.getDisplayOrder() : 0);
        story.setUpdatedBy(adminId);

        boolean newConsent = Boolean.TRUE.equals(dto.getConsentGiven());
        story.setConsentGiven(newConsent);

        // BUSINESS RULE: If consent is revoked (set to false), automatically unpublish the story
        if (!newConsent && Boolean.TRUE.equals(story.getIsPublished())) {
            story.setIsPublished(false);
        }

        SuccessStory updated = successStoryRepository.save(story);

        // Audit Log (Updating story -> NO notification)
        if (adminId != null) {
            adminAuditLogService.log(
                    adminId,
                    "SUCCESS_STORIES",
                    "UPDATE_SUCCESS_STORY",
                    "SuccessStory",
                    updated.getId(),
                    "Updated success story ID: " + updated.getId(),
                    oldSummary,
                    updated.getPartnerOneName() + " & " + updated.getPartnerTwoName()
            );
        }

        return mapToResponseDto(updated, true);
    }

    @Override
    @Transactional(readOnly = true)
    public SuccessStoryResponseDTO getSuccessStoryById(Long id) {
        SuccessStory story = successStoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Success story not found with ID: " + id));
        return mapToResponseDto(story, true);
    }

    @Override
    @Transactional(readOnly = true)
    public SuccessStoryResponseDTO getPublicSuccessStoryById(Long id) {
        SuccessStory story = successStoryRepository.findByIdAndIsPublishedTrueAndConsentGivenTrue(id)
                .orElseThrow(() -> new IllegalArgumentException("Success story not found or not available"));
        return mapToResponseDto(story, false);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SuccessStoryResponseDTO> getAdminSuccessStories(String search, Boolean published, Pageable pageable) {
        Pageable effectivePageable = pageable;
        if (!pageable.getSort().isSorted()) {
            Sort defaultSort = Boolean.TRUE.equals(published)
                    ? Sort.by(Sort.Order.desc("publishedAt"), Sort.Order.desc("id"))
                    : Sort.by(Sort.Order.desc("createdAt"), Sort.Order.desc("id"));
            effectivePageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }
        return successStoryRepository.searchAdminStories(search, published, effectivePageable)
                .map(story -> mapToResponseDto(story, true));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SuccessStoryResponseDTO> getPublishedSuccessStories(Pageable pageable) {
        return successStoryRepository.findPublicStoriesOrderedByRecency(pageable)
                .map(story -> mapToResponseDto(story, false));
    }

    @Override
    @Transactional
    public void deleteSuccessStory(Long id, Long adminId) {
        SuccessStory story = successStoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Success story not found with ID: " + id));

        successStoryRepository.delete(story);

        if (adminId != null) {
            adminAuditLogService.log(
                    adminId,
                    "SUCCESS_STORIES",
                    "DELETE_SUCCESS_STORY",
                    "SuccessStory",
                    id,
                    "Deleted success story for " + story.getPartnerOneName() + " & " + story.getPartnerTwoName(),
                    story.getPartnerOneName() + " & " + story.getPartnerTwoName(),
                    null
            );
        }
    }

    @Override
    @Transactional
    public SuccessStoryResponseDTO publishSuccessStory(Long id, Long adminId) {
        SuccessStory story = successStoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Success story not found with ID: " + id));

        // BUSINESS RULE: Consent MUST be given before publishing
        if (!Boolean.TRUE.equals(story.getConsentGiven())) {
            throw new IllegalStateException("Consent from couple is required before publishing a success story.");
        }

        boolean wasAlreadyPublished = Boolean.TRUE.equals(story.getIsPublished());

        if (wasAlreadyPublished) {
            log.info("[STORY PUBLISH] Story ID={} is already published (v{}). Skipping duplicate notification event.", id, story.getPublishVersion());
            return mapToResponseDto(story, true);
        }

        story.setIsPublished(true);
        story.setPublishedAt(LocalDateTime.now());
        int newVersion = (story.getPublishVersion() != null ? story.getPublishVersion() : 0) + 1;
        story.setPublishVersion(newVersion);
        story.setUpdatedBy(adminId);

        SuccessStory saved = successStoryRepository.save(story);

        log.info("[STORY PUBLISH] Story ID={} published successfully (v{}) | Partner1={} | Partner2={}",
                saved.getId(), newVersion, saved.getPartnerOneName(), saved.getPartnerTwoName());

        Long totalEligibleUsers = userRepository.countByIsActiveTrueAndIsDeletedFalse();
        String title = "New Success Story ❤️";
        String message = "Meet " + saved.getPartnerOneName() + " & " + saved.getPartnerTwoName() + "! Read their inspiring Gathbandhan success story.";

        BroadcastJob job = BroadcastJob.builder()
                .title(title)
                .message(message)
                .notificationType(NotificationType.ANNOUNCEMENT)
                .status(BroadcastJobStatus.IN_PROGRESS)
                .lastProcessedUserId(0L)
                .totalRecipients(totalEligibleUsers != null ? totalEligibleUsers : 0L)
                .enqueuedRecipients(0L)
                .processedRecipients(0L)
                .successfulRecipients(0L)
                .failedRecipients(0L)
                .createdByAdminId(adminId)
                .isTestMode(false)
                .build();
        BroadcastJob savedJob = broadcastJobRepository.save(job);

        final Long storyIdToNotify = saved.getId();
        final int versionToNotify = newVersion;
        final String partner1 = saved.getPartnerOneName();
        final String partner2 = saved.getPartnerTwoName();
        final Long jobIdToNotify = savedJob != null ? savedJob.getId() : null;

        // TRANSACTION SAFETY: Register synchronization callback to dispatch notifications AFTER_COMMIT
        Runnable asyncDispatch = () -> {
            dispatchPublishNotification(jobIdToNotify, storyIdToNotify, versionToNotify, partner1, partner2);
            try {
                adminNotificationService.publishSuccessStoryPublishedNotification(storyIdToNotify, partner1, partner2);
            } catch (Exception e) {
                log.error("[STORY ADMIN NOTIFICATION FAILED] StoryID={} | Error={}", storyIdToNotify, e.getMessage());
            }
        };

        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    CompletableFuture.runAsync(asyncDispatch);
                }
            });
        } else {
            CompletableFuture.runAsync(asyncDispatch);
        }

        if (adminId != null) {
            adminAuditLogService.log(
                    adminId,
                    "SUCCESS_STORIES",
                    "PUBLISH_SUCCESS_STORY",
                    "SuccessStory",
                    id,
                    "Published success story ID: " + id + " (v" + versionToNotify + ")",
                    "DRAFT",
                    "PUBLISHED"
            );
        }

        return mapToResponseDto(saved, true);
    }

    @Override
    @Transactional
    public SuccessStoryResponseDTO unpublishSuccessStory(Long id, Long adminId) {
        SuccessStory story = successStoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Success story not found with ID: " + id));

        story.setIsPublished(false);
        story.setUpdatedBy(adminId);
        SuccessStory saved = successStoryRepository.save(story);

        log.info("[STORY UNPUBLISH] Story ID={} unpublished", id);

        // Audit Log (Unpublishing story -> NO notification)
        if (adminId != null) {
            adminAuditLogService.log(
                    adminId,
                    "SUCCESS_STORIES",
                    "UNPUBLISH_SUCCESS_STORY",
                    "SuccessStory",
                    id,
                    "Unpublished success story ID: " + id,
                    "PUBLISHED",
                    "DRAFT"
            );
        }

        return mapToResponseDto(saved, true);
    }

    /**
     * Asynchronously dispatches publication broadcast notifications to active users via RabbitMQ.
     * Uses keyset pagination chunking and deterministic idempotency keys.
     */
    public void dispatchPublishNotification(Long storyId, int version, String partnerOneName, String partnerTwoName) {
        dispatchPublishNotification(null, storyId, version, partnerOneName, partnerTwoName);
    }

    public void dispatchPublishNotification(Long jobId, Long storyId, int version, String partnerOneName, String partnerTwoName) {
        BroadcastJob job = jobId != null ? broadcastJobRepository.findById(jobId).orElse(null) : null;
        Long lastId = job != null && job.getLastProcessedUserId() != null ? job.getLastProcessedUserId() : 0L;
        Long totalEligibleUsers = job != null && job.getTotalRecipients() != null ? job.getTotalRecipients() : userRepository.countByIsActiveTrueAndIsDeletedFalse();

        log.info("[STORY DISPATCH START] StoryID={} | JobID={} | Version={} | TotalEligibleUsers={} | StartingFromLastUserId={}",
                storyId, jobId, version, totalEligibleUsers, lastId);

        String title = "New Success Story ❤️";
        String message = "Meet " + partnerOneName + " & " + partnerTwoName + "! Read their inspiring Gathbandhan success story.";

        long totalProcessed = 0;
        long totalEnqueued = job != null && job.getEnqueuedRecipients() != null ? job.getEnqueuedRecipients() : 0;
        Long firstUserIdInDispatch = null;
        Long lastUserIdInDispatch = lastId;

        int chunkSize = configuredChunkSize > 0 ? configuredChunkSize : 1000;

        try {
            while (true) {
                Pageable pageable = PageRequest.of(0, chunkSize);
                List<UserBroadcastProjection> chunk = userRepository.findActiveUsersProjectionChunkAfterId(lastId, pageable);

                if (chunk == null || chunk.isEmpty()) {
                    break;
                }

                Long chunkFirstUserId = chunk.get(0).getId();
                Long chunkLastUserId = chunk.get(chunk.size() - 1).getId();
                if (firstUserIdInDispatch == null) {
                    firstUserIdInDispatch = chunkFirstUserId;
                }

                log.info("[STORY DISPATCH CHUNK] StoryID={} | Version={} | FirstUserId={} | LastUserId={} | ChunkSize={}",
                        storyId, version, chunkFirstUserId, chunkLastUserId, chunk.size());

                List<AppNotificationBatchPayload.RecipientItem> recipients = chunk.stream()
                        .map(u -> new AppNotificationBatchPayload.RecipientItem(u.getId()))
                        .toList();

                String batchId = "STORY_BATCH_" + storyId + "_V" + version + "_" + chunkLastUserId;

                AppNotificationBatchPayload batchPayload = AppNotificationBatchPayload.builder()
                        .batchId(batchId)
                        .storyId(storyId)
                        .storyVersion(version)
                        .broadcastJobId(jobId)
                        .title(title)
                        .message(message)
                        .type(NotificationType.ANNOUNCEMENT)
                        .priority(NotificationPriority.MEDIUM)
                        .eventType("SUCCESS_STORY_PUBLISHED")
                        .referenceId(storyId)
                        .recipients(recipients)
                        .build();

                if (jobId != null) {
                    List<Long> chunkUserIds = chunk.stream().map(UserBroadcastProjection::getId).toList();
                    try {
                        recipientStatusRepository.bulkInsertRecipientStatusesOnConflict(jobId, chunkUserIds);
                    } catch (Exception e) {
                        log.debug("[STORY DISPATCH RECIPIENT STATUS INSERT] JobID={} | Error={}", jobId, e.getMessage());
                    }
                }

                // Enqueue APP batch to RabbitMQ
                notificationProducer.enqueueAppBatch(batchPayload);

                for (UserBroadcastProjection user : chunk) {
                    String idempotencyKey = "SUCCESS_STORY_PUBLISHED_" + storyId + "_V" + version + "_USER_" + user.getId();
                    log.info("[STORY NOTIFICATION ENQUEUED] Key={} | UserID={}", idempotencyKey, user.getId());
                }

                totalProcessed += chunk.size();
                totalEnqueued += chunk.size();
                lastId = chunkLastUserId;
                lastUserIdInDispatch = lastId;

                if (jobId != null) {
                    try {
                        broadcastJobRepository.updateEnqueuedRecipientsNative(jobId, totalEnqueued, lastId);
                    } catch (Exception e) {
                        log.debug("[STORY DISPATCH CHECKPOINT UPDATE] JobID={} | Error={}", jobId, e.getMessage());
                    }
                }

                log.info("[STORY DISPATCH PROGRESS] StoryID={} | ProcessedUsers={} | EnqueuedUsers={} | LastUserId={}",
                        storyId, totalProcessed, totalEnqueued, lastId);

                if (chunk.size() < chunkSize) {
                    break;
                }
            }

            if (jobId != null) {
                BroadcastJob completedJob = broadcastJobRepository.findById(jobId).orElse(null);
                if (completedJob != null) {
                    completedJob.setStatus(BroadcastJobStatus.COMPLETED);
                    completedJob.setCompletedAt(java.time.LocalDateTime.now());
                    completedJob.setLastProcessedUserId(lastUserIdInDispatch);
                    broadcastJobRepository.save(completedJob);
                }
            }

            log.info("[STORY DISPATCH COMPLETE] StoryID={} | Version={} | TotalEligibleUsers={} | TotalProcessed={} | TotalEnqueued={} | FirstUserId={} | LastUserId={}",
                    storyId, version, totalEligibleUsers, totalProcessed, totalEnqueued, firstUserIdInDispatch, lastUserIdInDispatch);

        } catch (Exception e) {
            log.error("[STORY DISPATCH FAILED] StoryID={} | Version={} | LastProcessedUserId={} | TotalEnqueued={} | Error={}",
                    storyId, version, lastId, totalEnqueued, e.getMessage(), e);

            if (jobId != null) {
                BroadcastJob failedJob = broadcastJobRepository.findById(jobId).orElse(null);
                if (failedJob != null) {
                    failedJob.setStatus(BroadcastJobStatus.INTERRUPTED);
                    failedJob.setLastError(e.getMessage());
                    failedJob.setLastProcessedUserId(lastId);
                    broadcastJobRepository.save(failedJob);
                }
            }
        }
    }

    private void validateCreateDto(SuccessStoryCreateRequestDTO dto) {
        if (dto.getPartnerOneName() == null || dto.getPartnerOneName().trim().isEmpty()) {
            throw new IllegalArgumentException("Partner one name is required");
        }
        if (dto.getPartnerTwoName() == null || dto.getPartnerTwoName().trim().isEmpty()) {
            throw new IllegalArgumentException("Partner two name is required");
        }
        if (dto.getShortStory() == null || dto.getShortStory().trim().isEmpty()) {
            throw new IllegalArgumentException("Short story is required");
        }
        if (dto.getShortStory().length() > 1000) {
            throw new IllegalArgumentException("Short story must not exceed 1000 characters");
        }
        if (dto.getDisplayOrder() != null && dto.getDisplayOrder() < 0) {
            throw new IllegalArgumentException("Display order cannot be negative");
        }
    }

    private void validateUpdateDto(SuccessStoryUpdateRequestDTO dto) {
        if (dto.getPartnerOneName() == null || dto.getPartnerOneName().trim().isEmpty()) {
            throw new IllegalArgumentException("Partner one name is required");
        }
        if (dto.getPartnerTwoName() == null || dto.getPartnerTwoName().trim().isEmpty()) {
            throw new IllegalArgumentException("Partner two name is required");
        }
        if (dto.getShortStory() == null || dto.getShortStory().trim().isEmpty()) {
            throw new IllegalArgumentException("Short story is required");
        }
        if (dto.getShortStory().length() > 1000) {
            throw new IllegalArgumentException("Short story must not exceed 1000 characters");
        }
        if (dto.getDisplayOrder() != null && dto.getDisplayOrder() < 0) {
            throw new IllegalArgumentException("Display order cannot be negative");
        }
    }

    private SuccessStoryResponseDTO mapToResponseDto(SuccessStory story, boolean includeAdminInfo) {
        return SuccessStoryResponseDTO.builder()
                .id(story.getId())
                .partnerOneName(story.getPartnerOneName())
                .partnerTwoName(story.getPartnerTwoName())
                .partnerOneImageUrl(story.getPartnerOneImageUrl())
                .partnerTwoImageUrl(story.getPartnerTwoImageUrl())
                .coupleImageUrl(story.getCoupleImageUrl())
                .shortStory(story.getShortStory())
                .fullStory(story.getFullStory())
                .weddingDate(story.getWeddingDate())
                .location(story.getLocation())
                .consentGiven(story.getConsentGiven())
                .isPublished(story.getIsPublished())
                .displayOrder(story.getDisplayOrder())
                .createdAt(story.getCreatedAt())
                .updatedAt(story.getUpdatedAt())
                .createdBy(includeAdminInfo ? story.getCreatedBy() : null)
                .updatedBy(includeAdminInfo ? story.getUpdatedBy() : null)
                .build();
    }
}
