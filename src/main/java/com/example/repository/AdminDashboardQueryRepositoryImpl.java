package com.example.repository;

import com.example.dto.response.AdminDashboardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminDashboardQueryRepositoryImpl
        implements AdminDashboardQueryRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String DASHBOARD_SQL = """
        SELECT

            /* ================= USERS ================= */

            (SELECT COUNT(*) FROM users WHERE is_deleted = false) AS total_users,

            (SELECT COUNT(*) FROM users WHERE is_active = true AND is_deleted = false) AS active_users,

            (SELECT COUNT(*) FROM users WHERE is_active = false AND is_deleted = false) AS inactive_users,

            (SELECT COUNT(*) FROM users WHERE is_blocked = true AND is_deleted = false) AS blocked_users,

            (SELECT COUNT(*) FROM users WHERE email_verified = true AND phone_verified = true AND is_deleted = false) AS verified_users,

            (SELECT COUNT(*) FROM users WHERE email_verified = false AND is_deleted = false) AS unverified_users,

            (SELECT COUNT(*) FROM users WHERE created_at >= DATE_TRUNC('month', CURRENT_DATE) AND is_deleted = false) AS new_users_this_month,

            (SELECT COUNT(*) FROM users WHERE created_at >= DATE_TRUNC('week', CURRENT_DATE) AND is_deleted = false) AS new_users_this_week,

            (SELECT COUNT(*) FROM users WHERE created_at >= CURRENT_DATE AND is_deleted = false) AS new_users_today,

            (SELECT COUNT(*) FROM users WHERE created_at >= DATE_TRUNC('month', CURRENT_DATE) AND is_deleted = false) AS current_month_users,

            (SELECT COUNT(*) FROM users WHERE created_at >= DATE_TRUNC('month', CURRENT_DATE - INTERVAL '1 month') AND created_at < DATE_TRUNC('month', CURRENT_DATE) AND is_deleted = false) AS previous_month_users,

            /* ================= REPORTS ================= */

            (SELECT COUNT(*) FROM user_reports) AS total_reports,

            (SELECT COUNT(*) FROM user_reports WHERE status = 'PENDING') AS pending_reports,

            (SELECT COUNT(*) FROM user_reports WHERE status = 'APPROVED') AS resolved_reports,

            (SELECT COUNT(*) FROM user_reports WHERE status = 'REJECTED') AS closed_reports,

            /* ================= SUBSCRIPTIONS ================= */

            (SELECT COUNT(*) FROM user_subscriptions) AS total_subscriptions,

            (SELECT COUNT(*) FROM user_subscriptions WHERE is_active = true) AS active_subscriptions,

            (SELECT COUNT(*) FROM user_subscriptions WHERE status = 'EXPIRED') AS expired_subscriptions,

            (SELECT COUNT(*) FROM user_subscriptions WHERE created_at >= DATE_TRUNC('month', CURRENT_DATE)) AS current_month_subscriptions,

            (SELECT COUNT(*) FROM user_subscriptions WHERE created_at >= DATE_TRUNC('month', CURRENT_DATE - INTERVAL '1 month') AND created_at < DATE_TRUNC('month', CURRENT_DATE)) AS previous_month_subscriptions,

            /* ================= PAYMENTS ================= */

            (SELECT COALESCE(SUM(amount), 0) FROM payments WHERE status = 'SUCCESS') AS total_revenue,

            (SELECT COALESCE(SUM(amount), 0) FROM payments WHERE status = 'SUCCESS' AND created_at >= DATE_TRUNC('month', CURRENT_DATE)) AS current_month_revenue,

            (SELECT COALESCE(SUM(amount), 0) FROM payments WHERE status = 'SUCCESS' AND created_at >= DATE_TRUNC('month', CURRENT_DATE - INTERVAL '1 month') AND created_at < DATE_TRUNC('month', CURRENT_DATE)) AS previous_month_revenue,

            (SELECT COUNT(*) FROM payments) AS total_transactions,

            (SELECT COUNT(*) FROM payments WHERE status = 'SUCCESS') AS successful_transactions,

            (SELECT COUNT(*) FROM payments WHERE status = 'FAILED') AS failed_transactions,

            (SELECT COUNT(*) FROM payments WHERE status = 'PENDING') AS pending_transactions
        """;

    @Override
    public AdminDashboardDTO getDashboardStatistics() {

        return jdbcTemplate.queryForObject(
                DASHBOARD_SQL,
                (rs, rowNum) -> {

                    AdminDashboardDTO dto = new AdminDashboardDTO();

                    // ================= USERS =================
                    long totalUsers = rs.getLong("total_users");
                    long activeUsers = rs.getLong("active_users");
                    dto.setTotalUsers(totalUsers);
                    dto.setActiveUsers(activeUsers);
                    dto.setInactiveUsers(rs.getLong("inactive_users"));
                    dto.setBlockedUsers(rs.getLong("blocked_users"));
                    dto.setVerifiedUsers(rs.getLong("verified_users"));
                    dto.setUnverifiedUsers(rs.getLong("unverified_users"));

                    dto.setNewUsersThisMonth(rs.getLong("new_users_this_month"));
                    dto.setNewUsersThisWeek(rs.getLong("new_users_this_week"));
                    dto.setNewUsersToday(rs.getLong("new_users_today"));

                    long currentMonthUsers = rs.getLong("current_month_users");
                    long previousMonthUsers = rs.getLong("previous_month_users");

                    // ================= REPORTS =================
                    dto.setTotalReports(rs.getLong("total_reports"));
                    dto.setPendingReports(rs.getLong("pending_reports"));
                    dto.setResolvedReports(rs.getLong("resolved_reports"));
                    dto.setClosedReports(rs.getLong("closed_reports"));

                    // ================= SUBSCRIPTIONS =================
                    dto.setTotalSubscriptions(rs.getLong("total_subscriptions"));
                    dto.setActiveSubscriptions(rs.getLong("active_subscriptions"));
                    dto.setExpiredSubscriptions(rs.getLong("expired_subscriptions"));

                    long currentMonthSubscriptions = rs.getLong("current_month_subscriptions");
                    long previousMonthSubscriptions = rs.getLong("previous_month_subscriptions");

                    // ================= PAYMENTS =================
                    BigDecimal totalRevenue = rs.getBigDecimal("total_revenue");
                    BigDecimal currentMonthRevenue = rs.getBigDecimal("current_month_revenue");
                    BigDecimal previousMonthRevenue = rs.getBigDecimal("previous_month_revenue");

                    dto.setTotalRevenue(totalRevenue);
                    dto.setRevenueThisMonth(currentMonthRevenue);

                    dto.setTotalTransactions(rs.getLong("total_transactions"));
                    dto.setSuccessfulTransactions(rs.getLong("successful_transactions"));
                    dto.setFailedTransactions(rs.getLong("failed_transactions"));
                    dto.setPendingTransactions(rs.getLong("pending_transactions"));

                    // ================= GROWTH PERCENTAGES =================
                    dto.setUserGrowthPercentage(calculateGrowth(currentMonthUsers, previousMonthUsers));
                    dto.setRevenueGrowthPercentage(calculateGrowth(currentMonthRevenue, previousMonthRevenue));
                    dto.setSubscriptionGrowthPercentage(calculateGrowth(currentMonthSubscriptions, previousMonthSubscriptions));

                    return dto;
                }
        );
    }

    private Double calculateGrowth(Number current, Number previous) {
        double currentValue = current != null ? current.doubleValue() : 0.0;
        double previousValue = previous != null ? previous.doubleValue() : 0.0;

        if (previousValue == 0) {
            return currentValue > 0 ? 100.0 : 0.0;
        }

        return ((currentValue - previousValue) / previousValue) * 100.0;
    }
}