package com.example.serviceimpl;

import com.example.exception.ResourceNotFoundException;
import com.example.model.PremiumPlan;
import com.example.model.Profile;
import com.example.model.User;
import com.example.model.UserSubscription;
import com.example.repository.ProfileRepository;
import com.example.service.ProfilePremiumSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfilePremiumSyncServiceImpl
        implements ProfilePremiumSyncService {

    private final ProfileRepository profileRepository;

    @Override
    public void sync(User user, UserSubscription subscription) {

        System.out.println("========================================");
        System.out.println("PROFILE SYNC STARTED");
        System.out.println("User ID : " + user.getId());

        if (subscription == null) {
            System.out.println("Subscription : NULL");
        } else {
            System.out.println("Subscription ID : " + subscription.getId());
            System.out.println("Subscription Active : " + subscription.getIsActive());
            System.out.println("Subscription Plan : " + subscription.getSubscriptionPlan().getName());
            System.out.println("Duration : " + subscription.getSubscriptionPlan().getDuration());
        }

        Profile profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Profile not found"));

        System.out.println("Profile Found : " + profile.getId());

        // =====================================================
        // NO ACTIVE SUBSCRIPTION
        // =====================================================

        if (subscription == null
                || !Boolean.TRUE.equals(subscription.getIsActive())) {

            System.out.println("Updating Profile -> FREE");

            profile.setIsPremium(false);
            profile.setPremiumPlan(PremiumPlan.FREE);
            profile.setPremiumStartDate(null);
            profile.setPremiumEndDate(null);

            System.out.println("Saving Profile...");
            profileRepository.save(profile);

            System.out.println("Profile Saved Successfully");
            System.out.println("========================================");
            return;
        }

        // =====================================================
        // ACTIVE SUBSCRIPTION
        // =====================================================

        System.out.println("Updating Profile -> PREMIUM");

        profile.setIsPremium(true);
        profile.setPremiumStartDate(subscription.getStartDate());
        profile.setPremiumEndDate(subscription.getEndDate());

        int duration = subscription.getSubscriptionPlan().getDuration();

        switch (duration) {

            case 30 ->
                    profile.setPremiumPlan(PremiumPlan.ONE_MONTH);

            case 90 ->
                    profile.setPremiumPlan(PremiumPlan.THREE_MONTHS);

            case 180 ->
                    profile.setPremiumPlan(PremiumPlan.SIX_MONTHS);

            case 365 ->
                    profile.setPremiumPlan(PremiumPlan.TWELVE_MONTHS);

            default ->
                    profile.setPremiumPlan(PremiumPlan.FREE);
        }

        System.out.println("Premium : " + profile.getIsPremium());
        System.out.println("Premium Plan : " + profile.getPremiumPlan());
        System.out.println("Premium Start : " + profile.getPremiumStartDate());
        System.out.println("Premium End : " + profile.getPremiumEndDate());

        System.out.println("Saving Profile...");
        profileRepository.save(profile);

        System.out.println("Profile Saved Successfully");
        System.out.println("========================================");
    }
}