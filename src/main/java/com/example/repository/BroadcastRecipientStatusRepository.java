package com.example.repository;

import com.example.model.AppNotificationStatus;
import com.example.model.BroadcastRecipientStatus;
import com.example.model.RecipientEmailStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface BroadcastRecipientStatusRepository extends JpaRepository<BroadcastRecipientStatus, Long>, JpaSpecificationExecutor<BroadcastRecipientStatus> {

    Optional<BroadcastRecipientStatus> findByBroadcastJobIdAndUserId(Long broadcastJobId, Long userId);

    long countByBroadcastJobIdAndUserId(Long broadcastJobId, Long userId);

    Page<BroadcastRecipientStatus> findByBroadcastJobId(Long broadcastJobId, Pageable pageable);

    @Query("SELECT b FROM BroadcastRecipientStatus b WHERE b.broadcastJobId = :broadcastJobId " +
           "AND (:search IS NULL OR :search = '' OR CAST(b.userId AS string) LIKE %:search% OR LOWER(b.userEmail) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "AND (:appStatus IS NULL OR b.appNotificationStatus = :appStatus) " +
           "AND (:emailStatus IS NULL OR b.emailStatus = :emailStatus)")
    Page<BroadcastRecipientStatus> searchRecipients(
            @Param("broadcastJobId") Long broadcastJobId,
            @Param("search") String search,
            @Param("appStatus") AppNotificationStatus appStatus,
            @Param("emailStatus") RecipientEmailStatus emailStatus,
            Pageable pageable
    );

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO broadcast_recipient_status 
        (broadcast_job_id, user_id, user_email, app_notification_status, email_status, email_queued_at, created_at, updated_at, email_attempt_count)
        SELECT :jobId, u.id, u.email, 'QUEUED', 
               CASE WHEN u.email IS NOT NULL AND u.email <> '' THEN 'QUEUED' ELSE 'FAILED' END, 
               CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0
        FROM users u
        WHERE u.id IN (:userIds)
        ON CONFLICT (broadcast_job_id, user_id) DO NOTHING
    """, nativeQuery = true)
    int bulkInsertRecipientStatusesOnConflict(@Param("jobId") Long jobId, @Param("userIds") List<Long> userIds);

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE broadcast_recipient_status
        SET app_notification_status = :status,
            notification_processed_at = CURRENT_TIMESTAMP,
            last_error = CASE WHEN :error IS NOT NULL AND :error <> '' THEN :error ELSE last_error END,
            updated_at = CURRENT_TIMESTAMP
        WHERE broadcast_job_id = :jobId AND user_id = :userId
    """, nativeQuery = true)
    int updateAppRecipientStatusNative(
            @Param("jobId") Long jobId,
            @Param("userId") Long userId,
            @Param("status") String status,
            @Param("error") String error
    );

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE broadcast_recipient_status
        SET email_status = :status,
            email_attempt_count = COALESCE(email_attempt_count, 0) + CASE WHEN :status IN ('PROVIDER_ACCEPTED', 'DELIVERED', 'FAILED') THEN 1 ELSE 0 END,
            email_accepted_at = CASE WHEN :status = 'PROVIDER_ACCEPTED' THEN CURRENT_TIMESTAMP ELSE email_accepted_at END,
            email_delivered_at = CASE WHEN :status = 'DELIVERED' THEN CURRENT_TIMESTAMP ELSE email_delivered_at END,
            email_error = CASE WHEN :status = 'FAILED' THEN :error ELSE email_error END,
            last_error = CASE WHEN :status = 'FAILED' THEN :error ELSE last_error END,
            updated_at = CURRENT_TIMESTAMP
        WHERE broadcast_job_id = :jobId AND user_id = :userId
    """, nativeQuery = true)
    int updateEmailRecipientStatusNative(
            @Param("jobId") Long jobId,
            @Param("userId") Long userId,
            @Param("status") String status,
            @Param("error") String error
    );

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE broadcast_recipient_status
        SET app_notification_status = :status,
            notification_processed_at = CURRENT_TIMESTAMP,
            last_error = CASE WHEN :error IS NOT NULL AND :error <> '' THEN :error ELSE last_error END,
            updated_at = CURRENT_TIMESTAMP
        WHERE broadcast_job_id = :jobId AND user_id IN (:userIds)
    """, nativeQuery = true)
    int updateAppRecipientStatusBatchNative(
            @Param("jobId") Long jobId,
            @Param("userIds") List<Long> userIds,
            @Param("status") String status,
            @Param("error") String error
    );

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE broadcast_recipient_status
        SET email_status = :status,
            email_attempt_count = COALESCE(email_attempt_count, 0) + CASE WHEN :status IN ('PROVIDER_ACCEPTED', 'DELIVERED', 'FAILED') THEN 1 ELSE 0 END,
            email_accepted_at = CASE WHEN :status = 'PROVIDER_ACCEPTED' THEN CURRENT_TIMESTAMP ELSE email_accepted_at END,
            email_delivered_at = CASE WHEN :status = 'DELIVERED' THEN CURRENT_TIMESTAMP ELSE email_delivered_at END,
            email_error = CASE WHEN :status = 'FAILED' THEN :error ELSE email_error END,
            last_error = CASE WHEN :status = 'FAILED' THEN :error ELSE last_error END,
            updated_at = CURRENT_TIMESTAMP
        WHERE broadcast_job_id = :jobId AND user_id IN (:userIds)
    """, nativeQuery = true)
    int updateEmailRecipientStatusBatchNative(
            @Param("jobId") Long jobId,
            @Param("userIds") List<Long> userIds,
            @Param("status") String status,
            @Param("error") String error
    );

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE broadcast_recipient_status
        SET aggregate_processed = true,
            aggregate_processed_at = CURRENT_TIMESTAMP,
            updated_at = CURRENT_TIMESTAMP
        WHERE broadcast_job_id = :jobId
          AND user_id = :userId
          AND aggregate_processed = false
          AND app_notification_status IN ('SENT', 'FAILED')
          AND (
              user_email IS NULL OR user_email = '' 
              OR email_status IN ('PROVIDER_ACCEPTED', 'DELIVERED', 'FAILED')
          )
    """, nativeQuery = true)
    int tryMarkAggregateProcessedNative(@Param("jobId") Long jobId, @Param("userId") Long userId);

    @Transactional
    @Query(value = """
        UPDATE broadcast_recipient_status
        SET aggregate_processed = true,
            aggregate_processed_at = CURRENT_TIMESTAMP,
            updated_at = CURRENT_TIMESTAMP
        WHERE broadcast_job_id = :jobId
          AND user_id IN (:userIds)
          AND aggregate_processed = false
          AND app_notification_status IN ('SENT', 'FAILED')
          AND (
              user_email IS NULL OR user_email = '' 
              OR email_status IN ('PROVIDER_ACCEPTED', 'DELIVERED', 'FAILED')
          )
        RETURNING user_id
    """, nativeQuery = true)
    List<Long> tryMarkAggregateProcessedBatchNative(@Param("jobId") Long jobId, @Param("userIds") List<Long> userIds);

    @Query(value = """
        SELECT user_id FROM broadcast_recipient_status
        WHERE broadcast_job_id = :jobId
          AND user_id IN (:userIds)
          AND aggregate_processed = true
    """, nativeQuery = true)
    List<Long> findAggregateProcessedUserIdsNative(@Param("jobId") Long jobId, @Param("userIds") List<Long> userIds);

    @Query(value = """
        SELECT COUNT(*) FROM broadcast_recipient_status
        WHERE broadcast_job_id = :jobId
          AND user_id IN (:userIds)
          AND app_notification_status = 'SENT'
          AND (user_email IS NULL OR user_email = '' OR email_status IN ('PROVIDER_ACCEPTED', 'DELIVERED'))
    """, nativeQuery = true)
    long countSuccessRecipientsInBatchNative(@Param("jobId") Long jobId, @Param("userIds") List<Long> userIds);

    @Query(value = """
        SELECT CASE WHEN (
            app_notification_status = 'SENT' 
            AND (user_email IS NULL OR user_email = '' OR email_status IN ('PROVIDER_ACCEPTED', 'DELIVERED'))
        ) THEN true ELSE false END
        FROM broadcast_recipient_status
        WHERE broadcast_job_id = :jobId AND user_id = :userId
    """, nativeQuery = true)
    Boolean isRecipientSuccessNative(@Param("jobId") Long jobId, @Param("userId") Long userId);

    @Query(value = "SELECT COUNT(*) FROM broadcast_recipient_status WHERE broadcast_job_id = :jobId", nativeQuery = true)
    long countByBroadcastJobIdNative(@Param("jobId") Long jobId);
}
