package com.example.serviceimpl;

import com.example.dto.response.AdminDashboardDTO;
import com.example.repository.*;
import com.example.service.AdminDashboardAsyncService;
import com.example.service.AdminDashboardService;
import com.example.service.DashboardCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl
        implements AdminDashboardService {

    private final UserRepository userRepository;

    private final ProfileRepository profileRepository;
    private final DashboardCacheService cacheService;
    private final UserSubscriptionRepository userSubscriptionRepository;

    private final PaymentRepository paymentRepository;

    private final UserReportRepository reportRepository;

    private final AdminDashboardAsyncService dashboardAsyncService;
    private final AdminDashboardQueryRepository adminDashboardQueryRepository;
    // =====================================================
    // DASHBOARD OVERVIEW
    // =====================================================


    public AdminDashboardDTO buildDashboard() {

        long start = System.currentTimeMillis();

        log.info("========== DASHBOARD START ==========");
        System.out.println("******** DASHBOARD METHOD CALLED ********");
        // =====================================================
        // USER STATISTICS (ASYNC)
        // =====================================================

        CompletableFuture<Long> totalUsers = dashboardAsyncService.totalUsers();
        CompletableFuture<Long> activeUsers = dashboardAsyncService.activeUsers();
        CompletableFuture<Long> inactiveUsers = dashboardAsyncService.inactiveUsers();
        CompletableFuture<Long> blockedUsers = dashboardAsyncService.blockedUsers();
        CompletableFuture<Long> verifiedUsers = dashboardAsyncService.verifiedUsers();
        CompletableFuture<Long> unverifiedUsers = dashboardAsyncService.unverifiedUsers();

        CompletableFuture<Long> newUsersThisMonth = dashboardAsyncService.newUsersThisMonth();
        CompletableFuture<Long> newUsersThisWeek = dashboardAsyncService.newUsersThisWeek();
        CompletableFuture<Long> newUsersToday = dashboardAsyncService.newUsersToday();

        // =====================================================
        // PAYMENT
        // =====================================================

        CompletableFuture<BigDecimal> totalRevenue = dashboardAsyncService.totalRevenue();
        CompletableFuture<Long> totalTransactions = dashboardAsyncService.totalTransactions();
        CompletableFuture<Long> successfulTransactions = dashboardAsyncService.successfulTransactions();
        CompletableFuture<Long> failedTransactions = dashboardAsyncService.failedTransactions();
        CompletableFuture<Long> pendingTransactions = dashboardAsyncService.pendingTransactions();

        CompletableFuture<BigDecimal> currentMonthRevenue =
                dashboardAsyncService.currentMonthRevenue();

        CompletableFuture<BigDecimal> previousMonthRevenue =
                dashboardAsyncService.previousMonthRevenue();

        // =====================================================
        // REPORTS
        // =====================================================

        CompletableFuture<Long> totalReports = dashboardAsyncService.totalReports();
        CompletableFuture<Long> pendingReports = dashboardAsyncService.pendingReports();
        CompletableFuture<Long> resolvedReports = dashboardAsyncService.resolvedReports();
        CompletableFuture<Long> closedReports = dashboardAsyncService.closedReports();

        // =====================================================
        // SUBSCRIPTIONS
        // =====================================================

        CompletableFuture<Long> totalSubscriptions =
                dashboardAsyncService.totalSubscriptions();

        CompletableFuture<Long> activeSubscriptions =
                dashboardAsyncService.activeSubscriptions();

        CompletableFuture<Long> expiredSubscriptions =
                dashboardAsyncService.expiredSubscriptions();

        CompletableFuture<Long> currentMonthSubscriptions =
                dashboardAsyncService.currentMonthSubscriptions();

        CompletableFuture<Long> previousMonthSubscriptions =
                dashboardAsyncService.previousMonthSubscriptions();

        CompletableFuture<Long> currentMonthUsers =
                dashboardAsyncService.currentMonthUsers();

        CompletableFuture<Long> previousMonthUsers =
                dashboardAsyncService.previousMonthUsers();

        CompletableFuture<java.util.List<Object[]>> monthlyUserRegistrations =
                dashboardAsyncService.monthlyUserRegistrations();

        CompletableFuture<java.util.List<Object[]>> monthlyRevenue =
                dashboardAsyncService.monthlyRevenue();

        CompletableFuture<java.util.List<Object[]>> monthlyReports =
                dashboardAsyncService.monthlyReports();

        CompletableFuture<java.util.List<Object[]>> paymentMethodDistribution =
                dashboardAsyncService.paymentMethodDistribution();

        CompletableFuture<java.util.List<Object[]>> reportStatusDistribution =
                dashboardAsyncService.reportStatusDistribution();

        CompletableFuture<java.util.List<Object[]>> topPaymentPlans =
                dashboardAsyncService.topPaymentPlans();

        CompletableFuture<java.util.List<Object[]>> topCities =
                dashboardAsyncService.topCities();

        CompletableFuture<java.util.List<Object[]>> topReligions =
                dashboardAsyncService.topReligions();

        // =====================================================
        // WAIT FOR ALL 34 ASYNC TASKS PARALLEL
        // =====================================================

        CompletableFuture.allOf(
                totalUsers,
                activeUsers,
                inactiveUsers,
                blockedUsers,
                verifiedUsers,
                unverifiedUsers,
                newUsersThisMonth,
                newUsersThisWeek,
                newUsersToday,
                totalRevenue,
                totalTransactions,
                successfulTransactions,
                failedTransactions,
                pendingTransactions,
                currentMonthRevenue,
                previousMonthRevenue,
                totalReports,
                pendingReports,
                resolvedReports,
                closedReports,
                totalSubscriptions,
                activeSubscriptions,
                expiredSubscriptions,
                currentMonthSubscriptions,
                previousMonthSubscriptions,
                currentMonthUsers,
                previousMonthUsers,
                monthlyUserRegistrations,
                monthlyRevenue,
                monthlyReports,
                paymentMethodDistribution,
                reportStatusDistribution,
                topPaymentPlans,
                topCities,
                topReligions
        ).join();

        log.info("All 34 Async dashboard tasks completed in {} ms",
                System.currentTimeMillis() - start);

        var registrationTrend = toLongMap(monthlyUserRegistrations.join());
        var revenueTrend = toBigDecimalMap(monthlyRevenue.join());
        var reportsTrend = toLongMap(monthlyReports.join());
        var paymentDistribution = toLongMap(paymentMethodDistribution.join());
        var reportDistribution = toLongMap(reportStatusDistribution.join());
        var topPlans = toTopPaymentPlans(topPaymentPlans.join());
        var topCitiesList = toTopCities(topCities.join());
        var topReligionsList = toTopReligions(topReligions.join());

        AdminDashboardDTO dto = AdminDashboardDTO.builder()

                .totalUsers(totalUsers.join())
                .activeUsers(activeUsers.join())
                .inactiveUsers(inactiveUsers.join())
                .blockedUsers(blockedUsers.join())
                .verifiedUsers(verifiedUsers.join())
                .unverifiedUsers(unverifiedUsers.join())

                .newUsersThisMonth(newUsersThisMonth.join())
                .newUsersThisWeek(newUsersThisWeek.join())
                .newUsersToday(newUsersToday.join())

                .totalRevenue(totalRevenue.join())
                .revenueThisMonth(currentMonthRevenue.join())

                .totalTransactions(totalTransactions.join())
                .successfulTransactions(successfulTransactions.join())
                .failedTransactions(failedTransactions.join())
                .pendingTransactions(pendingTransactions.join())

                .totalReports(totalReports.join())
                .pendingReports(pendingReports.join())
                .resolvedReports(resolvedReports.join())
                .closedReports(closedReports.join())

                .totalSubscriptions(totalSubscriptions.join())
                .activeSubscriptions(activeSubscriptions.join())
                .expiredSubscriptions(expiredSubscriptions.join())

                .userRegistrationTrend(registrationTrend)
                .revenueTrend(revenueTrend)
                .reportsTrend(reportsTrend)
                .paymentMethodDistribution(paymentDistribution)
                .reportTypeDistribution(reportDistribution)

                .topPaymentPlans(topPlans)
                .topCities(topCitiesList)
                .topReligions(topReligionsList)

                .userGrowthPercentage(
                        calculateGrowthPercentage(
                                currentMonthUsers.join(),
                                previousMonthUsers.join()
                        )
                )

                .revenueGrowthPercentage(
                        calculateGrowthPercentage(
                                currentMonthRevenue.join(),
                                previousMonthRevenue.join()
                        )
                )

                .subscriptionGrowthPercentage(
                        calculateGrowthPercentage(
                                currentMonthSubscriptions.join(),
                                previousMonthSubscriptions.join()
                        )
                )

                .build();

        log.info("========== DASHBOARD TOTAL : {} ms ==========",
                System.currentTimeMillis() - start);

        return dto;
    }    // =====================================================
    // MAP<Long>
    // =====================================================

    private Map<String, Long> toLongMap(List<Object[]> results) {

        Map<String, Long> map = new LinkedHashMap<>();

        for (Object[] row : results) {

            map.put(
                    String.valueOf(row[0]),
                    ((Number) row[1]).longValue()
            );
        }

        return map;
    }

    // =====================================================
    // MAP<BigDecimal>
    // =====================================================

    private Map<String, BigDecimal> toBigDecimalMap(
            List<Object[]> results
    ) {

        Map<String, BigDecimal> map = new LinkedHashMap<>();

        for (Object[] row : results) {

            map.put(
                    String.valueOf(row[0]),
                    row[1] != null
                            ? (BigDecimal) row[1]
                            : BigDecimal.ZERO
            );
        }

        return map;
    }

    // =====================================================
    // TOP PAYMENT PLANS
    // =====================================================

    private List<AdminDashboardDTO.TopPaymentPlanDTO> toTopPaymentPlans(
            List<Object[]> results
    ) {

        List<AdminDashboardDTO.TopPaymentPlanDTO> list =
                new ArrayList<>();

        for (Object[] row : results) {

            list.add(

                    AdminDashboardDTO.TopPaymentPlanDTO.builder()
                            .planId(((Number) row[0]).longValue())
                            .planName((String) row[1])
                            .subscriptionCount(((Number) row[2]).longValue())
                            .totalRevenue(
                                    row[3] != null
                                            ? (BigDecimal) row[3]
                                            : BigDecimal.ZERO
                            )
                            .build()

            );
        }

        return list;
    }

    // =====================================================
    // TOP CITIES
    // =====================================================

    private List<AdminDashboardDTO.TopCityDTO> toTopCities(
            List<Object[]> results
    ) {

        List<AdminDashboardDTO.TopCityDTO> list =
                new ArrayList<>();

        for (Object[] row : results) {

            list.add(

                    AdminDashboardDTO.TopCityDTO.builder()
                            .cityId(((Number) row[0]).longValue())
                            .cityName((String) row[1])
                            .userCount(((Number) row[2]).longValue())
                            .build()

            );
        }

        return list;
    }

    // =====================================================
    // TOP RELIGIONS
    // =====================================================

    private List<AdminDashboardDTO.TopReligionDTO> toTopReligions(
            List<Object[]> results
    ) {

        List<AdminDashboardDTO.TopReligionDTO> list =
                new ArrayList<>();

        for (Object[] row : results) {

            list.add(

                    AdminDashboardDTO.TopReligionDTO.builder()
                            .religionId(((Number) row[0]).longValue())
                            .religionName((String) row[1])
                            .userCount(((Number) row[2]).longValue())
                            .build()

            );
        }

        return list;
    }

    // =====================================================
    // GROWTH CALCULATION
    // =====================================================

    private Double calculateGrowthPercentage(
            Number current,
            Number previous
    ) {

        double currentValue =
                current != null
                        ? current.doubleValue()
                        : 0.0;

        double previousValue =
                previous != null
                        ? previous.doubleValue()
                        : 0.0;

        if (previousValue == 0) {
            return currentValue > 0 ? 100.0 : 0.0;
        }

        return ((currentValue - previousValue)
                / previousValue) * 100.0;
    }
    @Override
    public AdminDashboardDTO getDashboardOverview() {

        AdminDashboardDTO dashboard = cacheService.getDashboard();

        if (dashboard != null) {
            return dashboard;
        }

        dashboard = buildDashboard();

        cacheService.saveDashboard(dashboard);

        return dashboard;
    }
}