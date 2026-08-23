package com.example.serviceimpl;

import com.example.model.PremiumPlan;
import com.example.model.Profile;
import com.example.model.User;
import com.example.model.UserSubscription;
import com.example.repository.ProfileRepository;
import com.example.service.ProfilePremiumSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProfilePremiumSyncServiceImpl
        implements ProfilePremiumSyncService {

    private final ProfileRepository profileRepository;

    @Override
    public void sync(User user, UserSubscription subscription) {

        if (user == null || user.getId() == null) {
            log.warn("[PROFILE SYNC] User or User ID is null. Skipping sync.");
            return;
        }

        log.debug("[PROFILE SYNC] Starting profile sync for User ID: {}", user.getId());

        Optional<Profile> profileOpt = profileRepository.findByUserId(user.getId());
        if (profileOpt.isEmpty()) {
            log.info("[PROFILE SYNC] Profile not yet created for User ID: {}. Skipping sync without error.", user.getId());
            return;
        }

        Profile profile = profileOpt.get();

        // =====================================================
        // NO ACTIVE SUBSCRIPTION -> RESET TO FREE
        // =====================================================
        if (subscription == null
                || !Boolean.TRUE.equals(subscription.getIsActive())
                || !"ACTIVE".equalsIgnoreCase(subscription.getStatus())) {

            log.debug("[PROFILE SYNC] Updating User ID: {} profile -> FREE", user.getId());

            profile.setIsPremium(false);
            profile.setPremiumPlan(PremiumPlan.FREE);
            profile.setPremiumStartDate(null);
            profile.setPremiumEndDate(null);

            profileRepository.save(profile);
            log.debug("[PROFILE SYNC] Profile successfully set to FREE for User ID: {}", user.getId());
            return;
        }

        // =====================================================
        // ACTIVE SUBSCRIPTION -> SYNC TO PREMIUM
        // =====================================================
        log.debug("[PROFILE SYNC] Updating User ID: {} profile -> PREMIUM (Plan: {})",
                user.getId(), subscription.getSubscriptionPlan() != null ? subscription.getSubscriptionPlan().getName() : "N/A");

        profile.setIsPremium(true);
        profile.setPremiumStartDate(subscription.getStartDate());
        profile.setPremiumEndDate(subscription.getEndDate());

        int duration = subscription.getSubscriptionPlan() != null && subscription.getSubscriptionPlan().getDuration() != null
                ? subscription.getSubscriptionPlan().getDuration()
                : 30;

        if (duration <= 45) {
            profile.setPremiumPlan(PremiumPlan.ONE_MONTH);
        } else if (duration <= 120) {
            profile.setPremiumPlan(PremiumPlan.THREE_MONTHS);
        } else if (duration <= 240) {
            profile.setPremiumPlan(PremiumPlan.SIX_MONTHS);
        } else {
            profile.setPremiumPlan(PremiumPlan.TWELVE_MONTHS);
        }

        profileRepository.save(profile);
        log.debug("[PROFILE SYNC] Profile successfully synced to PREMIUM for User ID: {}", user.getId());
    }
}