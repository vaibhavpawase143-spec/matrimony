package com.example.scheduler;

import com.example.model.UserSubscription;
import com.example.repository.NotificationRepository;
import com.example.repository.UserSubscriptionRepository;
import com.example.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PremiumReminderScheduler {

    private final UserSubscriptionRepository subscriptionRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    /**
     * Runs every day at 9:00 AM.
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void sendPremiumExpiryReminders() {

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

            String title = null;
            String message = null;

            switch ((int) daysRemaining) {

                case 7:
                    title = "PREMIUM_REMINDER_7";
                    message = "Your Premium Membership will expire in 7 days. Renew now to continue enjoying premium benefits.";
                    break;

                case 5:
                    title = "PREMIUM_REMINDER_5";
                    message = "Your Premium Membership will expire in 5 days. Renew now to continue enjoying premium benefits.";
                    break;

                case 3:
                    title = "PREMIUM_REMINDER_3";
                    message = "Your Premium Membership will expire in 3 days. Renew now to continue enjoying premium benefits.";
                    break;

                case 2:
                    title = "PREMIUM_REMINDER_2";
                    message = "Your Premium Membership will expire in 2 days. Renew now to continue enjoying premium benefits.";
                    break;

                case 1:
                    title = "PREMIUM_REMINDER_1";
                    message = "Your Premium Membership will expire tomorrow. Renew now to continue enjoying premium benefits.";
                    break;



                default:
                    continue;
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

        log.info("PremiumReminderScheduler completed. Sent {} reminder(s).", reminderCount);
    }
}