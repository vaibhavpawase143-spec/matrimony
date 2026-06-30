package com.example.repository;

import com.example.model.ReportStatus;
import com.example.model.User;
import com.example.model.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserReportRepository extends
        JpaRepository<UserReport, Long>,
        JpaSpecificationExecutor<UserReport> {

    // ================= USER SIDE =================

    long countByReportedUser(User user);

    boolean existsByReporterAndReportedUser(
            User reporter,
            User reportedUser
    );

    boolean existsByReporterEmailAndReportedUserId(
            String email,
            Long reportedUserId
    );

    long countByReportedUserId(Long reportedUserId);

    // ================= ADMIN SIDE =================

    List<UserReport> findByStatus(ReportStatus status);

    long countByStatus(ReportStatus status);
    // ==========================================
// DASHBOARD - REPORT STATUS DISTRIBUTION
// ==========================================

    @Query(value = """
        SELECT status,
               COUNT(*)
        FROM user_reports
        GROUP BY status
        """, nativeQuery = true)
    List<Object[]> getReportStatusDistribution();
    @Query(value = """
        SELECT TO_CHAR(created_at, 'YYYY-MM') AS month,
               COUNT(*) AS total
        FROM user_reports
        GROUP BY TO_CHAR(created_at, 'YYYY-MM')
        ORDER BY month
        """, nativeQuery = true)
    List<Object[]> getMonthlyReports();
}