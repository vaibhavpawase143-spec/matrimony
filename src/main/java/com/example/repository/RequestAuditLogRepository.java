package com.example.repository;

import com.example.model.RequestAuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestAuditLogRepository extends JpaRepository<RequestAuditLog, Long> {
    Page<RequestAuditLog> findByActorId(Long actorId, Pageable pageable);
}
