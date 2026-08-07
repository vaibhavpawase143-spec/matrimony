package com.example.repository;

import com.example.dashboard.PaymentStatisticsProjection;
import com.example.dashboard.ReportStatisticsProjection;
import com.example.dashboard.SubscriptionStatisticsProjection;
import com.example.dashboard.UserStatisticsProjection;

import java.util.List;

public interface AdminDashboardRepository {

    UserStatisticsProjection getUserStatistics();

    PaymentStatisticsProjection getPaymentStatistics();

    ReportStatisticsProjection getReportStatistics();

    SubscriptionStatisticsProjection getSubscriptionStatistics();

    List<Object[]> getMonthlyUserRegistrations();

    List<Object[]> getMonthlyRevenue();

    List<Object[]> getMonthlyReports();

    List<Object[]> getPaymentMethodDistribution();

    List<Object[]> getReportStatusDistribution();

    List<Object[]> getTopPaymentPlans();

    List<Object[]> getTopCities();

    List<Object[]> getTopReligions();

}