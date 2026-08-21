package com.example.repository;

import com.example.model.NotificationJobOutbox;
import com.example.model.NotificationJobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationJobOutboxRepository extends JpaRepository<NotificationJobOutbox, Long> {

    Optional<NotificationJobOutbox> findByIdempotencyKey(String idempotencyKey);

    boolean existsByIdempotencyKey(String idempotencyKey);

    List<NotificationJobOutbox> findTop100ByStatusOrderByCreatedAtAsc(NotificationJobStatus status);

    /**
     * Atomic Claim: Safely transitions job from PENDING to PROCESSING.
     * Returns 1 if claimed by current consumer thread, 0 if already claimed by another thread.
     */
    @Modifying
    @Query("UPDATE NotificationJobOutbox n SET n.status = 'PROCESSING', n.startedAt = :now, n.updatedAt = :now " +
           "WHERE n.idempotencyKey = :idempotencyKey AND (n.status = 'PENDING' OR n.status = 'FAILED')")
    int claimJobAtomically(@Param("idempotencyKey") String idempotencyKey, @Param("now") LocalDateTime now);
}
