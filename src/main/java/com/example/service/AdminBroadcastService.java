package com.example.service;

import com.example.dto.response.BroadcastJobResponseDTO;
import com.example.dto.response.BroadcastRecipientStatusResponseDTO;
import com.example.model.AppNotificationStatus;
import com.example.model.NotificationType;
import com.example.model.RecipientEmailStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminBroadcastService {
    Long initiateBroadcast(String title, String message, NotificationType type, Long adminId);
    void processPendingBroadcasts();
    BroadcastJobResponseDTO resumeBroadcastJob(Long jobId);
    BroadcastJobResponseDTO cancelBroadcastJob(Long jobId);
    void recordRecipientSuccess(Long broadcastJobId);
    void recordRecipientFailure(Long broadcastJobId, String errorMessage);
    void recordAppRecipientStatus(Long broadcastJobId, Long userId, AppNotificationStatus status, String error);
    void recordEmailRecipientStatus(Long broadcastJobId, Long userId, RecipientEmailStatus status, String error);
    void recordEmailRecipientBatchStatus(Long broadcastJobId, java.util.List<Long> userIds, RecipientEmailStatus status, String error);
    void recordAppRecipientBatchStatus(Long broadcastJobId, java.util.List<Long> userIds, AppNotificationStatus status, String error);
    BroadcastJobResponseDTO getActiveBroadcastJob();
    BroadcastJobResponseDTO getBroadcastJobById(Long jobId);
    Page<BroadcastJobResponseDTO> getBroadcastHistory(Pageable pageable);
    Page<BroadcastRecipientStatusResponseDTO> getBroadcastRecipients(Long broadcastJobId, String search, AppNotificationStatus appStatus, RecipientEmailStatus emailStatus, Pageable pageable);
}
