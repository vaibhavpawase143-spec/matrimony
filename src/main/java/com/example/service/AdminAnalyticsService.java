package com.example.service;

import com.example.dto.response.AdminDashboardDTO;

public interface AdminAnalyticsService {

    // ================= DASHBOARD =================
    AdminDashboardDTO getFullDashboard();

    AdminDashboardDTO getDashboardForDateRange(String startDate, String endDate);

    // ================= USER STATISTICS =================
    Long getTotalUsers();

    Long getActiveUsersCount();

    Long getInactiveUsersCount();

    Long getBlockedUsersCount();

    Long getVerifiedUsersCount();

    Long getUnverifiedUsersCount();

    Long getNewUsersThisMonth();

    Long getNewUsersThisWeek();

    Long getNewUsersToday();

    // ================= PAYMENT STATISTICS =================
    java.math.BigDecimal getTotalRevenue();

    java.math.BigDecimal getRevenueThisMonth();

    java.math.BigDecimal getRevenueThisWeek();

    Long getTotalTransactions();

    Long getSuccessfulTransactions();

    Long getFailedTransactions();

    Long getPendingTransactions();

    // ================= SUBSCRIPTION STATISTICS =================
    Long getActiveSubscriptions();

    Long getExpiredSubscriptions();

    Long getTotalSubscriptions();

    // ================= GROWTH METRICS =================
    Double getUserGrowthPercentage();

    Double getRevenueGrowthPercentage();

    Double getSubscriptionGrowthPercentage();
}

