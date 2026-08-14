package com.example.service;

import com.example.model.ReportStatus;
import com.example.repository.PaymentRepository;
import com.example.repository.UserReportRepository;
import com.example.repository.UserRepository;
import com.example.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminDashboardAsyncService {

    private final UserRepository userRepository;

    private final PaymentRepository paymentRepository;

    private final UserReportRepository reportRepository;

    private final UserSubscriptionRepository subscriptionRepository;

    // =====================================================
    // USER STATISTICS
    // =====================================================

    @Async("applicationTaskExecutor")
    public CompletableFuture<Long> totalUsers() {

        long start = System.currentTimeMillis();

        Long result = userRepository.countByIsDeletedFalse();

        log.info("totalUsers() : {} ms",
                System.currentTimeMillis() - start);

        return CompletableFuture.completedFuture(result);
    }

    @Async("applicationTaskExecutor")
    public CompletableFuture<Long> activeUsers() {

        long start = System.currentTimeMillis();

        Long result =
                userRepository.countByIsActiveTrueAndIsDeletedFalse();

        log.info("activeUsers() : {} ms",
                System.currentTimeMillis() - start);

        return CompletableFuture.completedFuture(result);
    }

    @Async("applicationTaskExecutor")
    public CompletableFuture<Long> inactiveUsers() {

        long start = System.currentTimeMillis();

        Long total = userRepository.countByIsDeletedFalse();
        Long active = userRepository.countByIsActiveTrueAndIsDeletedFalse();

        Long result = (total != null ? total : 0L) - (active != null ? active : 0L);

        log.info("inactiveUsers() : {} ms",
                System.currentTimeMillis() - start);

        return CompletableFuture.completedFuture(result);
    }

    @Async("applicationTaskExecutor")
    public CompletableFuture<Long> blockedUsers() {

        long start = System.currentTimeMillis();

        Long result =
                userRepository.countByIsBlockedTrueAndIsDeletedFalse();

        log.info("blockedUsers() : {} ms",
                System.currentTimeMillis() - start);

        return CompletableFuture.completedFuture(result);
    }

    @Async("applicationTaskExecutor")
    public CompletableFuture<Long> verifiedUsers() {

        long start = System.currentTimeMillis();

        Long result =
                userRepository.countByEmailVerifiedTrueAndPhoneVerifiedTrueAndIsDeletedFalse();

        log.info("verifiedUsers() : {} ms",
                System.currentTimeMillis() - start);

        return CompletableFuture.completedFuture(result);
    }

    @Async("applicationTaskExecutor")
    public CompletableFuture<Long> unverifiedUsers() {

        long start = System.currentTimeMillis();

        Long result =
                userRepository.countByEmailVerifiedFalseAndIsDeletedFalse();

        log.info("unverifiedUsers() : {} ms",
                System.currentTimeMillis() - start);

        return CompletableFuture.completedFuture(result);
    }

    @Async("applicationTaskExecutor")
    public CompletableFuture<Long> newUsersThisMonth() {

        long start = System.currentTimeMillis();

        Long result =
                userRepository.findNewUsersCount(
                        LocalDateTime.now().minusMonths(1)
                );

        log.info("newUsersThisMonth() : {} ms",
                System.currentTimeMillis() - start);

        return CompletableFuture.completedFuture(result);
    }

    @Async("applicationTaskExecutor")
    public CompletableFuture<Long> newUsersThisWeek() {

        long start = System.currentTimeMillis();

        Long result =
                userRepository.findNewUsersCount(
                        LocalDateTime.now().minusWeeks(1)
                );

        log.info("newUsersThisWeek() : {} ms",
                System.currentTimeMillis() - start);

        return CompletableFuture.completedFuture(result);
    }

    @Async("applicationTaskExecutor")
    public CompletableFuture<Long> newUsersToday() {

        long start = System.currentTimeMillis();

        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        Long result =
                userRepository.findUsersCountByDate(
                        startOfDay,
                        endOfDay
                );

        log.info("newUsersToday() : {} ms",
                System.currentTimeMillis() - start);

        return CompletableFuture.completedFuture(result);
    }

    // =====================================================
    // PAYMENT STATISTICS
    // =====================================================

    @Async("applicationTaskExecutor")
    public CompletableFuture<BigDecimal> totalRevenue() {

        long start = System.currentTimeMillis();

        BigDecimal result = paymentRepository.getTotalRevenue();

        log.info("totalRevenue() : {} ms",
                System.currentTimeMillis() - start);

        return CompletableFuture.completedFuture(result);
    }

    @Async("applicationTaskExecutor")
    public CompletableFuture<Long> totalTransactions() {

        long start = System.currentTimeMillis();

        Long result = paymentRepository.count();

        log.info("totalTransactions() : {} ms",
                System.currentTimeMillis() - start);

        return CompletableFuture.completedFuture(result);
    }

    @Async("applicationTaskExecutor")
    public CompletableFuture<Long> successfulTransactions() {

        long start = System.currentTimeMillis();

        Long result =
                paymentRepository.countByStatus("SUCCESS");

        log.info("successfulTransactions() : {} ms",
                System.currentTimeMillis() - start);

        return CompletableFuture.completedFuture(result);
    }

    @Async("applicationTaskExecutor")
    public CompletableFuture<Long> failedTransactions() {

        long start = System.currentTimeMillis();

        Long result =
                paymentRepository.countByStatus("FAILED");

        log.info("failedTransactions() : {} ms",
                System.currentTimeMillis() - start);

        return CompletableFuture.completedFuture(result);
    }

    @Async("applicationTaskExecutor")
    public CompletableFuture<Long> pendingTransactions() {

        long start = System.currentTimeMillis();

        Long result =
                paymentRepository.countByStatus("PENDING");

        log.info("pendingTransactions() : {} ms",
                System.currentTimeMillis() - start);

        return CompletableFuture.completedFuture(result);
    }

    @Async("applicationTaskExecutor")
    public CompletableFuture<BigDecimal> currentMonthRevenue() {

        long start = System.currentTimeMillis();

        BigDecimal result =
                paymentRepository.getCurrentMonthRevenue();

        log.info("currentMonthRevenue() : {} ms",
                System.currentTimeMillis() - start);

        return CompletableFuture.completedFuture(result);
    }

    @Async("applicationTaskExecutor")
    public CompletableFuture<BigDecimal> previousMonthRevenue() {

        long start = System.currentTimeMillis();

        BigDecimal result =
                paymentRepository.getPreviousMonthRevenue();

        log.info("previousMonthRevenue() : {} ms",
                System.currentTimeMillis() - start);

        return CompletableFuture.completedFuture(result);
    }

    // =====================================================
    // REPORT STATISTICS
    // =====================================================

    @Async("applicationTaskExecutor")
    public CompletableFuture<Long> totalReports() {

        long start = System.currentTimeMillis();

        Long result = reportRepository.count();

        log.info("totalReports() : {} ms",
                System.currentTimeMillis() - start);

        return CompletableFuture.completedFuture(result);
    }

    @Async("applicationTaskExecutor")
    public CompletableFuture<Long> pendingReports() {

        long start = System.currentTimeMillis();

        Long result =
                reportRepository.countByStatus(ReportStatus.PENDING);

        log.info("pendingReports() : {} ms",
                System.currentTimeMillis() - start);

        return CompletableFuture.completedFuture(result);
    }

    @Async("applicationTaskExecutor")
    public CompletableFuture<Long> resolvedReports() {

        long start = System.currentTimeMillis();

        Long result =
                reportRepository.countByStatus(ReportStatus.APPROVED);

        log.info("resolvedReports() : {} ms",
                System.currentTimeMillis() - start);

        return CompletableFuture.completedFuture(result);
    }

    @Async("applicationTaskExecutor")
    public CompletableFuture<Long> closedReports() {

        long start = System.currentTimeMillis();

        Long result =
                reportRepository.countByStatus(ReportStatus.REJECTED);

        log.info("closedReports() : {} ms",
                System.currentTimeMillis() - start);

        return CompletableFuture.completedFuture(result);
    }

    // =====================================================
    // SUBSCRIPTION STATISTICS
    // =====================================================

    @Async("applicationTaskExecutor")
    public CompletableFuture<Long> totalSubscriptions() {

        long start = System.currentTimeMillis();

        Long result = subscriptionRepository.count();

        log.info("totalSubscriptions() : {} ms",
                System.currentTimeMillis() - start);

        return CompletableFuture.completedFuture(result);
    }

    @Async("applicationTaskExecutor")
    public CompletableFuture<Long> activeSubscriptions() {

        long start = System.currentTimeMillis();

        Long result =
                subscriptionRepository.countByIsActiveTrue();

        log.info("activeSubscriptions() : {} ms",
                System.currentTimeMillis() - start);

        return CompletableFuture.completedFuture(result);
    }

    @Async("applicationTaskExecutor")
    public CompletableFuture<Long> expiredSubscriptions() {

        long start = System.currentTimeMillis();

        Long result =
                subscriptionRepository.countByStatus("EXPIRED");

        log.info("expiredSubscriptions() : {} ms",
                System.currentTimeMillis() - start);

        return CompletableFuture.completedFuture(result);
    }

    @Async("applicationTaskExecutor")
    public CompletableFuture<Long> currentMonthSubscriptions() {

        long start = System.currentTimeMillis();

        Long result =
                subscriptionRepository.countCurrentMonthSubscriptions();

        log.info("currentMonthSubscriptions() : {} ms",
                System.currentTimeMillis() - start);

        return CompletableFuture.completedFuture(result);
    }

    @Async("applicationTaskExecutor")
    public CompletableFuture<Long> previousMonthSubscriptions() {

        long start = System.currentTimeMillis();

        Long result =
                subscriptionRepository.countPreviousMonthSubscriptions();

        log.info("previousMonthSubscriptions() : {} ms",
                System.currentTimeMillis() - start);

        return CompletableFuture.completedFuture(result);
    }

    @Async("applicationTaskExecutor")
    public CompletableFuture<Long> currentMonthUsers() {

        long start = System.currentTimeMillis();

        Long result = userRepository.countCurrentMonthUsers();

        log.info("currentMonthUsers() : {} ms",
                System.currentTimeMillis() - start);

        return CompletableFuture.completedFuture(result);
    }

    @Async("applicationTaskExecutor")
    public CompletableFuture<Long> previousMonthUsers() {

        long start = System.currentTimeMillis();

        Long result = userRepository.countPreviousMonthUsers();

        log.info("previousMonthUsers() : {} ms",
                System.currentTimeMillis() - start);

        return CompletableFuture.completedFuture(result);
    }

    @Async("applicationTaskExecutor")
    public CompletableFuture<java.util.List<Object[]>> monthlyUserRegistrations() {
        return CompletableFuture.completedFuture(userRepository.getMonthlyUserRegistrations());
    }

    @Async("applicationTaskExecutor")
    public CompletableFuture<java.util.List<Object[]>> monthlyRevenue() {
        return CompletableFuture.completedFuture(paymentRepository.getMonthlyRevenue());
    }

    @Async("applicationTaskExecutor")
    public CompletableFuture<java.util.List<Object[]>> monthlyReports() {
        return CompletableFuture.completedFuture(reportRepository.getMonthlyReports());
    }

    @Async("applicationTaskExecutor")
    public CompletableFuture<java.util.List<Object[]>> paymentMethodDistribution() {
        return CompletableFuture.completedFuture(paymentRepository.getPaymentMethodDistribution());
    }

    @Async("applicationTaskExecutor")
    public CompletableFuture<java.util.List<Object[]>> reportStatusDistribution() {
        return CompletableFuture.completedFuture(reportRepository.getReportStatusDistribution());
    }

    @Async("applicationTaskExecutor")
    public CompletableFuture<java.util.List<Object[]>> topPaymentPlans() {
        return CompletableFuture.completedFuture(paymentRepository.getTopPaymentPlans());
    }

    @Async("applicationTaskExecutor")
    public CompletableFuture<java.util.List<Object[]>> topCities() {
        return CompletableFuture.completedFuture(userRepository.getTopCities());
    }

    @Async("applicationTaskExecutor")
    public CompletableFuture<java.util.List<Object[]>> topReligions() {
        return CompletableFuture.completedFuture(userRepository.getTopReligions());
    }

}