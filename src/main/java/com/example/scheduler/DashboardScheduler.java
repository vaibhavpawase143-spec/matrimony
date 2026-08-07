package com.example.scheduler;

import com.example.dto.response.AdminDashboardDTO;
import com.example.service.DashboardCacheService;
import com.example.serviceimpl.AdminDashboardServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DashboardScheduler {

    private final AdminDashboardServiceImpl dashboardService;
    private final DashboardCacheService cacheService;

    /**
     * Runs once after application startup (10 seconds later)
     * to warm up the dashboard cache.
     */
    @Scheduled(initialDelay = 10000, fixedDelay = Long.MAX_VALUE)
    public void initializeCache() {

        try {

            log.info("Initializing Dashboard Cache...");

            AdminDashboardDTO dto = dashboardService.buildDashboard();

            cacheService.saveDashboard(dto);

            log.info("Dashboard Cache Initialized");

        } catch (Exception e) {

            log.error("Failed to initialize dashboard cache", e);

        }
    }

    /**
     * Refresh dashboard cache every 5 minutes.
     */
    @Scheduled(fixedRate = 300000)
    public void refreshDashboard() {

        try {

            log.info("Refreshing Dashboard Cache...");

            AdminDashboardDTO dto = dashboardService.buildDashboard();

            cacheService.saveDashboard(dto);

            log.info("Dashboard Cache Updated");

        } catch (Exception e) {

            log.error("Failed to refresh dashboard cache", e);

        }
    }
}