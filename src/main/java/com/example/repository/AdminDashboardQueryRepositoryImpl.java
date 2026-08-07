package com.example.repository;

import com.example.dto.response.AdminDashboardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminDashboardQueryRepositoryImpl
        implements AdminDashboardQueryRepository {

    private final JdbcTemplate jdbcTemplate; // entityManager;
    private static final String DASHBOARD_SQL = """
SELECT

    /* ================= USERS ================= */

    (SELECT COUNT(*) FROM users WHERE is_deleted = false) AS total_users,

    (SELECT COUNT(*) FROM users
        WHERE is_active = true
        AND is_deleted = false) AS active_users,

    (SELECT COUNT(*) FROM users
        WHERE is_active = false
        AND is_deleted = false) AS inactive_users,

    (SELECT COUNT(*) FROM users
        WHERE is_blocked = true
        AND is_deleted = false) AS blocked_users,

    (SELECT COUNT(*) FROM users
        WHERE email_verified = true
        AND phone_verified = true
        AND is_deleted = false) AS verified_users,

    (SELECT COUNT(*) FROM users
        WHERE email_verified = false
        AND is_deleted = false) AS unverified_users,

    /* ================= REPORTS ================= */

    (SELECT COUNT(*) FROM user_reports)
        AS total_reports,

    (SELECT COUNT(*) FROM user_reports
        WHERE status='PENDING')
        AS pending_reports,

    (SELECT COUNT(*) FROM user_reports
        WHERE status='APPROVED')
        AS resolved_reports,

    (SELECT COUNT(*) FROM user_reports
        WHERE status='REJECTED')
        AS closed_reports,

    /* ================= SUBSCRIPTIONS ================= */

    (SELECT COUNT(*) FROM user_subscriptions)
        AS total_subscriptions,

    (SELECT COUNT(*) FROM user_subscriptions
        WHERE is_active=true)
        AS active_subscriptions,

    (SELECT COUNT(*) FROM user_subscriptions
        WHERE status='EXPIRED')
        AS expired_subscriptions,

    /* ================= PAYMENTS ================= */

    (
        SELECT COALESCE(SUM(amount),0)
        FROM payments
        WHERE status='SUCCESS'
    ) AS total_revenue,

    (
        SELECT COUNT(*)
        FROM payments
    ) AS total_transactions,

    (
        SELECT COUNT(*)
        FROM payments
        WHERE status='SUCCESS'
    ) AS successful_transactions,

    (
        SELECT COUNT(*)
        FROM payments
        WHERE status='FAILED'
    ) AS failed_transactions,

    (
        SELECT COUNT(*)
        FROM payments
        WHERE status='PENDING'
    ) AS pending_transactions
""";
    @Override
    public AdminDashboardDTO getDashboardStatistics() {

        return jdbcTemplate.queryForObject(
                DASHBOARD_SQL,
                (rs, rowNum) -> {

                    AdminDashboardDTO dto = new AdminDashboardDTO();

                    // ================= USERS =================

                    dto.setTotalUsers(
                            rs.getLong("total_users"));

                    dto.setActiveUsers(
                            rs.getLong("active_users"));

                    dto.setInactiveUsers(
                            rs.getLong("inactive_users"));

                    dto.setBlockedUsers(
                            rs.getLong("blocked_users"));

                    dto.setVerifiedUsers(
                            rs.getLong("verified_users"));

                    dto.setUnverifiedUsers(
                            rs.getLong("unverified_users"));

                    // ================= REPORTS =================

                    dto.setTotalReports(
                            rs.getLong("total_reports"));

                    dto.setPendingReports(
                            rs.getLong("pending_reports"));

                    dto.setResolvedReports(
                            rs.getLong("resolved_reports"));

                    dto.setClosedReports(
                            rs.getLong("closed_reports"));

                    // ================= SUBSCRIPTIONS =================

                    dto.setTotalSubscriptions(
                            rs.getLong("total_subscriptions"));

                    dto.setActiveSubscriptions(
                            rs.getLong("active_subscriptions"));

                    dto.setExpiredSubscriptions(
                            rs.getLong("expired_subscriptions"));

                    // ================= PAYMENTS =================

                    dto.setTotalRevenue(
                            rs.getBigDecimal("total_revenue"));

                    dto.setTotalTransactions(
                            rs.getLong("total_transactions"));

                    dto.setSuccessfulTransactions(
                            rs.getLong("successful_transactions"));

                    dto.setFailedTransactions(
                            rs.getLong("failed_transactions"));

                    dto.setPendingTransactions(
                            rs.getLong("pending_transactions"));

                    return dto;
                }
        );
    }
}