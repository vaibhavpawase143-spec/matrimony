package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDashboardDTO {

    // ================= USER STATS =================
    private Long totalUsers;
    private Long activeUsers;
    private Long inactiveUsers;
    private Long blockedUsers;
    private Long verifiedUsers;
    private Long unverifiedUsers;

    // ================= NEW USERS =================
    private Long newUsersThisMonth;
    private Long newUsersThisWeek;
    private Long newUsersToday;

    // ================= PAYMENT STATS =================
    private BigDecimal totalRevenue;
    private BigDecimal revenueThisMonth;
    private BigDecimal revenueThisWeek;
    private Long totalTransactions;
    private Long successfulTransactions;
    private Long failedTransactions;
    private Long pendingTransactions;

    // ================= REPORT STATS =================
    private Long totalReports;
    private Long pendingReports;
    private Long resolvedReports;
    private Long closedReports;

    // ================= SUBSCRIPTION STATS =================
    private Long activeSubscriptions;
    private Long expiredSubscriptions;
    private Long totalSubscriptions;

    // ================= GROWTH =================
    private Double userGrowthPercentage;
    private Double revenueGrowthPercentage;
    private Double subscriptionGrowthPercentage;

    // ================= TOP DATA =================
    private List<TopPaymentPlanDTO> topPaymentPlans;
    private List<TopCityDTO> topCities;
    private List<TopReligionDTO> topReligions;

    // ================= CHARTS DATA =================
    private Map<String, Long> userRegistrationTrend; // Date -> Count
    private Map<String, BigDecimal> revenueTrend; // Date -> Amount
    private Map<String, Long> reportsTrend; // Date -> Count
    private Map<String, Long> reportTypeDistribution; // Type -> Count
    private Map<String, Long> paymentMethodDistribution; // Method -> Count

    // ================= NESTED DTOs =================

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TopPaymentPlanDTO {
        private Long planId;
        private String planName;
        private Long subscriptionCount;
        private BigDecimal totalRevenue;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TopCityDTO {
        private Long cityId;
        private String cityName;
        private Long userCount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TopReligionDTO {
        private Long religionId;
        private String religionName;
        private Long userCount;
    }
}

