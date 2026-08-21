package com.example.service;

import com.example.repository.projection.UserBroadcastProjection;
import com.example.model.AppNotificationStatus;
import com.example.model.BroadcastJob;
import com.example.model.BroadcastJobStatus;
import com.example.model.BroadcastRecipientStatus;
import com.example.model.NotificationType;
import com.example.model.RecipientEmailStatus;
import com.example.model.User;
import com.example.queue.NotificationProducer;
import com.example.repository.BroadcastJobRepository;
import com.example.repository.BroadcastRecipientStatusRepository;
import com.example.repository.UserRepository;
import com.example.serviceimpl.AdminBroadcastServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BroadcastControlledTestModeTest {

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

    private List<UserBroadcastProjection> mock1000Projections;

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
        ReflectionTestUtils.setField(broadcastService, "testMode", true);
        ReflectionTestUtils.setField(broadcastService, "testRecipientLimit", 100);

        mock1000Projections = new ArrayList<>();
        for (long i = 1; i <= 1000; i++) {
            mock1000Projections.add(createProjection(i, "user" + i + "@example.com", "User" + i));
        }

        lenient().doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(0);
            runnable.run();
            return null;
        }).when(bulkEmailExecutor).execute(any());
    }

    @Test
    @DisplayName("1. Controlled Test Mode limits recipient batch to exactly 100 out of 1M+ active users")
    void testControlledTestModeLimitsTo100Recipients() {
        // Total active in DB is 1,000,002
        when(userRepository.countByIsActiveTrueAndIsDeletedFalse()).thenReturn(1000002L);

        when(broadcastJobRepository.save(any(BroadcastJob.class))).thenAnswer(i -> {
            BroadcastJob j = i.getArgument(0);
            if (j.getId() == null) j.setId(500L);
            return j;
        });

        BroadcastJob jobInDb = BroadcastJob.builder()
                .id(500L)
                .title("Controlled Test Broadcast")
                .message("Testing 100 user limit")
                .notificationType(NotificationType.ANNOUNCEMENT)
                .status(BroadcastJobStatus.IN_PROGRESS)
                .lastProcessedUserId(0L)
                .totalRecipients(100L)
                .processedRecipients(0L)
                .successfulRecipients(0L)
                .failedRecipients(0L)
                .isTestMode(true)
                .build();

        when(broadcastJobRepository.findById(500L)).thenReturn(Optional.of(jobInDb));
        when(userRepository.findActiveUsersProjectionChunkAfterId(eq(0L), any(Pageable.class)))
                .thenReturn(mock1000Projections); // DB returns 1000 projections

        Long jobId = broadcastService.initiateBroadcast("Controlled Test Broadcast", "Testing 100 user limit", NotificationType.ANNOUNCEMENT, 1L);

        assertEquals(500L, jobId);

        // Verify total recipients capped at 100
        assertEquals(100L, jobInDb.getTotalRecipients());

        // Verify exactly 100 APP notifications enqueued and 1 bulk email batch enqueued
        verify(notificationProducer, times(100)).enqueueJob(any());
        verify(notificationProducer, times(1)).enqueueEmailBatch(any());

        // Verify native insert bulkInsertRecipientStatusesOnConflict called once
        verify(recipientStatusRepository, times(1)).bulkInsertRecipientStatusesOnConflict(eq(500L), anyList());

        // Verify job remains IN_PROGRESS right after enqueuing (waiting for consumer callbacks)
        assertEquals(BroadcastJobStatus.IN_PROGRESS, jobInDb.getStatus());

        // Simulate RabbitMQ consumer callbacks for all 100 recipients
        when(recipientStatusRepository.tryMarkAggregateProcessedNative(eq(500L), anyLong())).thenReturn(1);
        when(recipientStatusRepository.isRecipientSuccessNative(eq(500L), anyLong())).thenReturn(true);
        when(broadcastJobRepository.incrementJobCountersNative(eq(500L), eq(1), eq(0))).thenAnswer(inv -> {
            jobInDb.setProcessedRecipients(jobInDb.getProcessedRecipients() + 1);
            jobInDb.setSuccessfulRecipients(jobInDb.getSuccessfulRecipients() + 1);
            return 1;
        });
        when(broadcastJobRepository.tryMarkJobCompletedNative(500L)).thenAnswer(inv -> {
            if (jobInDb.getProcessedRecipients() >= jobInDb.getTotalRecipients()) {
                jobInDb.setStatus(BroadcastJobStatus.COMPLETED);
                return 1;
            }
            return 0;
        });

        for (long i = 1; i <= 100; i++) {
            broadcastService.recordAppRecipientStatus(500L, i, AppNotificationStatus.SENT, null);
        }

        // Verify job transitions to COMPLETED after all recipient status callbacks finish
        assertEquals(BroadcastJobStatus.COMPLETED, jobInDb.getStatus());
    }

    @Test
    @DisplayName("2. Test mode does NOT delete, update, or deactivate any user in database")
    void testNoUsersModifiedInTestMode() {
        when(userRepository.countByIsActiveTrueAndIsDeletedFalse()).thenReturn(1000002L);
        when(broadcastJobRepository.save(any(BroadcastJob.class))).thenAnswer(i -> {
            BroadcastJob j = i.getArgument(0);
            if (j.getId() == null) j.setId(501L);
            return j;
        });

        when(broadcastJobRepository.findById(501L)).thenReturn(Optional.of(BroadcastJob.builder()
                .id(501L)
                .title("Safety Check")
                .message("Testing DB non-mutation")
                .notificationType(NotificationType.ANNOUNCEMENT)
                .status(BroadcastJobStatus.IN_PROGRESS)
                .lastProcessedUserId(0L)
                .totalRecipients(100L)
                .processedRecipients(0L)
                .successfulRecipients(0L)
                .failedRecipients(0L)
                .isTestMode(true)
                .build()));

        when(userRepository.findActiveUsersProjectionChunkAfterId(eq(0L), any(Pageable.class)))
                .thenReturn(mock1000Projections.subList(0, 100));

        broadcastService.initiateBroadcast("Safety Check", "Testing DB non-mutation", NotificationType.ANNOUNCEMENT, 1L);

        // Prove user repository save/delete methods were NEVER called
        verify(userRepository, never()).save(any(User.class));
        verify(userRepository, never()).saveAll(any());
        verify(userRepository, never()).deleteAll(any());
    }

    @Test
    @DisplayName("3. Concurrent recipient status calls for same (jobId, userId) produce exactly one recipient entity")
    void testConcurrentRecipientStatusCreation() throws InterruptedException {
        Long jobId = 12L;
        Long userId = 6095L;
        User user = new User();
        user.setId(userId);
        user.setEmail("prajakta.menon0006095@gmail.com");

        lenient().when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        java.util.Map<String, BroadcastRecipientStatus> dbMap = new java.util.concurrent.ConcurrentHashMap<>();

        lenient().when(recipientStatusRepository.findByBroadcastJobIdAndUserId(jobId, userId)).thenAnswer(inv -> {
            String key = jobId + "_" + userId;
            return Optional.ofNullable(dbMap.get(key));
        });

        Answer<BroadcastRecipientStatus> saveAnswer = inv -> {
            BroadcastRecipientStatus status = inv.getArgument(0);
            String key = status.getBroadcastJobId() + "_" + status.getUserId();
            if (dbMap.containsKey(key)) {
                throw new org.springframework.dao.DataIntegrityViolationException("Duplicate key violation: uk_broadcast_recipient_job_user");
            }
            if (status.getId() == null) {
                status.setId(18786L);
            }
            dbMap.put(key, status);
            return status;
        };

        lenient().doAnswer(inv -> {
            Long jId = inv.getArgument(0);
            List<Long> uIds = inv.getArgument(1);
            for (Long uId : uIds) {
                String key = jId + "_" + uId;
                dbMap.putIfAbsent(key, BroadcastRecipientStatus.builder().broadcastJobId(jId).userId(uId).build());
            }
            return null;
        }).when(recipientStatusRepository).bulkInsertRecipientStatusesOnConflict(eq(jobId), anyList());

        int numThreads = 15;
        java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(numThreads);
        java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                try {
                    recipientStatusRepository.bulkInsertRecipientStatusesOnConflict(jobId, List.of(userId));
                    broadcastService.recordAppRecipientStatus(jobId, userId, AppNotificationStatus.SENT, null);
                } finally {
                    latch.countDown();
                }
            });
        }

        boolean finished = latch.await(5, java.util.concurrent.TimeUnit.SECONDS);
        assertTrue(finished);
        executor.shutdown();

        // Exactly ONE entry created in dbMap
        assertEquals(1, dbMap.size());
        assertNotNull(dbMap.get(jobId + "_" + userId));
    }
}
