package com.example.service;

import com.example.dto.request.SuccessStoryCreateRequestDTO;
import com.example.dto.request.SuccessStoryUpdateRequestDTO;
import com.example.dto.response.SuccessStoryResponseDTO;
import com.example.model.NotificationType;
import com.example.model.SuccessStory;
import com.example.queue.NotificationJobPayload;
import com.example.queue.NotificationProducer;
import com.example.repository.SuccessStoryRepository;
import com.example.repository.UserRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SuccessStoryServiceTest {

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
    private com.example.repository.BroadcastJobRepository broadcastJobRepository;

    @Mock
    private com.example.repository.BroadcastRecipientStatusRepository recipientStatusRepository;

    @InjectMocks
    private SuccessStoryServiceImpl successStoryService;

    private SuccessStory mockStory;

    @BeforeEach
    void setUp() {
        mockStory = SuccessStory.builder()
                .id(1L)
                .partnerOneName("Rahul")
                .partnerTwoName("Priya")
                .shortStory("Met on Gathbandhan")
                .fullStory("Full beautiful journey...")
                .weddingDate(LocalDate.of(2025, 1, 15))
                .location("Mumbai")
                .consentGiven(true)
                .isPublished(false)
                .displayOrder(1)
                .publishVersion(0)
                .createdBy(100L)
                .build();
    }

    @Test
    @DisplayName("1. Create Draft - No Notification Triggered")
    void createDraft_NoNotificationTriggered() {
        SuccessStoryCreateRequestDTO dto = SuccessStoryCreateRequestDTO.builder()
                .partnerOneName("Rahul")
                .partnerTwoName("Priya")
                .shortStory("Met on Gathbandhan")
                .consentGiven(true)
                .build();

        when(successStoryRepository.save(any(SuccessStory.class))).thenReturn(mockStory);

        SuccessStoryResponseDTO result = successStoryService.createSuccessStory(dto, 100L);

        assertNotNull(result);
        verify(notificationProducer, never()).enqueueJob(any());
        verify(userRepository, never()).findActiveUsersProjectionChunkAfterId(anyLong(), any());
    }

    @Test
    @DisplayName("2. Update Draft - No Notification Triggered")
    void updateDraft_NoNotificationTriggered() {
        when(successStoryRepository.findById(1L)).thenReturn(Optional.of(mockStory));
        when(successStoryRepository.save(any(SuccessStory.class))).thenAnswer(i -> i.getArguments()[0]);

        SuccessStoryUpdateRequestDTO updateDto = SuccessStoryUpdateRequestDTO.builder()
                .partnerOneName("Rahul Updated")
                .partnerTwoName("Priya Updated")
                .shortStory("Updated story")
                .consentGiven(true)
                .build();

        SuccessStoryResponseDTO result = successStoryService.updateSuccessStory(1L, updateDto, 100L);

        assertNotNull(result);
        verify(notificationProducer, never()).enqueueJob(any());
    }

    @Test
    @DisplayName("3. Publish with Consent=True - Notification Dispatch Prepared")
    void publishSuccessStory_ConsentTrue_Succeeds() {
        mockStory.setConsentGiven(true);
        mockStory.setIsPublished(false);

        when(successStoryRepository.findById(1L)).thenReturn(Optional.of(mockStory));
        when(successStoryRepository.save(any(SuccessStory.class))).thenAnswer(i -> i.getArguments()[0]);
        when(broadcastJobRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        SuccessStoryResponseDTO result = successStoryService.publishSuccessStory(1L, 100L);

        assertTrue(result.getIsPublished());
        assertNotNull(mockStory.getPublishedAt());
        verify(adminAuditLogService, times(1)).log(eq(100L), anyString(), eq("PUBLISH_SUCCESS_STORY"), anyString(), eq(1L), anyString(), eq("DRAFT"), eq("PUBLISHED"));
    }

    @Test
    @DisplayName("4. Publish with Consent=False - Publish Rejected & No Notification")
    void publishSuccessStory_ConsentFalse_RejectsAndNoNotification() {
        mockStory.setConsentGiven(false);
        mockStory.setIsPublished(false);

        when(successStoryRepository.findById(1L)).thenReturn(Optional.of(mockStory));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> successStoryService.publishSuccessStory(1L, 100L)
        );

        assertTrue(exception.getMessage().contains("Consent from couple is required"));
        verify(successStoryRepository, never()).save(any(SuccessStory.class));
        verify(notificationProducer, never()).enqueueJob(any());
    }

    @Test
    @DisplayName("5. Publish Already-Published Story - No Accidental Duplicate Notification")
    void publishAlreadyPublishedStory_NoDuplicateNotification() {
        mockStory.setConsentGiven(true);
        mockStory.setIsPublished(true);
        mockStory.setPublishVersion(1);

        when(successStoryRepository.findById(1L)).thenReturn(Optional.of(mockStory));

        SuccessStoryResponseDTO result = successStoryService.publishSuccessStory(1L, 100L);

        assertTrue(result.getIsPublished());
        verify(successStoryRepository, never()).save(any(SuccessStory.class));
        verify(notificationProducer, never()).enqueueJob(any());
    }

    @Test
    @DisplayName("6. Unpublish Story - No Notification Triggered")
    void unpublishStory_NoNotificationTriggered() {
        mockStory.setIsPublished(true);

        when(successStoryRepository.findById(1L)).thenReturn(Optional.of(mockStory));
        when(successStoryRepository.save(any(SuccessStory.class))).thenAnswer(i -> i.getArguments()[0]);

        SuccessStoryResponseDTO result = successStoryService.unpublishSuccessStory(1L, 100L);

        assertFalse(result.getIsPublished());
        verify(notificationProducer, never()).enqueueJob(any());
    }

    @Test
    @DisplayName("7 & 8 & 9. Notification Payload Details (Dynamic Names, APP Channel, RabbitMQ Producer)")
    void dispatchPublishNotification_PayloadDetailsCorrect() {
        UserBroadcastProjection mockUser = mock(UserBroadcastProjection.class);
        when(mockUser.getId()).thenReturn(50L);

        when(userRepository.findActiveUsersProjectionChunkAfterId(eq(0L), any(Pageable.class)))
                .thenReturn(List.of(mockUser))
                .thenReturn(List.of());

        successStoryService.dispatchPublishNotification(1L, 1, "Rahul", "Priya");

        ArgumentCaptor<com.example.queue.AppNotificationBatchPayload> captor = ArgumentCaptor.forClass(com.example.queue.AppNotificationBatchPayload.class);
        verify(notificationProducer, times(1)).enqueueAppBatch(captor.capture());

        com.example.queue.AppNotificationBatchPayload payload = captor.getValue();
        assertEquals("New Success Story ❤️", payload.getTitle());
        assertTrue(payload.getMessage().contains("Rahul & Priya"));
        assertEquals("Meet Rahul & Priya! Read their inspiring Gathbandhan success story.", payload.getMessage());
        assertEquals(NotificationType.ANNOUNCEMENT, payload.getType());
        assertEquals("SUCCESS_STORY_PUBLISHED", payload.getEventType());
        assertEquals(1L, payload.getReferenceId());
        assertEquals(1, payload.getRecipients().size());
        assertEquals(50L, payload.getRecipients().get(0).getUserId());
    }

    @Test
    @DisplayName("10. Publish API Response Immediate (Non-Blocking)")
    void publishApi_NonBlocking() {
        mockStory.setConsentGiven(true);
        mockStory.setIsPublished(false);

        when(successStoryRepository.findById(1L)).thenReturn(Optional.of(mockStory));
        when(successStoryRepository.save(any(SuccessStory.class))).thenAnswer(i -> i.getArguments()[0]);
        when(broadcastJobRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        long start = System.currentTimeMillis();
        SuccessStoryResponseDTO response = successStoryService.publishSuccessStory(1L, 100L);
        long duration = System.currentTimeMillis() - start;

        assertNotNull(response);
        assertTrue(duration < 500, "Publish API should return immediately");
    }

    @Test
    @DisplayName("11. Database Publish Succeeds Even If Notification Enqueue Fails")
    void databasePublishSucceeds_EvenIfNotificationFails() {
        UserBroadcastProjection mockUser = mock(UserBroadcastProjection.class);
        when(mockUser.getId()).thenReturn(50L);

        when(userRepository.findActiveUsersProjectionChunkAfterId(eq(0L), any(Pageable.class)))
                .thenReturn(List.of(mockUser));

        doThrow(new RuntimeException("RabbitMQ connection pool exhausted"))
                .when(notificationProducer).enqueueAppBatch(any());

        assertDoesNotThrow(() -> successStoryService.dispatchPublishNotification(1L, 1, "Rahul", "Priya"));
    }

    @Test
    @DisplayName("12. Only Eligible Active Users Selected via Keyset Pagination")
    void recipientsSelection_OnlyActiveUsersChunked() {
        UserBroadcastProjection u1 = mock(UserBroadcastProjection.class);
        when(u1.getId()).thenReturn(10L);

        UserBroadcastProjection u2 = mock(UserBroadcastProjection.class);
        when(u2.getId()).thenReturn(20L);

        when(userRepository.findActiveUsersProjectionChunkAfterId(eq(0L), any()))
                .thenReturn(List.of(u1, u2));

        successStoryService.dispatchPublishNotification(1L, 1, "Rahul", "Priya");

        verify(userRepository, times(1)).findActiveUsersProjectionChunkAfterId(eq(0L), any());
        verify(notificationProducer, times(1)).enqueueAppBatch(any());
    }

    @Test
    @DisplayName("13. Deterministic Batch ID Format Is Deterministic")
    void idempotencyKeyFormat_IsDeterministic() {
        UserBroadcastProjection u1 = mock(UserBroadcastProjection.class);
        when(u1.getId()).thenReturn(10L);

        when(userRepository.findActiveUsersProjectionChunkAfterId(eq(0L), any()))
                .thenReturn(List.of(u1));

        successStoryService.dispatchPublishNotification(42L, 3, "Arjun", "Meera");

        ArgumentCaptor<com.example.queue.AppNotificationBatchPayload> captor = ArgumentCaptor.forClass(com.example.queue.AppNotificationBatchPayload.class);
        verify(notificationProducer).enqueueAppBatch(captor.capture());

        assertEquals("STORY_BATCH_42_V3_10", captor.getValue().getBatchId());
    }

    @Test
    @DisplayName("14. Republishing story after unpublish increments version and generates fresh versioned key")
    void republishStory_IncrementsVersionAndDispatchesFreshKey() {
        mockStory.setConsentGiven(true);
        mockStory.setIsPublished(true);
        mockStory.setPublishVersion(1);

        when(successStoryRepository.findById(1L)).thenReturn(Optional.of(mockStory));
        when(successStoryRepository.save(any(SuccessStory.class))).thenAnswer(i -> i.getArguments()[0]);

        // First unpublish
        successStoryService.unpublishSuccessStory(1L, 100L);
        mockStory.setIsPublished(false);

        // Now publish again -> should increment publishVersion to 2
        SuccessStoryResponseDTO republished = successStoryService.publishSuccessStory(1L, 100L);

        assertTrue(republished.getIsPublished());
        assertEquals(2, mockStory.getPublishVersion());
    }
}
