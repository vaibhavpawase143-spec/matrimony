package com.example.serviceimpl;

import com.example.dto.request.UserSubscriptionRequestDTO;
import com.example.dto.response.SubscriptionResponseDto;
import com.example.model.SubscriptionPlan;
import com.example.model.User;
import com.example.model.UserSubscription;
import com.example.repository.SubscriptionPlanRepository;
import com.example.repository.UserRepository;
import com.example.repository.UserSubscriptionRepository;
import com.example.service.SubscriptionService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final UserSubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final SubscriptionPlanRepository planRepository;

    public SubscriptionServiceImpl(UserSubscriptionRepository subscriptionRepository,
                                   UserRepository userRepository,
                                   SubscriptionPlanRepository planRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.planRepository = planRepository;
    }

    // ✅ MAIN LOGIC
    @Override
    public SubscriptionResponseDto subscribeUser(UserSubscriptionRequestDTO requestDto) {

        User user = getCurrentUser();

        SubscriptionPlan plan = planRepository.findById(requestDto.getPlanId())
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        // 🔥 deactivate old subscription
        subscriptionRepository.findByUserIdAndIsActiveTrue(user.getId())
                .ifPresent(old -> {
                    old.setIsActive(false);
                    old.setStatus("CANCELLED");
                    subscriptionRepository.save(old);
                });

        // ✅ create new subscription
        UserSubscription sub = new UserSubscription();
        sub.setUser(user);
        sub.setSubscriptionPlan(plan);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(plan.getDuration()); // 🔥 duration logic

        sub.setStartDate(start);
        sub.setEndDate(end);
        sub.setIsActive(true);
        sub.setStatus("ACTIVE");

        UserSubscription saved = subscriptionRepository.save(sub);

        // ✅ response DTO
        SubscriptionResponseDto response = new SubscriptionResponseDto();
        response.setSubscriptionId(saved.getId());
        response.setUserId(user.getId());
        response.setPlanName(plan.getName());
        response.setStartDate(start);
        response.setEndDate(end);
        response.setStatus(saved.getStatus());

        return response;
    }

    @Override
    @Transactional
    public UserSubscription activateSubscription(User user, SubscriptionPlan plan) {

        // Deactivate existing active subscription
        subscriptionRepository.findByUserIdAndIsActiveTrue(user.getId())
                .ifPresent(old -> {
                    old.setIsActive(false);
                    old.setStatus("EXPIRED");
                    subscriptionRepository.save(old);
                });

        // Create new subscription
        UserSubscription subscription = new UserSubscription();

        subscription.setUser(user);
        subscription.setSubscriptionPlan(plan);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(plan.getDuration());

        subscription.setStartDate(start);
        subscription.setEndDate(end);

        subscription.setIsActive(true);
        subscription.setStatus("ACTIVE");

        return subscriptionRepository.save(subscription);
    }

    @Override
    public UserSubscription create(UserSubscription subscription) {

        Long userId = subscription.getUser().getId();

        Optional<UserSubscription> existing =
                subscriptionRepository.findByUserIdAndIsActiveTrue(userId);

        if (existing.isPresent()) {

            UserSubscription old = existing.get();

            old.setIsActive(false);
            old.setStatus("CANCELLED");

            subscriptionRepository.save(old);
        }

        subscription.setIsActive(true);

        if (subscription.getStatus() == null) {
            subscription.setStatus("ACTIVE");
        }

        return subscriptionRepository.save(subscription);
    }

    @Override
    public Optional<UserSubscription> getById(Long id) {

        return subscriptionRepository.findById(id);
    }

    @Override
    public Optional<UserSubscription> getActiveByUser(Long userId) {

        return subscriptionRepository.findByUserIdAndIsActiveTrue(userId);
    }

    @Override
    public boolean hasActiveSubscription(Long userId) {

        return subscriptionRepository.existsByUserIdAndIsActiveTrue(userId);
    }
    @Override
    public List<UserSubscription> getByUser(Long userId) {

        return subscriptionRepository.findByUserId(userId);
    }

    @Override
    public List<UserSubscription> getInactiveByUser(Long userId) {

        return subscriptionRepository.findByUserIdAndIsActiveFalse(userId);
    }

    @Override
    public List<UserSubscription> getByPlan(Long planId) {

        return subscriptionRepository.findBySubscriptionPlanId(planId);
    }

    @Override
    public List<UserSubscription> getActiveByPlan(Long planId) {

        return subscriptionRepository.findBySubscriptionPlanIdAndIsActiveTrue(planId);
    }
    @Override
    public List<UserSubscription> getAllInactive() {

        return subscriptionRepository.findByIsActiveFalse();
    }
    @Override
    public void deactivate(Long id) {

        UserSubscription subscription =
                subscriptionRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Subscription not found"));

        subscription.setIsActive(false);
        subscription.setStatus("CANCELLED");

        subscriptionRepository.save(subscription);
    }
    @Override
    public List<UserSubscription> getMySubscriptionHistory() {

        User currentUser = getCurrentUser();

        return subscriptionRepository.findByUserId(currentUser.getId());

    }
    @Override
    public boolean isCurrentUserPremium() {

        User currentUser = getCurrentUser();

        return subscriptionRepository
                .findByUserIdAndIsActiveTrue(currentUser.getId())
                .isPresent();
    }


    @Override
    public List<UserSubscription> getAll() {

        return subscriptionRepository.findAll();
    }
    @Override
    public UserSubscription getMySubscription() {

        User currentUser = getCurrentUser();

        return subscriptionRepository
                .findByUserIdAndIsActiveTrue(currentUser.getId())
                .orElseThrow(() ->
                        new RuntimeException("No active subscription found"));
    }


    // (other methods can stay same or empty for now)
    private User getCurrentUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }
    @Override
    public Optional<UserSubscription> getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id);
    }
}