package com.example.service.impl;

import com.example.dto.response.AdminDashboardDTO;
import com.example.repository.*;
import com.example.service.AdminAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminAnalyticsServiceImpl implements AdminAnalyticsService {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final ReportRepository reportRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final CityRepository cityRepository;
    private final ReligionRepository religionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;

    @Override
    public AdminDashboardDTO getFullDashboard() {
        return buildDashboard(null, null);
    }

    @Override
    public AdminDashboardDTO getDashboardForDateRange(String startDate, String endDate) {
        return buildDashboard(startDate, endDate);
    }

    private AdminDashboardDTO buildDashboard(String startDateStr, String endDateStr) {
        return AdminDashboardDTO.builder()
                // User Stats
                .totalUsers(getTotalUsers())
                .activeUsers(getActiveUsersCount())
                .inactiveUsers(getInactiveUsersCount())
                .blockedUsers(getBlockedUsersCount())
                .verifiedUsers(getVerifiedUsersCount())
                .unverifiedUsers(getUnverifiedUsersCount())
                // New Users
                .newUsersThisMonth(getNewUsersThisMonth())
                .newUsersThisWeek(getNewUsersThisWeek())
                .newUsersToday(getNewUsersToday())
                // Payment Stats
                .totalRevenue(getTotalRevenue())
                .revenueThisMonth(getRevenueThisMonth())
                .revenueThisWeek(getRevenueThisWeek())
                .totalTransactions(getTotalTransactions())
                .successfulTransactions(getSuccessfulTransactions())
                .failedTransactions(getFailedTransactions())
                .pendingTransactions(getPendingTransactions())
                // Report Stats
                .totalReports(getTotalReportsCount())
                .pendingReports(reportRepository.countByStatus(com.example.model.Report.ReportStatus.PENDING))
                .resolvedReports(reportRepository.countByStatus(com.example.model.Report.ReportStatus.RESOLVED))
                .closedReports(reportRepository.countByStatus(com.example.model.Report.ReportStatus.CLOSED))
                // Subscription Stats
                .activeSubscriptions(getActiveSubscriptions())
                .expiredSubscriptions(getExpiredSubscriptions())
                .totalSubscriptions(getTotalSubscriptions())
                // Growth
                .userGrowthPercentage(getUserGrowthPercentage())
                .revenueGrowthPercentage(getRevenueGrowthPercentage())
                .subscriptionGrowthPercentage(getSubscriptionGrowthPercentage())
                .build();
    }

    // ================= USER STATISTICS =================

    @Override
    public Long getTotalUsers() {
        return userRepository.count();
    }

    @Override
    public Long getActiveUsersCount() {
        return (long) userRepository.findByIsActiveTrue().size();
    }

    @Override
    public Long getInactiveUsersCount() {
        return getTotalUsers() - getActiveUsersCount();
    }

    @Override
    public Long getBlockedUsersCount() {
        // Assuming there's a field for blocked users
        // You might need to add this query to UserRepository
        return 0L; // Placeholder
    }

    @Override
    public Long getVerifiedUsersCount() {
        return userRepository.count(); // You need to add this query
    }

    @Override
    public Long getUnverifiedUsersCount() {
        return getTotalUsers() - getVerifiedUsersCount();
    }

    @Override
    public Long getNewUsersThisMonth() {
        LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        // Query needed: findByCreatedAtAfter(monthStart)
        return 0L; // Placeholder
    }

    @Override
    public Long getNewUsersThisWeek() {
        LocalDateTime weekStart = LocalDateTime.now().minusDays(7);
        return 0L; // Placeholder
    }

    @Override
    public Long getNewUsersToday() {
        LocalDateTime dayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        return 0L; // Placeholder
    }

    // ================= PAYMENT STATISTICS =================

    @Override
    public BigDecimal getTotalRevenue() {
        List<com.example.model.Payment> payments = paymentRepository.findByStatus("SUCCESS");
        return payments.stream()
                .map(p -> p.getAmount() != null ? p.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getRevenueThisMonth() {
        LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        List<com.example.model.Payment> payments = paymentRepository.findByStatus("SUCCESS");
        return payments.stream()
                .filter(p -> p.getCreatedAt() != null && p.getCreatedAt().isAfter(monthStart))
                .map(p -> p.getAmount() != null ? p.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getRevenueThisWeek() {
        LocalDateTime weekStart = LocalDateTime.now().minusDays(7);
        List<com.example.model.Payment> payments = paymentRepository.findByStatus("SUCCESS");
        return payments.stream()
                .filter(p -> p.getCreatedAt() != null && p.getCreatedAt().isAfter(weekStart))
                .map(p -> p.getAmount() != null ? p.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Long getTotalTransactions() {
        return paymentRepository.count();
    }

    @Override
    public Long getSuccessfulTransactions() {
        return (long) paymentRepository.findByStatus("SUCCESS").size();
    }

    @Override
    public Long getFailedTransactions() {
        return (long) paymentRepository.findByStatus("FAILED").size();
    }

    @Override
    public Long getPendingTransactions() {
        return (long) paymentRepository.findByStatus("PENDING").size();
    }

    // ================= SUBSCRIPTION STATISTICS =================

    @Override
    public Long getActiveSubscriptions() {
        return subscriptionRepository.countActive();
    }

    @Override
    public Long getExpiredSubscriptions() {
        return subscriptionRepository.countExpired();
    }

    @Override
    public Long getTotalSubscriptions() {
        return subscriptionRepository.count();
    }

    // ================= GROWTH METRICS =================

    @Override
    public Double getUserGrowthPercentage() {
        Long thisMonth = getNewUsersThisMonth();
        Long lastMonth = getNewUsersLastMonth();
        
        if (lastMonth == 0) return 100.0;
        return ((double) (thisMonth - lastMonth) / lastMonth) * 100;
    }

    @Override
    public Double getRevenueGrowthPercentage() {
        BigDecimal thisMonth = getRevenueThisMonth();
        BigDecimal lastMonth = getRevenueLastMonth();
        
        if (lastMonth.compareTo(BigDecimal.ZERO) == 0) return 100.0;
        return thisMonth.subtract(lastMonth)
                .divide(lastMonth, 2, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }

    @Override
    public Double getSubscriptionGrowthPercentage() {
        Long thisMonth = getSubscriptionsThisMonth();
        Long lastMonth = getSubscriptionsLastMonth();
        
        if (lastMonth == 0) return 100.0;
        return ((double) (thisMonth - lastMonth) / lastMonth) * 100;
    }

    // ================= HELPER METHODS =================

    private Long getTotalReportsCount() {
        return reportRepository.count();
    }

    private Long getNewUsersLastMonth() {
        LocalDateTime monthStart = LocalDateTime.now().minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime monthEnd = monthStart.plusMonths(1);
        return 0L; // Placeholder
    }

    private BigDecimal getRevenueLastMonth() {
        LocalDateTime monthStart = LocalDateTime.now().minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        List<com.example.model.Payment> payments = paymentRepository.findByStatus("SUCCESS");
        return payments.stream()
                .filter(p -> p.getCreatedAt() != null && p.getCreatedAt().isAfter(monthStart))
                .map(p -> p.getAmount() != null ? p.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Long getSubscriptionsThisMonth() {
        LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        return subscriptionRepository.countByCreatedAtAfter(monthStart);
    }

    private Long getSubscriptionsLastMonth() {
        LocalDateTime monthStart = LocalDateTime.now().minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime monthEnd = monthStart.plusMonths(1);
        return subscriptionRepository.countByCreatedAtBetween(monthStart, monthEnd);
    }
}

