package com.example.scheduler;

import com.example.model.*;
import com.example.queue.SubscriptionExpiryConsumer;
import com.example.queue.SubscriptionExpiryJobPayload;
import com.example.queue.SubscriptionExpiryProducer;
import com.example.repository.SubscriptionExpiryJobRepository;
import com.example.repository.UserSubscriptionRepository;
import com.example.service.EmailService;
import com.example.service.NotificationService;
import com.example.service.ProfilePremiumSyncService;
import com.example.serviceimpl.SubscriptionExpiryWorkflowServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionExpiryWorkflowTest {

    @Mock
    private UserSubscriptionRepository userSubscriptionRepository;

    @Mock
    private SubscriptionExpiryJobRepository jobRepository;

    @Mock
    private ProfilePremiumSyncService profilePremiumSyncService;

    @Mock
    private SubscriptionExpiryProducer producer;

    @Mock
    private EmailService emailService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private SubscriptionExpiryWorkflowServiceImpl workflowService;

    @InjectMocks
    private SubscriptionExpiryConsumer consumer;

    private User user;
    private UserSubscription subscription;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(101L);
        user.setEmail("user@example.com");
        user.setFirstName("Rahul");

        subscription = new UserSubscription();
        subscription.setId(501L);
        subscription.setUser(user);
        subscription.setIsActive(true);
        subscription.setStatus("ACTIVE");
        subscription.setEndDate(LocalDateTime.now().minusDays(1));
    }

    @Test
    @DisplayName("1. Active subscription expires correctly")
    void testActiveSubscriptionExpiresCorrectly() {
        when(userSubscriptionRepository.findActiveSubscriptionsDueForExpiry(any(LocalDateTime.class)))
                .thenReturn(List.of(subscription));
        when(jobRepository.existsByIdempotencyKey(anyString())).thenReturn(false);
        when(jobRepository.save(any(SubscriptionExpiryJob.class)))
                .thenAnswer(invocation -> {
                    SubscriptionExpiryJob job = invocation.getArgument(0);
                    job.setId(1L);
                    return job;
                });

        List<SubscriptionExpiryJob> jobs = workflowService.processExpiringSubscriptions();

        assertFalse(subscription.getIsActive());
        assertEquals("EXPIRED", subscription.getStatus());
        assertEquals(1, jobs.size());
        verify(profilePremiumSyncService, times(1)).sync(eq(user), eq(subscription));
        verify(jobRepository, times(1)).save(any(SubscriptionExpiryJob.class));
    }

    @Test
    @DisplayName("2. Already expired subscription is ignored")
    void testAlreadyExpiredSubscriptionIsIgnored() {
        subscription.setIsActive(false);
        subscription.setStatus("EXPIRED");

        when(userSubscriptionRepository.findActiveSubscriptionsDueForExpiry(any(LocalDateTime.class)))
                .thenReturn(List.of());

        List<SubscriptionExpiryJob> jobs = workflowService.processExpiringSubscriptions();

        assertTrue(jobs.isEmpty());
        verify(jobRepository, never()).save(any());
    }

    @Test
    @DisplayName("3 & 4. Scheduler creates only one job and duplicate execution does not duplicate job")
    void testDuplicateSchedulerExecutionDoesNotDuplicateJob() {
        when(userSubscriptionRepository.findActiveSubscriptionsDueForExpiry(any(LocalDateTime.class)))
                .thenReturn(List.of(subscription));
        when(jobRepository.existsByIdempotencyKey("EXPIRY_EMAIL_SUB_501")).thenReturn(true);

        List<SubscriptionExpiryJob> jobs = workflowService.processExpiringSubscriptions();

        assertTrue(jobs.isEmpty());
        verify(jobRepository, never()).save(any(SubscriptionExpiryJob.class));
    }

    @Test
    @DisplayName("5. Duplicate queue message does not duplicate email")
    void testDuplicateQueueMessageDoesNotDuplicateEmail() {
        SubscriptionExpiryJobPayload payload = SubscriptionExpiryJobPayload.builder()
                .jobId(1L)
                .subscriptionId(501L)
                .userId(101L)
                .userEmail("user@example.com")
                .userFirstName("Rahul")
                .attemptCount(0)
                .build();

        when(jobRepository.claimJobAtomically(eq(1L), any())).thenReturn(0);

        consumer.processSubscriptionExpiryJob(payload);

        verify(emailService, never()).sendPremiumExpiredEmail(anyString(), anyString());
    }

    @Test
    @DisplayName("6. Email failure retries")
    void testEmailFailureRetries() {
        SubscriptionExpiryJob pendingJob = SubscriptionExpiryJob.builder()
                .id(1L)
                .subscription(subscription)
                .user(user)
                .status(SubscriptionExpiryJobStatus.PENDING)
                .attemptCount(0)
                .idempotencyKey("EXPIRY_EMAIL_SUB_501")
                .build();

        SubscriptionExpiryJobPayload payload = SubscriptionExpiryJobPayload.builder()
                .jobId(1L)
                .subscriptionId(501L)
                .userId(101L)
                .userEmail("user@example.com")
                .userFirstName("Rahul")
                .attemptCount(0)
                .build();

        when(jobRepository.claimJobAtomically(eq(1L), any())).thenReturn(1);
        when(jobRepository.findById(1L)).thenReturn(Optional.of(pendingJob));
        doThrow(new RuntimeException("SMTP connection timeout"))
                .when(emailService).sendPremiumExpiredEmail(anyString(), anyString());

        consumer.processSubscriptionExpiryJob(payload);

        assertEquals(SubscriptionExpiryJobStatus.FAILED, pendingJob.getStatus());
        assertEquals(1, pendingJob.getAttemptCount());
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), any(SubscriptionExpiryJobPayload.class));
    }

    @Test
    @DisplayName("7. Maximum retries move job to failed/DLQ")
    void testMaxRetriesMovesJobToDLQ() {
        SubscriptionExpiryJob pendingJob = SubscriptionExpiryJob.builder()
                .id(1L)
                .subscription(subscription)
                .user(user)
                .status(SubscriptionExpiryJobStatus.PENDING)
                .attemptCount(2)
                .idempotencyKey("EXPIRY_EMAIL_SUB_501")
                .build();

        SubscriptionExpiryJobPayload payload = SubscriptionExpiryJobPayload.builder()
                .jobId(1L)
                .subscriptionId(501L)
                .userId(101L)
                .userEmail("user@example.com")
                .userFirstName("Rahul")
                .attemptCount(2)
                .build();

        when(jobRepository.claimJobAtomically(eq(1L), any())).thenReturn(1);
        when(jobRepository.findById(1L)).thenReturn(Optional.of(pendingJob));
        doThrow(new RuntimeException("SMTP connection timeout"))
                .when(emailService).sendPremiumExpiredEmail(anyString(), anyString());

        consumer.processSubscriptionExpiryJob(payload);

        assertEquals(SubscriptionExpiryJobStatus.DLQ, pendingJob.getStatus());
        assertEquals(3, pendingJob.getAttemptCount());
        verify(rabbitTemplate, times(1)).convertAndSend(eq("subscription.expiry.email.dlx"), eq("subscription.expiry.email.dlq.routingKey"), any(SubscriptionExpiryJobPayload.class));
    }

    @Test
    @DisplayName("8. Invalid email is handled correctly")
    void testInvalidEmailHandledCorrectly() {
        SubscriptionExpiryJob pendingJob = SubscriptionExpiryJob.builder()
                .id(1L)
                .subscription(subscription)
                .user(user)
                .status(SubscriptionExpiryJobStatus.PENDING)
                .attemptCount(0)
                .idempotencyKey("EXPIRY_EMAIL_SUB_501")
                .build();

        SubscriptionExpiryJobPayload payload = SubscriptionExpiryJobPayload.builder()
                .jobId(1L)
                .subscriptionId(501L)
                .userId(101L)
                .userEmail("invalid-email")
                .userFirstName("Rahul")
                .attemptCount(0)
                .build();

        when(jobRepository.claimJobAtomically(eq(1L), any())).thenReturn(1);
        when(jobRepository.findById(1L)).thenReturn(Optional.of(pendingJob));
        doThrow(new RuntimeException("550 5.1.1 Invalid address"))
                .when(emailService).sendPremiumExpiredEmail(anyString(), anyString());

        consumer.processSubscriptionExpiryJob(payload);

        assertEquals(SubscriptionExpiryJobStatus.DLQ, pendingJob.getStatus());
        verify(rabbitTemplate, times(1)).convertAndSend(eq("subscription.expiry.email.dlx"), eq("subscription.expiry.email.dlq.routingKey"), any(SubscriptionExpiryJobPayload.class));
    }

    @Test
    @DisplayName("9. Outbox event is eventually published")
    void testOutboxEventPublished() {
        SubscriptionExpiryJob pendingJob = SubscriptionExpiryJob.builder()
                .id(1L)
                .subscription(subscription)
                .user(user)
                .status(SubscriptionExpiryJobStatus.PENDING)
                .idempotencyKey("EXPIRY_EMAIL_SUB_501")
                .build();

        when(jobRepository.findByStatus(SubscriptionExpiryJobStatus.PENDING))
                .thenReturn(List.of(pendingJob));

        workflowService.publishPendingOutboxJobs();

        verify(producer, times(1)).enqueueJob(any(SubscriptionExpiryJobPayload.class));
    }

    @Test
    @DisplayName("10. Multiple scheduler executions are safe")
    void testMultipleSchedulerExecutionsAreSafe() {
        when(userSubscriptionRepository.findActiveSubscriptionsDueForExpiry(any(LocalDateTime.class)))
                .thenReturn(List.of(subscription));
        when(jobRepository.existsByIdempotencyKey("EXPIRY_EMAIL_SUB_501")).thenReturn(false);
        when(jobRepository.save(any(SubscriptionExpiryJob.class)))
                .thenAnswer(i -> {
                    SubscriptionExpiryJob j = i.getArgument(0);
                    j.setId(1L);
                    return j;
                });

        // Run 1
        workflowService.processExpiringSubscriptions();

        // Run 2 (Simulated sub status updated)
        subscription.setIsActive(false);
        subscription.setStatus("EXPIRED");
        when(userSubscriptionRepository.findActiveSubscriptionsDueForExpiry(any(LocalDateTime.class)))
                .thenReturn(List.of());

        List<SubscriptionExpiryJob> secondRunJobs = workflowService.processExpiringSubscriptions();

        assertTrue(secondRunJobs.isEmpty());
        verify(jobRepository, times(1)).save(any(SubscriptionExpiryJob.class));
    }
}
