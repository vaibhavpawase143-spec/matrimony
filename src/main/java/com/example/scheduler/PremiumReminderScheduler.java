package com.example.scheduler;

import com.example.model.UserSubscription;
import com.example.repository.NotificationRepository;
import com.example.repository.UserSubscriptionRepository;
import com.example.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class PremiumReminderScheduler {

    private final UserSubscriptionRepository subscriptionRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    @Value("${subscription.reminder.days}")
    private String reminderDaysConfig;

    /**
     * Runs every day according to application.properties
     */
    @Scheduled(cron = "${subscription.reminder.cron}")
    public void sendPremiumExpiryReminders() {

        log.info("========== Premium Reminder Scheduler Started ==========");

        Set<Long> reminderDays = Arrays.stream(reminderDaysConfig.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .collect(Collectors.toSet());

        List<UserSubscription> subscriptions =
                subscriptionRepository.findAllActiveSubscriptions();

        if (subscriptions.isEmpty()) {
            log.info("No active subscriptions found.");
            return;
        }

        int reminderCount = 0;

        for (UserSubscription subscription : subscriptions) {

            long daysRemaining = ChronoUnit.DAYS.between(
                    LocalDate.now(),
                    subscription.getEndDate().toLocalDate()
            );

            if (!reminderDays.contains(daysRemaining)) {
                continue;
            }

            String title = "PREMIUM_REMINDER_" + daysRemaining;

            String message =
                    "Your Premium Membership will expire in "
                            + daysRemaining
                            + " day(s). Renew now to continue enjoying premium benefits.";

            if (daysRemaining == 1) {
                message =
                        "Your Premium Membership will expire tomorrow. Renew now to continue enjoying premium benefits.";
            }

            boolean alreadySent =
                    notificationRepository.existsBySubscriptionIdAndTitleAndDeletedFalse(
                            subscription.getId(),
                            title
                    );

            if (alreadySent) {
                continue;
            }

            notificationService.createSubscriptionReminder(
                    subscription.getUser().getId(),
                    subscription.getId(),
                    title,
                    message
            );

            reminderCount++;
        }

        log.info("Premium Reminder Scheduler completed successfully.");
        log.info("Total reminders sent: {}", reminderCount);
        log.info("========================================================");
    }
}