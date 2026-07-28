package com.example.scheduler;

import com.example.model.UserSubscription;
import com.example.repository.UserSubscriptionRepository;
import com.example.service.NotificationService;
import com.example.service.ProfilePremiumSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionExpiryScheduler {

    private final UserSubscriptionRepository userSubscriptionRepository;
    private final ProfilePremiumSyncService profilePremiumSyncService;
    private final NotificationService notificationService;
    /**
     * Runs every hour.
     * Expires subscriptions whose end date has passed.
     */
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void expireSubscriptions() {

        List<UserSubscription> expiredSubscriptions =
                userSubscriptionRepository.findByIsActiveTrueAndEndDateBefore(
                        LocalDateTime.now()
                );

        if (expiredSubscriptions.isEmpty()) {
            return;
        }

        int expiredCount = 0;

        for (UserSubscription subscription : expiredSubscriptions) {

            subscription.setIsActive(false);
            subscription.setStatus("EXPIRED");

            userSubscriptionRepository.save(subscription);

            profilePremiumSyncService.sync(
                    subscription.getUser(),
                    subscription
            );
            notificationService.createSubscriptionExpiredNotification(
                    subscription.getUser().getId(),
                    subscription.getId()
            );
            expiredCount++;
        }

        log.info(
                "SubscriptionExpiryScheduler completed. Expired {} subscription(s).",
                expiredCount
        );
    }
}