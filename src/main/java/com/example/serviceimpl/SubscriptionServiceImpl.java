package com.example.serviceimpl;

import com.example.dto.request.UserSubscriptionRequestDTO;
import com.example.dto.response.SubscriptionResponseDto;
import com.example.model.NotificationType;
import com.example.model.SubscriptionPlan;
import com.example.model.User;
import com.example.model.UserSubscription;
import com.example.repository.SubscriptionPlanRepository;
import com.example.repository.UserRepository;
import com.example.repository.UserSubscriptionRepository;
import com.example.service.NotificationService;
import com.example.service.ProfilePremiumSyncService;
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
    private final ProfilePremiumSyncService profilePremiumSyncService;
    private final NotificationService notificationService;
    public SubscriptionServiceImpl(
            UserSubscriptionRepository subscriptionRepository,
            UserRepository userRepository,
            SubscriptionPlanRepository planRepository,
            ProfilePremiumSyncService profilePremiumSyncService,
            NotificationService notificationService) {

        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.planRepository = planRepository;
        this.profilePremiumSyncService = profilePremiumSyncService;
        this.notificationService = notificationService;
    }

    // =====================================================
    // SUBSCRIBE USER
    // =====================================================

    @Override
    @Transactional
    public SubscriptionResponseDto subscribeUser(UserSubscriptionRequestDTO requestDto) {

        User user = getCurrentUser();

        SubscriptionPlan plan = planRepository.findById(requestDto.getPlanId())
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        // Deactivate old subscription
        subscriptionRepository.findByUserIdAndIsActiveTrue(user.getId())
                .ifPresent(old -> {
                    old.setIsActive(false);
                    old.setStatus("CANCELLED");
                    subscriptionRepository.save(old);

                    profilePremiumSyncService.sync(user, null);
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

        UserSubscription saved = subscriptionRepository.save(subscription);
        notificationService.createAdminNotification(
                "Premium Subscription Purchased",
                user.getFullName()
                        + " purchased the "
                        + plan.getName()
                        + " subscription plan.",
                NotificationType.SUBSCRIPTION
        );
        notificationService.createSubscriptionReminder(
                user.getId(),
                saved.getId(),
                "Premium Activated",
                "Your " + plan.getName() + " subscription is now active."
        );
        // Synchronize Profile
        profilePremiumSyncService.sync(user, saved);

        SubscriptionResponseDto response = new SubscriptionResponseDto();

        response.setSubscriptionId(saved.getId());
        response.setUserId(user.getId());
        response.setPlanName(plan.getName());
        response.setStartDate(start);
        response.setEndDate(end);
        response.setStatus(saved.getStatus());

        return response;
    }

    // =====================================================
    // ACTIVATE SUBSCRIPTION
    // =====================================================

    @Override
    @Transactional
    public UserSubscription activateSubscription(User user, SubscriptionPlan plan) {

        subscriptionRepository.findByUserIdAndIsActiveTrue(user.getId())
                .ifPresent(old -> {
                    old.setIsActive(false);
                    old.setStatus("EXPIRED");
                    subscriptionRepository.save(old);

                    profilePremiumSyncService.sync(user, null);
                });

        UserSubscription subscription = new UserSubscription();

        subscription.setUser(user);
        subscription.setSubscriptionPlan(plan);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(plan.getDuration());

        subscription.setStartDate(start);
        subscription.setEndDate(end);

        subscription.setIsActive(true);
        subscription.setStatus("ACTIVE");

        UserSubscription saved = subscriptionRepository.save(subscription);
        notificationService.createAdminNotification(
                "Premium Subscription Activated",
                user.getFullName()
                        + " activated the "
                        + plan.getName()
                        + " subscription plan.",
                NotificationType.SUBSCRIPTION
        );
        notificationService.createSubscriptionReminder(
                user.getId(),
                saved.getId(),
                "Premium Activated",
                "Your " + plan.getName() + " subscription is now active."
        );
        // Synchronize Profile
        profilePremiumSyncService.sync(user, saved);

        return saved;
    }
    // =====================================================
    // CREATE SUBSCRIPTION
    // =====================================================

    @Override
    @Transactional
    public UserSubscription create(UserSubscription subscription) {

        Long userId = subscription.getUser().getId();

        subscriptionRepository.findByUserIdAndIsActiveTrue(userId)
                .ifPresent(old -> {
                    old.setIsActive(false);
                    old.setStatus("CANCELLED");
                    subscriptionRepository.save(old);

                    profilePremiumSyncService.sync(old.getUser(), null);
                });

        subscription.setIsActive(true);

        if (subscription.getStatus() == null) {
            subscription.setStatus("ACTIVE");
        }

        UserSubscription saved = subscriptionRepository.save(subscription);
        notificationService.createAdminNotification(
                "Premium Subscription Created",
                saved.getUser().getFullName()
                        + " received the "
                        + saved.getSubscriptionPlan().getName()
                        + " subscription plan.",
                NotificationType.SUBSCRIPTION
        );
        // Synchronize Profile
        profilePremiumSyncService.sync(saved.getUser(), saved);

        return saved;
    }

    // =====================================================
    // GET METHODS
    // =====================================================

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

    // =====================================================
    // DEACTIVATE SUBSCRIPTION
    // =====================================================

    @Override
    @Transactional
    public void deactivate(Long id) {

        UserSubscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Subscription not found"));

        subscription.setIsActive(false);
        subscription.setStatus("CANCELLED");

        subscriptionRepository.save(subscription);

        // Synchronize Profile
        profilePremiumSyncService.sync(subscription.getUser(), null);
    }
    // =====================================================
    // MY SUBSCRIPTIONS
    // =====================================================

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

    // =====================================================
    // CURRENT USER
    // =====================================================

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

    // =====================================================
    // GET SUBSCRIPTION BY ID
    // =====================================================

    @Override
    public Optional<UserSubscription> getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id);
    }

}