package com.example.repository;

import com.example.model.Report;
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
public interface ReportRepository extends JpaRepository<Report, Long> {

    // ================= BY STATUS =================
    Page<Report> findByStatus(Report.ReportStatus status, Pageable pageable);

    List<Report> findByStatus(Report.ReportStatus status);

    // String-based status (for admin API)
    Page<Report> findByStatus(String status, Pageable pageable);
    long countByStatus(String status);

    // ================= BY TYPE =================
    Page<Report> findByReportType(Report.ReportType reportType, Pageable pageable);

    // String-based category (for admin API) - mapping to reportType
    Page<Report> findByReportType(String reportType, Pageable pageable);
    long countByReportType(String reportType);

    // ================= BY ADMIN =================
    Page<Report> findByAssignedAdmin(com.example.model.Admin admin, Pageable pageable);

    List<Report> findByAssignedAdmin(com.example.model.Admin admin);

    // ================= BY USER =================
    List<Report> findByReportedUserId(Long userId);
    Page<Report> findByReportedUserId(Long userId, Pageable pageable);

    List<Report> findByReportedById(Long userId);

    // ================= DATE RANGE =================
    @Query("""
        SELECT r FROM Report r
        WHERE r.createdAt BETWEEN :startDate AND :endDate
    """)
    Page<Report> findByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    List<Report> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    // ================= COMPLEX QUERIES =================
    @Query("""
        SELECT r FROM Report r
        WHERE r.status = :status
        AND (:type IS NULL OR r.reportType = :type)
        AND (:adminId IS NULL OR r.assignedAdmin.id = :adminId)
        ORDER BY r.createdAt DESC
    """)
    Page<Report> findFiltered(
            @Param("status") Report.ReportStatus status,
            @Param("type") Report.ReportType type,
            @Param("adminId") Long adminId,
            Pageable pageable
    );

    @Query("""
        SELECT r FROM Report r
        WHERE r.reportedUser.id = :userId
        ORDER BY r.createdAt DESC
    """)
    List<Report> findReportsAgainstUser(@Param("userId") Long userId);

    @Query("""
        SELECT COUNT(r) FROM Report r
        WHERE r.status = :status
    """)
    Long countByStatus(@Param("status") Report.ReportStatus status);

    @Query("""
        SELECT r.reportType, COUNT(r) FROM Report r
        GROUP BY r.reportType
    """)
    List<Object[]> countByReportType();

    // Count by report type (replacing severity)
    @Query("SELECT COUNT(r) FROM Report r WHERE r.reportType = :reportType")
    long countByReportTypeString(@Param("reportType") String reportType);

    @Query("""
        SELECT r FROM Report r
        WHERE r.status = 'PENDING'
        ORDER BY r.createdAt ASC
    """)
    List<Report> findPendingReports();

    boolean existsByReportedUserIdAndReportedById(Long reportedUserId, Long reportedById);

    // ================= ADMIN ANALYTICS =================

    @Query("SELECT r.reportType, COUNT(r) FROM Report r GROUP BY r.reportType")
    Map<String, Long> countReportsByCategory();

    @Query(value = "SELECT r.reported_by_id, COUNT(r.id) as report_count FROM reports r GROUP BY r.reported_by_id ORDER BY report_count DESC LIMIT :limit", nativeQuery = true)
    List<Map<String, Object>> getTopReporters(@Param("limit") int limit);

    @Query(value = "SELECT r.reported_user_id, COUNT(r.id) as report_count FROM reports r GROUP BY r.reported_user_id ORDER BY report_count DESC LIMIT :limit", nativeQuery = true)
    List<Map<String, Object>> getMostReportedUsers(@Param("limit") int limit);

    // Count total and pending
    @Query("SELECT COUNT(r) FROM Report r WHERE r.status = PENDING")
    long countPendingReports();
}

