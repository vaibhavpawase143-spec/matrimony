package com.example.repository;

import com.example.model.SubscriptionExpiryJob;
import com.example.model.SubscriptionExpiryJobStatus;
import com.example.model.SubscriptionExpiryJobType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionExpiryJobRepository extends JpaRepository<SubscriptionExpiryJob, Long> {

    boolean existsByIdempotencyKey(String idempotencyKey);

    boolean existsBySubscriptionIdAndJobType(Long subscriptionId, SubscriptionExpiryJobType jobType);

    Optional<SubscriptionExpiryJob> findByIdempotencyKey(String idempotencyKey);

    @EntityGraph(attributePaths = {"subscription", "user"})
    Optional<SubscriptionExpiryJob> findWithDetailsById(Long id);

    @EntityGraph(attributePaths = {"subscription", "user"})
    List<SubscriptionExpiryJob> findByStatus(SubscriptionExpiryJobStatus status);

    @EntityGraph(attributePaths = {"subscription", "user"})
    Page<SubscriptionExpiryJob> findByStatus(SubscriptionExpiryJobStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"subscription", "user"})
    @Query("SELECT j FROM SubscriptionExpiryJob j ORDER BY j.createdAt DESC")
    Page<SubscriptionExpiryJob> findAllWithDetails(Pageable pageable);
}
