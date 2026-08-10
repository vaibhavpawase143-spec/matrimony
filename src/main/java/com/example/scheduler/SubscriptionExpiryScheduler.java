package com.example.scheduler;

import com.example.service.SubscriptionExpiryWorkflowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionExpiryScheduler {

    private final SubscriptionExpiryWorkflowService expiryWorkflowService;

    @Value("${subscription.expiry.scheduler.enabled:true}")
    private boolean schedulerEnabled;

    /**
     * Daily Scheduler for Subscription Expiry Workflow.
     * Default: 9:00 AM every day using application configured timezone.
     */
    @Scheduled(cron = "${subscription.expiry.scheduler.cron:0 0 9 * * *}", zone = "${subscription.expiry.scheduler.zone:Asia/Kolkata}")
    public void executeSubscriptionExpiryWorkflow() {
        if (!schedulerEnabled) {
            log.info("Subscription Expiry Scheduler is disabled in configuration.");
            return;
        }

        log.info("========== Subscription Expiry Scheduler Started ==========");

        try {
            expiryWorkflowService.processAndPublishExpiringSubscriptions();
        } catch (Exception e) {
            log.error("Error executing Subscription Expiry Scheduler: {}", e.getMessage(), e);
        }

        log.info("========== Subscription Expiry Scheduler Completed ==========");
    }
}