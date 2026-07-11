package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardDTO {

    // ================= USER STATISTICS =================

    private Long totalUsers;
    private Long activeUsers;
    private Long inactiveUsers;

    // Optional (implement later if supported)
    private Long blockedUsers;
    private Long verifiedUsers;
    private Long unverifiedUsers;

    // ================= NEW USER STATISTICS =================

    private Long newUsersThisMonth;
    private Long newUsersThisWeek;
    private Long newUsersToday;

    // ================= PAYMENT STATISTICS =================

    private BigDecimal totalRevenue;
    private BigDecimal revenueThisMonth;
    private BigDecimal revenueThisWeek;

    private Long totalTransactions;
    private Long successfulTransactions;
    private Long failedTransactions;
    private Long pendingTransactions;

    // ================= REPORT STATISTICS =================

    private Long totalReports;
    private Long pendingReports;
    private Long resolvedReports;
    private Long closedReports;

    // ================= SUBSCRIPTION STATISTICS =================

    private Long activeSubscriptions;
    private Long expiredSubscriptions;
    private Long totalSubscriptions;

    // ================= GROWTH STATISTICS =================

    private Double userGrowthPercentage;
    private Double revenueGrowthPercentage;
    private Double subscriptionGrowthPercentage;

    // ================= TOP ANALYTICS =================

    private List<TopPaymentPlanDTO> topPaymentPlans;
    private List<TopCityDTO> topCities;
    private List<TopReligionDTO> topReligions;

    // ================= CHART DATA =================

    private Map<String, Long> userRegistrationTrend;

    private Map<String, BigDecimal> revenueTrend;

    private Map<String, Long> reportsTrend;

    private Map<String, Long> reportTypeDistribution;

    private Map<String, Long> paymentMethodDistribution;

    // =======================================================
    // NESTED DTOs
    // =======================================================

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopPaymentPlanDTO {

        private Long planId;
        private String planName;
        private Long subscriptionCount;
        private BigDecimal totalRevenue;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopCityDTO {

        private Long cityId;
        private String cityName;
        private Long userCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopReligionDTO {

        private Long religionId;
        private String religionName;
        private Long userCount;
    }
}