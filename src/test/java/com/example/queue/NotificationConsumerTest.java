package com.example.queue;

import com.example.config.RabbitMQConfig;
import com.example.model.Notification;
import com.example.model.NotificationJobOutbox;
import com.example.model.NotificationJobStatus;
import com.example.model.NotificationPriority;
import com.example.model.NotificationType;
import com.example.repository.NotificationJobOutboxRepository;
import com.example.repository.NotificationRepository;
import com.example.service.AdminBroadcastService;
import com.example.service.EmailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class NotificationConsumerTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationJobOutboxRepository outboxRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private EmailService emailService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private AdminBroadcastService adminBroadcastService;

    @InjectMocks
    private NotificationConsumer consumer;

    private NotificationJobPayload createTestPayload(String key, NotificationPriority priority) {
        return NotificationJobPayload.builder()
                .jobId(UUID.randomUUID().toString())
                .idempotencyKey(key)
                .userId(101L)
                .userEmail("test@example.com")
                .userFirstName("Vikram")
                .title("Welcome")
                .message("Welcome to Gathbandhan")
                .type(NotificationType.ANNOUNCEMENT)
                .channelType(NotificationJobPayload.ChannelType.BOTH)
                .priority(priority)
                .retryCount(0)
                .build();
    }

    @Test
    @DisplayName("1. Successful notification delivery updates STOMP, Email, Outbox, and Broadcast tracker")
    void testSuccessfulNotificationDelivery() {
        NotificationJobPayload payload = createTestPayload("TEST_KEY_1", NotificationPriority.CRITICAL);
        payload.setBroadcastJobId(123L);

        when(outboxRepository.existsByIdempotencyKey("TEST_KEY_1")).thenReturn(true);
        when(outboxRepository.claimJobAtomically(eq("TEST_KEY_1"), any())).thenReturn(1);
        when(notificationRepository.save(any(Notification.class))).thenAnswer(i -> i.getArgument(0));

        NotificationJobOutbox outbox = NotificationJobOutbox.builder()
                .idempotencyKey("TEST_KEY_1")
                .status(NotificationJobStatus.PROCESSING)
                .build();
        when(outboxRepository.findByIdempotencyKey("TEST_KEY_1")).thenReturn(Optional.of(outbox));

        consumer.processCriticalNotification(payload);

        verify(emailService, times(1)).sendAnnouncementEmail(eq("test@example.com"), eq("Vikram"), eq("Welcome"), eq("Welcome to Gathbandhan"));
        verify(messagingTemplate, times(1)).convertAndSend(eq("/topic/notifications/101"), any(Object.class));
        verify(adminBroadcastService, times(1)).recordAppRecipientStatus(123L, 101L, com.example.model.AppNotificationStatus.SENT, null);
        verify(adminBroadcastService, times(1)).recordEmailRecipientStatus(123L, 101L, com.example.model.RecipientEmailStatus.PROVIDER_ACCEPTED, null);
        assertEquals(NotificationJobStatus.COMPLETED, outbox.getStatus());
    }

    @Test
    @DisplayName("2. Duplicate message claiming skips execution")
    void testDuplicateMessageClaimingSkipsExecution() {
        NotificationJobPayload payload = createTestPayload("TEST_KEY_2", NotificationPriority.HIGH);
        when(outboxRepository.existsByIdempotencyKey("TEST_KEY_2")).thenReturn(true);
        when(outboxRepository.claimJobAtomically(eq("TEST_KEY_2"), any())).thenReturn(0);

        consumer.processHighNotification(payload);

        verify(emailService, never()).sendAnnouncementEmail(anyString(), anyString(), anyString(), anyString());
        verify(messagingTemplate, never()).convertAndSend(anyString(), any(Object.class));
    }

    @Test
    @DisplayName("3. Transient SMTP error increments retry count and re-enqueues without blocking thread")
    void testTransientErrorTriggersRetry() {
        NotificationJobPayload payload = createTestPayload("TEST_KEY_3", NotificationPriority.MEDIUM);
        doThrow(new RuntimeException("Connection timeout"))
                .when(emailService).sendAnnouncementEmail(anyString(), anyString(), anyString(), anyString());

        consumer.processMediumNotification(payload);

        verify(rabbitTemplate, times(1)).convertAndSend(eq(RabbitMQConfig.NOTIFICATION_EXCHANGE), eq(RabbitMQConfig.ROUTING_MEDIUM), eq(payload));
        assertEquals(1, payload.getRetryCount());
    }

    @Test
    @DisplayName("4. Permanent SMTP 550 error routes immediately to DLQ and records broadcast failure")
    void testPermanentErrorRoutesToDLQ() {
        NotificationJobPayload payload = createTestPayload("TEST_KEY_4", NotificationPriority.CRITICAL);
        payload.setBroadcastJobId(123L);
        payload.setChannelType(NotificationJobPayload.ChannelType.EMAIL);
        when(notificationRepository.save(any(Notification.class))).thenAnswer(i -> i.getArgument(0));
        doThrow(new RuntimeException("550 User unknown"))
                .when(emailService).sendAnnouncementEmail(anyString(), anyString(), anyString(), anyString());

        consumer.processCriticalNotification(payload);

        verify(rabbitTemplate, times(1)).convertAndSend(eq(RabbitMQConfig.NOTIFICATION_DLX), eq(RabbitMQConfig.ROUTING_CRITICAL + ".dlq"), eq(payload));
        verify(adminBroadcastService, times(1)).recordEmailRecipientStatus(eq(123L), eq(101L), eq(com.example.model.RecipientEmailStatus.FAILED), anyString());
    }

    @Test
    @DisplayName("5. Strongly typed BulkEmailBatchPayload listener processes email batch and records status")
    void testBulkEmailBatchProcessing() {
        com.example.provider.EmailProvider emailProvider = mock(com.example.provider.EmailProvider.class);
        org.springframework.test.util.ReflectionTestUtils.setField(consumer, "emailProvider", emailProvider);

        BulkEmailBatchPayload batch = BulkEmailBatchPayload.builder()
                .batchId("BATCH_100")
                .broadcastJobId(456L)
                .title("Bulk Title")
                .message("Bulk Message")
                .recipients(java.util.List.of(
                        new BulkEmailBatchPayload.RecipientItem(201L, "user201@example.com", "User201"),
                        new BulkEmailBatchPayload.RecipientItem(202L, "user202@example.com", "User202")
                ))
                .build();

        com.example.provider.BulkEmailBatchResponse response = com.example.provider.BulkEmailBatchResponse.builder()
                .success(true)
                .acceptedCount(2)
                .rejectedCount(0)
                .failedUserIds(java.util.Collections.emptyList())
                .providerMessage("Accepted")
                .build();

        when(emailProvider.sendBatch(anyList(), eq("Bulk Title"), eq("Bulk Message"))).thenReturn(response);

        consumer.processBulkEmailNotification(batch);

        verify(emailProvider, times(1)).sendBatch(anyList(), eq("Bulk Title"), eq("Bulk Message"));
        verify(adminBroadcastService, times(1)).recordEmailRecipientBatchStatus(eq(456L), eq(java.util.List.of(201L, 202L)), eq(com.example.model.RecipientEmailStatus.PROVIDER_ACCEPTED), isNull());
    }

    @Test
    @DisplayName("6. Success Story Publish APP Notification is persisted and pushed via WebSocket without Email")
    void testSuccessStoryPublishNotificationProcessing() {
        NotificationJobPayload payload = NotificationJobPayload.builder()
                .jobId(UUID.randomUUID().toString())
                .idempotencyKey("SUCCESS_STORY_PUBLISHED_1_V1_USER_101")
                .userId(101L)
                .title("New Success Story ❤️")
                .message("Meet Rahul & Priya! Read their inspiring Gathbandhan success story.")
                .type(NotificationType.ANNOUNCEMENT)
                .channelType(NotificationJobPayload.ChannelType.APP)
                .priority(NotificationPriority.MEDIUM)
                .retryCount(0)
                .build();

        when(notificationRepository.save(any(Notification.class))).thenAnswer(i -> {
            Notification n = i.getArgument(0);
            n.setId(999L);
            return n;
        });

        consumer.processMediumNotification(payload);

        verify(notificationRepository, times(1)).save(any(Notification.class));
        verify(messagingTemplate, times(1)).convertAndSend(eq("/topic/notifications/101"), any(Object.class));
        verify(emailService, never()).sendAnnouncementEmail(anyString(), anyString(), anyString(), anyString());
    }
}
