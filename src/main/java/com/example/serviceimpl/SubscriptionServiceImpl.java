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

import org.springframework.stereotype.Service;

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

        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

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
    public UserSubscription create(UserSubscription subscription) {
        return null;
    }

    @Override
    public Optional<UserSubscription> getById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<UserSubscription> getActiveByUser(Long userId) {
        return Optional.empty();
    }

    @Override
    public boolean hasActiveSubscription(Long userId) {
        return false;
    }

    @Override
    public List<UserSubscription> getByUser(Long userId) {
        return List.of();
    }

    @Override
    public List<UserSubscription> getByPlan(Long planId) {
        return List.of();
    }

    @Override
    public List<UserSubscription> getActiveByPlan(Long planId) {
        return List.of();
    }

    @Override
    public void deactivate(Long id) {

    }

    @Override
    public List<UserSubscription> getAll() {
        return List.of();
    }

    // (other methods can stay same or empty for now)
}