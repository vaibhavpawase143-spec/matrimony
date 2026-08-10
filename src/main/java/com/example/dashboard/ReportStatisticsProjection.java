package com.example.dashboard;

public interface ReportStatisticsProjection {

    Long getTotalReports();

    Long getPendingReports();

    Long getResolvedReports();

    Long getClosedReports();

}