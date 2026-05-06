package com.example.repository;

import com.example.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    // Find by admin
    Page<AuditLog> findByAdminId(Long adminId, Pageable pageable);

    // Find by action
    Page<AuditLog> findByAction(String action, Pageable pageable);

    // Find by entity type
    Page<AuditLog> findByEntityType(String entityType, Pageable pageable);

    // Find by entity id
    Page<AuditLog> findByEntityId(Long entityId, Pageable pageable);

    // Find by date range
    Page<AuditLog> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // Find by IP address
    Page<AuditLog> findByIpAddress(String ipAddress, Pageable pageable);

    // Find suspicious activities
    Page<AuditLog> findBySuspiciousActivityTrue(Pageable pageable);

    // Count by suspicious activity
    long countBySuspiciousActivityTrue();

    // Count logs created after
    @Query("SELECT COUNT(a) FROM AuditLog a WHERE a.createdAt > :date")
    long countLogsCreatedAfter(@Param("date") LocalDateTime date);

    // Count logs by action
    @Query("SELECT a.action, COUNT(a) FROM AuditLog a GROUP BY a.action")
    Map<String, Long> countLogsByAction();

    // Count logs by entity type
    @Query("SELECT a.entityType, COUNT(a) FROM AuditLog a GROUP BY a.entityType")
    Map<String, Long> countLogsByEntityType();

    // Get admin activity stats
    @Query("SELECT a.adminId, COUNT(a) FROM AuditLog a GROUP BY a.adminId ORDER BY COUNT(a) DESC")
    Map<Long, Long> getAdminActivityStats();

    // Get hourly distribution
    @Query("SELECT HOUR(a.createdAt), COUNT(a) FROM AuditLog a GROUP BY HOUR(a.createdAt)")
    Map<Integer, Long> getHourlyDistribution();

    // Get most active admins
    @Query(value = "SELECT a.admin_id, a.admin_email, COUNT(a.id) as activity_count FROM audit_logs a GROUP BY a.admin_id ORDER BY activity_count DESC LIMIT :limit", nativeQuery = true)
    List<Map<String, Object>> getMostActiveAdmins(@Param("limit") int limit);

    // Find logs created after
    Page<AuditLog> findByCreatedAtAfter(LocalDateTime date, Pageable pageable);
}

