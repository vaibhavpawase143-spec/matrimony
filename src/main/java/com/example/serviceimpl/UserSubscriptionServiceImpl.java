package com.example.serviceimpl;

import com.example.model.UserSubscription;
import com.example.repository.UserSubscriptionRepository;
import com.example.service.UserSubscriptionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserSubscriptionServiceImpl implements UserSubscriptionService {

    private final UserSubscriptionRepository repository;

    public UserSubscriptionServiceImpl(UserSubscriptionRepository repository) {
        this.repository = repository;
    }

    // ✅ Create (only one active subscription per user)
    @Override
    public UserSubscription create(UserSubscription subscription) {

        Long userId = subscription.getUser().getId();

        // Check existing active subscription
        Optional<UserSubscription> existing =
                repository.findByUserIdAndIsActiveTrue(userId);

        // Deactivate old subscription
        if (existing.isPresent()) {
            UserSubscription old = existing.get();
            old.setIsActive(false);
            repository.save(old);
        }

        // Activate new subscription
        subscription.setIsActive(true);

        return repository.save(subscription);
    }

    // 🔍 Get by ID
    @Override
    public Optional<UserSubscription> getById(Long id) {
        return repository.findById(id);
    }

    // 🔍 Active subscription
    @Override
    public Optional<UserSubscription> getActiveByUser(Long userId) {
        return repository.findByUserIdAndIsActiveTrue(userId);
    }

    // 🔍 Check active
    @Override
    public boolean hasActiveSubscription(Long userId) {
        return repository.existsByUserIdAndIsActiveTrue(userId);
    }

    // 🔍 All history
    @Override
    public List<UserSubscription> getByUser(Long userId) {
        return repository.findByUserId(userId);
    }

    // 🔍 Inactive history
    @Override
    public List<UserSubscription> getInactiveByUser(Long userId) {
        return repository.findByUserIdAndIsActiveFalse(userId);
    }

    // 🔍 By plan
    @Override
    public List<UserSubscription> getByPlan(Long planId) {
        return repository.findBySubscriptionPlanId(planId);
    }

    // 🔍 Active by plan
    @Override
    public List<UserSubscription> getActiveByPlan(Long planId) {
        return repository.findBySubscriptionPlanIdAndIsActiveTrue(planId);
    }

    // 🔍 All inactive
    @Override
    public List<UserSubscription> getAllInactive() {
        return repository.findByIsActiveFalse();
    }

    // ❌ Deactivate
    @Override
    public void deactivate(Long id) {

        UserSubscription subscription = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found!"));

        subscription.setIsActive(false);
        repository.save(subscription);
    }

    // 🔍 Get all
    @Override
    public List<UserSubscription> getAll() {
        return repository.findAll();
    }
}