package com.example.serviceimpl;

import com.example.model.UserSubscription;
import com.example.repository.SubscriptionRepository;
import com.example.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepository repo;

    // ✅ Subscribe user
    @Override
    public UserSubscription subscribeUser(Long userId, Long planId) {

        // deactivate old subscription (if exists)
        Optional<UserSubscription> existing = repo.findByUserIdAndIsActiveTrue(userId);
        existing.ifPresent(sub -> {
            sub.setIsActive(false);
            sub.setEndDate(LocalDateTime.now());
            repo.save(sub);
        });

        // create new subscription
        UserSubscription subscription = new UserSubscription();
        subscription.setUserId(userId);
        subscription.setPlanId(planId);
        subscription.setStartDate(LocalDateTime.now());
        subscription.setIsActive(true);

        // NOTE: duration logic can be added here (based on plan)

        return repo.save(subscription);
    }

    // ✅ Get active subscription
    @Override
    public UserSubscription getActiveSubscription(Long userId) {
        return repo.findByUserIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new RuntimeException("No active subscription for userId: " + userId));
    }

    // ✅ Check if active
    @Override
    public boolean isSubscriptionActive(Long userId) {
        return repo.existsByUserIdAndIsActiveTrue(userId);
    }
}