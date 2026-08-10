package com.example.dashboard;

public interface UserStatisticsProjection {

    Long getTotalUsers();

    Long getActiveUsers();

    Long getInactiveUsers();

    Long getBlockedUsers();

    Long getVerifiedUsers();

    Long getUnverifiedUsers();

    Long getNewUsersThisMonth();

    Long getNewUsersThisWeek();

    Long getNewUsersToday();

}