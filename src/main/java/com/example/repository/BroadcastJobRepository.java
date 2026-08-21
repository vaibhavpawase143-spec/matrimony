package com.example.repository;

import com.example.model.BroadcastJob;
import com.example.model.BroadcastJobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface BroadcastJobRepository extends JpaRepository<BroadcastJob, Long> {
    List<BroadcastJob> findByStatus(BroadcastJobStatus status);

    List<BroadcastJob> findByStatusIn(List<BroadcastJobStatus> statuses);

    Optional<BroadcastJob> findFirstByStatusInOrderByCreatedAtDesc(List<BroadcastJobStatus> statuses);

    Optional<BroadcastJob> findFirstByTitleAndMessageAndStatusIn(String title, String message, List<BroadcastJobStatus> statuses);

    Page<BroadcastJob> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE broadcast_jobs
        SET status = 'INTERRUPTED',
            updated_at = CURRENT_TIMESTAMP
        WHERE status IN ('PENDING', 'IN_PROGRESS')
    """, nativeQuery = true)
    int markActiveJobsAsInterruptedNative();

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE broadcast_jobs
        SET status = 'IN_PROGRESS',
            updated_at = CURRENT_TIMESTAMP
        WHERE id = :jobId
          AND status = 'INTERRUPTED'
    """, nativeQuery = true)
    int tryResumeInterruptedJobNative(@Param("jobId") Long jobId);

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE broadcast_jobs
        SET status = 'CANCELLED',
            completed_at = CURRENT_TIMESTAMP,
            updated_at = CURRENT_TIMESTAMP
        WHERE id = :jobId
          AND status IN ('INTERRUPTED', 'IN_PROGRESS', 'PENDING')
    """, nativeQuery = true)
    int tryCancelInterruptedOrActiveJobNative(@Param("jobId") Long jobId);

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE broadcast_jobs
        SET processed_recipients = processed_recipients + :successInc + :failInc,
            successful_recipients = successful_recipients + :successInc,
            failed_recipients = failed_recipients + :failInc,
            updated_at = CURRENT_TIMESTAMP
        WHERE id = :jobId
    """, nativeQuery = true)
    int incrementJobCountersNative(@Param("jobId") Long jobId, @Param("successInc") int successInc, @Param("failInc") int failInc);

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE broadcast_jobs
        SET status = CASE WHEN failed_recipients > 0 THEN 'COMPLETED_WITH_FAILURES' ELSE 'COMPLETED' END,
            completed_at = CURRENT_TIMESTAMP,
            updated_at = CURRENT_TIMESTAMP
        WHERE id = :jobId
          AND status = 'IN_PROGRESS'
          AND processed_recipients >= total_recipients
    """, nativeQuery = true)
    int tryMarkJobCompletedNative(@Param("jobId") Long jobId);

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE broadcast_jobs
        SET enqueued_recipients = :enqueuedCount,
            last_processed_user_id = :lastUserId,
            updated_at = CURRENT_TIMESTAMP
        WHERE id = :jobId
    """, nativeQuery = true)
    int updateEnqueuedRecipientsNative(@Param("jobId") Long jobId, @Param("enqueuedCount") Long enqueuedCount, @Param("lastUserId") Long lastUserId);
}
