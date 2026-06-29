package com.example.repository;

import com.example.model.ReportStatus;
import com.example.model.User;
import com.example.model.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

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
}