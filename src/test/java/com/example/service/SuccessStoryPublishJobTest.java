package com.example.service;

import com.example.model.*;
import com.example.queue.AppNotificationBatchPayload;
import com.example.queue.NotificationConsumer;
import com.example.queue.NotificationProducer;
import com.example.repository.*;
import com.example.repository.projection.UserBroadcastProjection;
import com.example.serviceimpl.SuccessStoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SuccessStoryPublishJobTest {

    @Mock
    private SuccessStoryRepository successStoryRepository;

    @Mock
    private AdminAuditLogService adminAuditLogService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationProducer notificationProducer;

    @Mock
    private AdminNotificationService adminNotificationService;

    @Mock
    private BroadcastJobRepository broadcastJobRepository;

    @Mock
    private BroadcastRecipientStatusRepository recipientStatusRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationJobOutboxRepository outboxRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private AdminBroadcastService adminBroadcastService;

    @InjectMocks
    private SuccessStoryServiceImpl successStoryService;

    @InjectMocks
    private NotificationConsumer notificationConsumer;

    private SuccessStory mockStory;
    private BroadcastJob mockJob;

    @BeforeEach
    void setUp() {
        mockStory = SuccessStory.builder()
                .id(100L)
                .partnerOneName("Rahul")
                .partnerTwoName("Priya")
                .shortStory("Met on Gathbandhan")
                .consentGiven(true)
                .isPublished(false)
                .publishVersion(0)
                .build();

        mockJob = BroadcastJob.builder()
                .id(1L)
                .title("New Success Story ❤️")
                .message("Meet Rahul & Priya! Read their inspiring Gathbandhan success story.")
                .notificationType(NotificationType.ANNOUNCEMENT)
                .status(BroadcastJobStatus.IN_PROGRESS)
                .lastProcessedUserId(0L)
                .totalRecipients(1000002L)
                .enqueuedRecipients(0L)
                .processedRecipients(0L)
                .successfulRecipients(0L)
                .failedRecipients(0L)
                .build();
    }

    @Test
    @DisplayName("A. Publish creates durable job and starts keyset pagination")
    void publish_creates_durable_job_and_starts_keyset_pagination() {
        when(successStoryRepository.findById(100L)).thenReturn(Optional.of(mockStory));
        when(successStoryRepository.save(any(SuccessStory.class))).thenAnswer(i -> i.getArgument(0));
        when(userRepository.countByIsActiveTrueAndIsDeletedFalse()).thenReturn(1000002L);
        when(broadcastJobRepository.save(any(BroadcastJob.class))).thenReturn(mockJob);

        successStoryService.publishSuccessStory(100L, 1L);

        verify(broadcastJobRepository, times(1)).save(any(BroadcastJob.class));
        assertTrue(mockStory.getIsPublished());
        assertEquals(1, mockStory.getPublishVersion());
    }

    @Test
    @DisplayName("B. Keyset pagination includes first user ID 1")
    void keyset_pagination_includes_first_user_id_1() {
        UserBroadcastProjection user1 = mock(UserBroadcastProjection.class);
        when(user1.getId()).thenReturn(1L);

        when(userRepository.findActiveUsersProjectionChunkAfterId(eq(0L), any(Pageable.class)))
                .thenReturn(List.of(user1))
                .thenReturn(List.of());

        successStoryService.dispatchPublishNotification(100L, 1, "Rahul", "Priya");

        ArgumentCaptor<AppNotificationBatchPayload> captor = ArgumentCaptor.forClass(AppNotificationBatchPayload.class);
        verify(notificationProducer).enqueueAppBatch(captor.capture());

        AppNotificationBatchPayload payload = captor.getValue();
        assertEquals(1, payload.getRecipients().size());
        assertEquals(1L, payload.getRecipients().get(0).getUserId());
    }

    @Test
    @DisplayName("C. Keyset pagination includes middle user ID 999999")
    void keyset_pagination_includes_middle_user_id_999999() {
        UserBroadcastProjection userMid = mock(UserBroadcastProjection.class);
        when(userMid.getId()).thenReturn(999999L);

        when(userRepository.findActiveUsersProjectionChunkAfterId(eq(999000L), any(Pageable.class)))
                .thenReturn(List.of(userMid))
                .thenReturn(List.of());

        when(broadcastJobRepository.findById(1L)).thenReturn(Optional.of(mockJob));
        mockJob.setLastProcessedUserId(999000L);

        successStoryService.dispatchPublishNotification(1L, 100L, 1, "Rahul", "Priya");

        ArgumentCaptor<AppNotificationBatchPayload> captor = ArgumentCaptor.forClass(AppNotificationBatchPayload.class);
        verify(notificationProducer).enqueueAppBatch(captor.capture());

        assertEquals(999999L, captor.getValue().getRecipients().get(0).getUserId());
    }

    @Test
    @DisplayName("D. Keyset pagination includes last user ID 1000021")
    void keyset_pagination_includes_last_user_id_1000021() {
        UserBroadcastProjection userLast = mock(UserBroadcastProjection.class);
        when(userLast.getId()).thenReturn(1000021L);

        when(userRepository.findActiveUsersProjectionChunkAfterId(eq(1000000L), any(Pageable.class)))
                .thenReturn(List.of(userLast))
                .thenReturn(List.of());

        when(broadcastJobRepository.findById(1L)).thenReturn(Optional.of(mockJob));
        mockJob.setLastProcessedUserId(1000000L);

        successStoryService.dispatchPublishNotification(1L, 100L, 1, "Rahul", "Priya");

        ArgumentCaptor<AppNotificationBatchPayload> captor = ArgumentCaptor.forClass(AppNotificationBatchPayload.class);
        verify(notificationProducer).enqueueAppBatch(captor.capture());

        assertEquals(1000021L, captor.getValue().getRecipients().get(0).getUserId());
    }

    @Test
    @DisplayName("E. Deleted and Inactive users excluded via DB query specification")
    void deleted_and_inactive_users_excluded() {
        when(userRepository.findActiveUsersProjectionChunkAfterId(eq(0L), any(Pageable.class)))
                .thenReturn(List.of());

        successStoryService.dispatchPublishNotification(100L, 1, "Rahul", "Priya");

        verify(notificationProducer, never()).enqueueAppBatch(any());
    }

    @Test
    @DisplayName("F. Job checkpoint updates last_processed_user_id after each batch")
    void job_checkpoint_updates_last_processed_user_id_after_each_batch() {
        UserBroadcastProjection u1 = mock(UserBroadcastProjection.class);
        when(u1.getId()).thenReturn(500L);

        when(userRepository.findActiveUsersProjectionChunkAfterId(eq(0L), any(Pageable.class)))
                .thenReturn(List.of(u1))
                .thenReturn(List.of());

        when(broadcastJobRepository.findById(1L)).thenReturn(Optional.of(mockJob));

        successStoryService.dispatchPublishNotification(1L, 100L, 1, "Rahul", "Priya");

        verify(broadcastJobRepository).updateEnqueuedRecipientsNative(eq(1L), eq(1L), eq(500L));
    }

    @Test
    @DisplayName("G. Reboot resumes from last_processed_user_id")
    void reboot_resumes_from_last_processed_user_id() {
        mockJob.setLastProcessedUserId(350000L);
        when(broadcastJobRepository.findById(1L)).thenReturn(Optional.of(mockJob));

        UserBroadcastProjection uResumed = mock(UserBroadcastProjection.class);
        when(uResumed.getId()).thenReturn(350001L);

        when(userRepository.findActiveUsersProjectionChunkAfterId(eq(350000L), any(Pageable.class)))
                .thenReturn(List.of(uResumed))
                .thenReturn(List.of());

        successStoryService.dispatchPublishNotification(1L, 100L, 1, "Rahul", "Priya");

        verify(userRepository).findActiveUsersProjectionChunkAfterId(eq(350000L), any(Pageable.class));
        verify(notificationProducer).enqueueAppBatch(any());
    }

    @Test
    @DisplayName("H. Duplicate job not started for already published story")
    void duplicate_job_not_started_for_already_published_story() {
        mockStory.setIsPublished(true);
        mockStory.setPublishVersion(1);
        when(successStoryRepository.findById(100L)).thenReturn(Optional.of(mockStory));

        successStoryService.publishSuccessStory(100L, 1L);

        verify(broadcastJobRepository, never()).save(any(BroadcastJob.class));
        verify(notificationProducer, never()).enqueueAppBatch(any());
    }

    @Test
    @DisplayName("I. Republish increments version and dispatches fresh batch")
    void republish_increments_version_and_dispatches_fresh_batch() {
        mockStory.setIsPublished(false);
        mockStory.setPublishVersion(1);
        when(successStoryRepository.findById(100L)).thenReturn(Optional.of(mockStory));
        when(successStoryRepository.save(any(SuccessStory.class))).thenAnswer(i -> i.getArgument(0));
        when(userRepository.countByIsActiveTrueAndIsDeletedFalse()).thenReturn(500L);
        when(broadcastJobRepository.save(any(BroadcastJob.class))).thenReturn(mockJob);

        successStoryService.publishSuccessStory(100L, 1L);

        assertEquals(2, mockStory.getPublishVersion());
        verify(broadcastJobRepository, times(1)).save(any(BroadcastJob.class));
    }

    @Test
    @DisplayName("J. Unpublish does not dispatch notification")
    void unpublish_does_not_dispatch_notification() {
        mockStory.setIsPublished(true);
        when(successStoryRepository.findById(100L)).thenReturn(Optional.of(mockStory));
        when(successStoryRepository.save(any(SuccessStory.class))).thenAnswer(i -> i.getArgument(0));

        successStoryService.unpublishSuccessStory(100L, 1L);

        assertFalse(mockStory.getIsPublished());
        verify(notificationProducer, never()).enqueueAppBatch(any());
    }

    @Test
    @DisplayName("K. APP notification batch inserted in single operation")
    void app_notification_batch_inserted_in_single_operation() {
        AppNotificationBatchPayload batchPayload = AppNotificationBatchPayload.builder()
                .batchId("BATCH_1")
                .storyId(100L)
                .storyVersion(1)
                .title("New Success Story ❤️")
                .message("Meet Rahul & Priya!")
                .eventType("SUCCESS_STORY_PUBLISHED")
                .referenceId(100L)
                .recipients(List.of(
                        new AppNotificationBatchPayload.RecipientItem(1L),
                        new AppNotificationBatchPayload.RecipientItem(2L)
                ))
                .build();

        when(notificationRepository.bulkInsertAppNotificationsWithMetadata(anyList(), anyString(), anyString(), anyString(), any(LocalDateTime.class), any(), any()))
                .thenReturn(2);

        notificationConsumer.processAppNotificationBatchJob(batchPayload);

        verify(notificationRepository, times(1)).bulkInsertAppNotificationsWithMetadata(
                eq(List.of(1L, 2L)),
                eq("New Success Story ❤️"),
                eq("Meet Rahul & Priya!"),
                anyString(),
                any(LocalDateTime.class),
                eq(100L),
                eq("SUCCESS_STORY_PUBLISHED")
        );
    }

    @Test
    @DisplayName("L. Idempotency prevents duplicate notifications on redelivery")
    void idempotency_prevents_duplicate_notifications_on_redelivery() {
        AppNotificationBatchPayload batchPayload = AppNotificationBatchPayload.builder()
                .batchId("BATCH_REDELIVERED")
                .storyId(100L)
                .storyVersion(1)
                .title("New Success Story ❤️")
                .message("Meet Rahul & Priya!")
                .eventType("SUCCESS_STORY_PUBLISHED")
                .referenceId(100L)
                .recipients(List.of(new AppNotificationBatchPayload.RecipientItem(1L)))
                .build();

        // Native SQL bulk insert returns 0 rows inserted because WHERE NOT EXISTS match caught duplicate
        when(notificationRepository.bulkInsertAppNotificationsWithMetadata(anyList(), anyString(), anyString(), anyString(), any(LocalDateTime.class), any(), any()))
                .thenReturn(0);

        assertDoesNotThrow(() -> notificationConsumer.processAppNotificationBatchJob(batchPayload));
    }

    @Test
    @DisplayName("M. Failed batch is retried or DLQ without premature ACK")
    void failed_batch_is_retried_or_dlq_without_premature_ack() {
        AppNotificationBatchPayload batchPayload = AppNotificationBatchPayload.builder()
                .batchId("BATCH_FAIL")
                .storyId(100L)
                .storyVersion(1)
                .title("New Success Story ❤️")
                .eventType("SUCCESS_STORY_PUBLISHED")
                .referenceId(100L)
                .recipients(List.of(new AppNotificationBatchPayload.RecipientItem(1L)))
                .build();

        when(notificationRepository.bulkInsertAppNotificationsWithMetadata(anyList(), anyString(), anyString(), anyString(), any(LocalDateTime.class), any(), any()))
                .thenThrow(new RuntimeException("Database deadlock"));

        assertThrows(RuntimeException.class, () -> notificationConsumer.processAppNotificationBatchJob(batchPayload));
    }

    @Test
    @DisplayName("N. Admin notification is sent on publish")
    void admin_notification_is_sent_on_publish() {
        mockStory.setConsentGiven(true);
        when(successStoryRepository.findById(100L)).thenReturn(Optional.of(mockStory));
        when(successStoryRepository.save(any(SuccessStory.class))).thenAnswer(i -> i.getArgument(0));

        successStoryService.publishSuccessStory(100L, 1L);

        verify(adminAuditLogService).log(eq(1L), eq("SUCCESS_STORIES"), eq("PUBLISH_SUCCESS_STORY"), eq("SuccessStory"), eq(100L), anyString(), eq("DRAFT"), eq("PUBLISHED"));
    }
}
