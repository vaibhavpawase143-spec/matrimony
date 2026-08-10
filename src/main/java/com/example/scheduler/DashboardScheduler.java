package com.example.scheduler;

import com.example.dto.response.AdminDashboardDTO;
import com.example.service.AdminDashboardService;
import com.example.service.DashboardCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DashboardScheduler {

    private final AdminDashboardService dashboardService;
    private final DashboardCacheService cacheService;

    /**
     * Pre-warm admin dashboard cache asynchronously on startup.
     */
    @Async("applicationTaskExecutor")
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("Pre-warming Admin Dashboard cache on application startup...");
        try {
            AdminDashboardDTO dto = dashboardService.getDashboardOverview();
            if (dto != null) {
                cacheService.saveDashboard(dto);
            }
            log.info("Admin Dashboard cache pre-warmed successfully!");
        } catch (Exception e) {
            log.warn("Failed to pre-warm dashboard cache on startup: {}", e.getMessage());
        }
    }

    /**
     * Refresh dashboard cache every 5 minutes (starts after 5 min initial delay).
     */
    @Scheduled(initialDelay = 300000, fixedRate = 300000)
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