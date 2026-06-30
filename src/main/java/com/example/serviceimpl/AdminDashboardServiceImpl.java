package com.example.serviceimpl;

import com.example.dto.response.AdminDashboardDTO;
import com.example.repository.*;
import com.example.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl
        implements AdminDashboardService {

    private final UserRepository userRepository;

    private final ProfileRepository profileRepository;

    private final UserSubscriptionRepository userSubscriptionRepository;

    private final PaymentRepository paymentRepository;

    private final UserReportRepository reportRepository;

    @Override
    public AdminDashboardDTO getDashboardOverview() {

        return AdminDashboardDTO.builder()

                // ================= USER =================

                .totalUsers(
                        userRepository.countByIsDeletedFalse()
                )

                .activeUsers(
                        userRepository.countByIsActiveTrueAndIsDeletedFalse()
                )

                .inactiveUsers(
                        userRepository.countByIsActiveFalseAndIsDeletedFalse()
                )

                .blockedUsers(
                        userRepository.countByIsBlockedTrueAndIsDeletedFalse()
                )

                .verifiedUsers(
                        userRepository.countByEmailVerifiedTrueAndPhoneVerifiedTrueAndIsDeletedFalse()
                )

                .unverifiedUsers(
                        userRepository.countByEmailVerifiedFalseAndIsDeletedFalse()
                )

                // ================= NEW USERS =================

                .newUsersThisMonth(
                        userRepository.findNewUsersCount(
                                java.time.LocalDateTime.now().minusMonths(1)
                        )
                )

                .newUsersThisWeek(
                        userRepository.findNewUsersCount(
                                java.time.LocalDateTime.now().minusWeeks(1)
                        )
                )

                .newUsersToday(
                        userRepository.findUsersCountByDate(
                                java.time.LocalDateTime.now()
                        )
                )

                // ================= PAYMENT =================

                .totalRevenue(
                        paymentRepository.getTotalRevenue()
                )

                .totalTransactions(
                        paymentRepository.count()
                )

                .successfulTransactions(
                        paymentRepository.countByStatus("SUCCESS")
                )

                .failedTransactions(
                        paymentRepository.countByStatus("FAILED")
                )

                .pendingTransactions(
                        paymentRepository.countByStatus("PENDING")
                )

                // ================= REPORT =================

                .totalReports(
                        reportRepository.count()
                )

                .pendingReports(
                        reportRepository.countByStatus(
                                com.example.model.ReportStatus.PENDING
                        )
                )

                .resolvedReports(
                        reportRepository.countByStatus(
                                com.example.model.ReportStatus.APPROVED
                        )
                )

                .closedReports(
                        reportRepository.countByStatus(
                                com.example.model.ReportStatus.REJECTED
                        )
                )

                // ================= SUBSCRIPTION =================

                .totalSubscriptions(
                        userSubscriptionRepository.count()
                )

                .activeSubscriptions(
                        userSubscriptionRepository.countByIsActiveTrue()
                )

                .expiredSubscriptions(
                        userSubscriptionRepository.countByStatus("EXPIRED")
                )
// ================= CHARTS =================

                .userRegistrationTrend(
                        toLongMap(
                                userRepository.getMonthlyUserRegistrations()
                        )
                )

                .revenueTrend(
                        toBigDecimalMap(
                                paymentRepository.getMonthlyRevenue()
                        )
                )

                .reportsTrend(
                        toLongMap(
                                reportRepository.getMonthlyReports()
                        )
                )

                .paymentMethodDistribution(
                        toLongMap(
                                paymentRepository.getPaymentMethodDistribution()
                        )
                )

                .reportTypeDistribution(
                        toLongMap(
                                reportRepository.getReportStatusDistribution()
                        )
                )
// ================= TOP ANALYTICS =================

                .topPaymentPlans(
                        toTopPaymentPlans(
                                paymentRepository.getTopPaymentPlans()
                        )
                )

                .topCities(
                        toTopCities(
                                userRepository.getTopCities()
                        )
                )

                .topReligions(
                        toTopReligions(
                                userRepository.getTopReligions()
                        )
                )
// ================= GROWTH =================

                .userGrowthPercentage(
                        calculateGrowthPercentage(
                                userRepository.countCurrentMonthUsers(),
                                userRepository.countPreviousMonthUsers()
                        )
                )

                .revenueGrowthPercentage(
                        calculateGrowthPercentage(
                                paymentRepository.getCurrentMonthRevenue(),
                                paymentRepository.getPreviousMonthRevenue()
                        )
                )

                .subscriptionGrowthPercentage(
                        calculateGrowthPercentage(
                                userSubscriptionRepository.countCurrentMonthSubscriptions(),
                                userSubscriptionRepository.countPreviousMonthSubscriptions()
                        )
                )
                .build();
    }
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

    private Map<String, BigDecimal> toBigDecimalMap(List<Object[]> results) {

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

    private List<AdminDashboardDTO.TopPaymentPlanDTO> toTopPaymentPlans(
            List<Object[]> results
    ) {

        List<AdminDashboardDTO.TopPaymentPlanDTO> list = new ArrayList<>();

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
    private List<AdminDashboardDTO.TopCityDTO> toTopCities(
            List<Object[]> results
    ) {

        List<AdminDashboardDTO.TopCityDTO> list = new ArrayList<>();

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
    private List<AdminDashboardDTO.TopReligionDTO> toTopReligions(
            List<Object[]> results
    ) {

        List<AdminDashboardDTO.TopReligionDTO> list = new ArrayList<>();

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
    private Double calculateGrowthPercentage(
            Number current,
            Number previous
    ) {

        double currentValue = current != null
                ? current.doubleValue()
                : 0.0;

        double previousValue = previous != null
                ? previous.doubleValue()
                : 0.0;

        // No previous data
        if (previousValue == 0) {

            return currentValue > 0 ? 100.0 : 0.0;

        }

        return ((currentValue - previousValue)
                / previousValue) * 100.0;
    }
}