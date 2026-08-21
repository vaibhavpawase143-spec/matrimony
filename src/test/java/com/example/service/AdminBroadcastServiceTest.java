package com.example.service;

import com.example.dto.response.BroadcastJobResponseDTO;
import com.example.model.BroadcastJob;
import com.example.model.BroadcastJobStatus;
import com.example.model.NotificationType;
import com.example.queue.NotificationJobPayload;
import com.example.queue.NotificationProducer;
import com.example.repository.BroadcastJobRepository;
import com.example.repository.BroadcastRecipientStatusRepository;
import com.example.repository.UserRepository;
import com.example.repository.projection.UserBroadcastProjection;
import com.example.serviceimpl.AdminBroadcastServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminBroadcastServiceTest {

    @Mock
    private BroadcastJobRepository broadcastJobRepository;

    @Mock
    private BroadcastRecipientStatusRepository recipientStatusRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationProducer notificationProducer;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private Executor bulkEmailExecutor;

    @InjectMocks
    private AdminBroadcastServiceImpl broadcastService;

    private UserBroadcastProjection user1;
    private UserBroadcastProjection user2;

    private UserBroadcastProjection createProjection(Long id, String email, String firstName) {
        return new UserBroadcastProjection() {
            @Override
            public Long getId() { return id; }
            @Override
            public String getEmail() { return email; }
            @Override
            public String getFirstName() { return firstName; }
        };
    }

    @BeforeEach
    void setUp() {
        user1 = createProjection(10L, "u10@example.com", "User10");
        user2 = createProjection(20L, "u20@example.com", "User20");

        lenient().doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(0);
            runnable.run();
            return null;
        }).when(bulkEmailExecutor).execute(any());
    }

    @Test
    @DisplayName("1. Initiate broadcast persists IN_PROGRESS job immediately and delegates chunked pagination to background executor")
    void testInitiateBroadcastChunkedProcessing() {
        when(userRepository.countByIsActiveTrueAndIsDeletedFalse()).thenReturn(2L);
        when(broadcastJobRepository.save(any(BroadcastJob.class))).thenAnswer(i -> {
            BroadcastJob j = i.getArgument(0);
            if (j.getId() == null) j.setId(1L);
            return j;
        });
        when(broadcastJobRepository.findById(1L)).thenReturn(Optional.of(BroadcastJob.builder()
                .id(1L)
                .title("Festival Special")
                .message("Enjoy 50% off")
                .notificationType(NotificationType.ANNOUNCEMENT)
                .status(BroadcastJobStatus.IN_PROGRESS)
                .lastProcessedUserId(0L)
                .totalRecipients(2L)
                .processedRecipients(0L)
                .successfulRecipients(0L)
                .failedRecipients(0L)
                .build()));

        when(userRepository.findActiveUsersProjectionChunkAfterId(eq(0L), any(Pageable.class)))
                .thenReturn(List.of(user1, user2));
        when(userRepository.findActiveUsersProjectionChunkAfterId(eq(20L), any(Pageable.class)))
                .thenReturn(List.of());

        Long broadcastId = broadcastService.initiateBroadcast("Festival Special", "Enjoy 50% off", NotificationType.ANNOUNCEMENT, 1L);

        assertEquals(1L, broadcastId);
        verify(notificationProducer, times(2)).enqueueJob(any(NotificationJobPayload.class));
        verify(notificationProducer, times(1)).enqueueEmailBatch(any(com.example.queue.BulkEmailBatchPayload.class));
    }

    @Test
    @DisplayName("2. Crash recovery resumes broadcast from lastProcessedUserId checkpoint")
    void testCrashRecoveryResumesFromCheckpoint() {
        BroadcastJob interruptedJob = BroadcastJob.builder()
                .id(2L)
                .title("Maintenance Alert")
                .message("Server upgrade tonight")
                .notificationType(NotificationType.ANNOUNCEMENT)
                .status(BroadcastJobStatus.IN_PROGRESS)
                .lastProcessedUserId(100L)
                .totalRecipients(2L)
                .processedRecipients(1L)
                .successfulRecipients(1L)
                .failedRecipients(0L)
                .build();

        when(broadcastJobRepository.findByStatusIn(any())).thenReturn(List.of(interruptedJob));
        when(broadcastJobRepository.findById(2L)).thenReturn(Optional.of(interruptedJob));

        when(userRepository.findActiveUsersProjectionChunkAfterId(eq(100L), any(Pageable.class)))
                .thenReturn(List.of(user2));
        when(userRepository.findActiveUsersProjectionChunkAfterId(eq(20L), any(Pageable.class)))
                .thenReturn(List.of());

        broadcastService.processPendingBroadcasts();

        verify(userRepository, times(1)).findActiveUsersProjectionChunkAfterId(eq(100L), any(Pageable.class));
        verify(notificationProducer, times(1)).enqueueJob(any(NotificationJobPayload.class));
        verify(notificationProducer, times(1)).enqueueEmailBatch(any(com.example.queue.BulkEmailBatchPayload.class));

        when(recipientStatusRepository.tryMarkAggregateProcessedNative(eq(2L), eq(user2.getId()))).thenReturn(1);
        when(recipientStatusRepository.isRecipientSuccessNative(eq(2L), eq(user2.getId()))).thenReturn(true);
        when(broadcastJobRepository.incrementJobCountersNative(eq(2L), eq(1), eq(0))).thenAnswer(inv -> {
            interruptedJob.setProcessedRecipients(interruptedJob.getProcessedRecipients() + 1);
            interruptedJob.setSuccessfulRecipients(interruptedJob.getSuccessfulRecipients() + 1);
            return 1;
        });
        when(broadcastJobRepository.tryMarkJobCompletedNative(2L)).thenAnswer(inv -> {
            if (interruptedJob.getProcessedRecipients() >= interruptedJob.getTotalRecipients()) {
                interruptedJob.setStatus(BroadcastJobStatus.COMPLETED);
                return 1;
            }
            return 0;
        });

        broadcastService.recordAppRecipientStatus(2L, user2.getId(), com.example.model.AppNotificationStatus.SENT, null);

        assertEquals(BroadcastJobStatus.COMPLETED, interruptedJob.getStatus());
    }

    @Test
    @DisplayName("3. Duplicate broadcast request returns existing job ID via idempotency check")
    void testIdempotencyPreventsDuplicateJobCreation() {
        BroadcastJob activeJob = BroadcastJob.builder()
                .id(99L)
                .title("Festival Special")
                .message("Enjoy 50% off")
                .status(BroadcastJobStatus.IN_PROGRESS)
                .build();

        when(broadcastJobRepository.findFirstByTitleAndMessageAndStatusIn(eq("Festival Special"), eq("Enjoy 50% off"), any()))
                .thenReturn(Optional.of(activeJob));

        Long broadcastId = broadcastService.initiateBroadcast("Festival Special", "Enjoy 50% off", NotificationType.ANNOUNCEMENT, 1L);

        assertEquals(99L, broadcastId);
        verify(notificationProducer, never()).enqueueJob(any());
    }

    @Test
    @DisplayName("4. Broadcast finishes with COMPLETED_WITH_FAILURES when recipient failures occur")
    void testBroadcastCompletesWithFailuresWhenFailuresRecorded() {
        when(userRepository.countByIsActiveTrueAndIsDeletedFalse()).thenReturn(1L);
        when(broadcastJobRepository.save(any(BroadcastJob.class))).thenAnswer(i -> {
            BroadcastJob j = i.getArgument(0);
            if (j.getId() == null) j.setId(10L);
            return j;
        });

        BroadcastJob job = BroadcastJob.builder()
                .id(10L)
                .title("Alert")
                .message("Test message")
                .status(BroadcastJobStatus.IN_PROGRESS)
                .lastProcessedUserId(0L)
                .totalRecipients(1L)
                .processedRecipients(0L)
                .successfulRecipients(0L)
                .failedRecipients(0L)
                .build();

        when(broadcastJobRepository.findById(10L)).thenReturn(Optional.of(job));
        when(userRepository.findActiveUsersProjectionChunkAfterId(eq(0L), any(Pageable.class))).thenReturn(List.of(user1));
        when(userRepository.findActiveUsersProjectionChunkAfterId(eq(10L), any(Pageable.class))).thenReturn(List.of());

        // Simulate enqueue error
        doThrow(new RuntimeException("RabbitMQ Connection Failed")).when(notificationProducer).enqueueJob(any());

        when(recipientStatusRepository.tryMarkAggregateProcessedNative(eq(10L), eq(10L))).thenReturn(1);
        when(recipientStatusRepository.isRecipientSuccessNative(eq(10L), eq(10L))).thenReturn(false);
        when(broadcastJobRepository.incrementJobCountersNative(eq(10L), eq(0), eq(1))).thenAnswer(inv -> {
            job.setProcessedRecipients(job.getProcessedRecipients() + 1);
            job.setFailedRecipients(job.getFailedRecipients() + 1);
            job.setLastError("RabbitMQ Connection Failed");
            return 1;
        });
        when(broadcastJobRepository.tryMarkJobCompletedNative(10L)).thenAnswer(inv -> {
            if (job.getProcessedRecipients() >= job.getTotalRecipients()) {
                job.setStatus(job.getFailedRecipients() > 0 ? BroadcastJobStatus.COMPLETED_WITH_FAILURES : BroadcastJobStatus.COMPLETED);
                return 1;
            }
            return 0;
        });

        broadcastService.initiateBroadcast("Alert", "Test message", NotificationType.ANNOUNCEMENT, 1L);

        assertEquals(BroadcastJobStatus.COMPLETED_WITH_FAILURES, job.getStatus());
        assertEquals(1L, job.getFailedRecipients());
        assertNotNull(job.getLastError());
    }
}
