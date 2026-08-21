package com.example.controller.admin;

import com.example.repository.WebsiteAnalyticsRepository;
import com.example.model.WebsiteAnalytics;
import com.example.service.DashboardCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class WebsiteAnalyticsController {

    private final WebsiteAnalyticsRepository websiteAnalyticsRepository;
    private final DashboardCacheService dashboardCacheService;

    @PostMapping("/visitor")
    @Transactional
    public ResponseEntity<Void> recordVisitor() {

        int updated = websiteAnalyticsRepository.incrementProfileHits();

        // Safety: create counter row if it doesn't exist
        if (updated == 0) {
            websiteAnalyticsRepository.save(
                    WebsiteAnalytics.builder()
                            .id(1L)
                            .profileHits(1L)
                            .build()
            );
        }

        // Dashboard cache contains old profileHits
        dashboardCacheService.clearDashboard();

        return ResponseEntity.ok().build();
    }
}