package com.example.dashboard;

public interface SubscriptionStatisticsProjection {

    Long getTotalSubscriptions();

    Long getActiveSubscriptions();

    Long getExpiredSubscriptions();

    Long getCurrentMonthSubscriptions();

    Long getPreviousMonthSubscriptions();

}