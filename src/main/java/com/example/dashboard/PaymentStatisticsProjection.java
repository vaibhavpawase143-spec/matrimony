package com.example.dashboard;

import java.math.BigDecimal;

public interface PaymentStatisticsProjection {

    BigDecimal getTotalRevenue();

    Long getTotalTransactions();

    Long getSuccessfulTransactions();

    Long getFailedTransactions();

    Long getPendingTransactions();

    BigDecimal getCurrentMonthRevenue();

    BigDecimal getPreviousMonthRevenue();

}