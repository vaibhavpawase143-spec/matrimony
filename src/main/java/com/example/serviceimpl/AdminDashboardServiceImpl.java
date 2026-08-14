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

        // 1. Fetch all scalar statistics in a single consolidated SQL query (1 DB connection checkout)
        AdminDashboardDTO dto = adminDashboardQueryRepository.getDashboardStatistics();

        // 2. Fetch chart and list datasets concurrently (8 async tasks instead of 35)
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
        // WAIT FOR 8 ASYNC CHART TASKS
        // =====================================================

        CompletableFuture.allOf(
                monthlyUserRegistrations,
                monthlyRevenue,
                monthlyReports,
                paymentMethodDistribution,
                reportStatusDistribution,
                topPaymentPlans,
                topCities,
                topReligions
        ).join();

        log.info("All 8 Async chart dashboard tasks completed in {} ms",
                System.currentTimeMillis() - start);

        dto.setUserRegistrationTrend(toLongMap(monthlyUserRegistrations.join()));
        dto.setRevenueTrend(toBigDecimalMap(monthlyRevenue.join()));
        dto.setReportsTrend(toLongMap(monthlyReports.join()));
        dto.setPaymentMethodDistribution(toLongMap(paymentMethodDistribution.join()));
        dto.setReportTypeDistribution(toLongMap(reportStatusDistribution.join()));
        dto.setTopPaymentPlans(toTopPaymentPlans(topPaymentPlans.join()));
        dto.setTopCities(toTopCities(topCities.join()));
        dto.setTopReligions(toTopReligions(topReligions.join()));

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