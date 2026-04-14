package com.example.serviceimpl;

import com.example.model.*;
import com.example.repository.*;
import com.example.service.CompatibilityService;
import com.example.service.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepo;
    private final ProfileRepository profileRepo;
    private final FamilyDetailsRepository familyRepo;
    private final PartnerPreferenceRepository prefRepo;
    private final UserSubscriptionRepository subRepo;
    private final PaymentRepository paymentRepo;
    private final CompatibilityService compatibilityService;

    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public UserDetails getUserDetails(Long userId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Profile profile = profileRepo.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        FamilyDetails family = profile != null ? familyRepo.findByProfile(profile) : null;
        PartnerPreference pref = prefRepo.findByUser(user);
        UserSubscription sub = subRepo.findByUser(user);
        Payment payment = paymentRepo.findByUser(user);

        UserDetails dto = new UserDetails();

        // ===== USER =====
        dto.setUserId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setIsActive(user.getIsActive());

        dto.setRoles(
                user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet())
        );

        // ===== PROFILE =====
        if (profile != null) {
            dto.setReligion(profile.getReligion() != null ? profile.getReligion().getName() : null);
            dto.setCaste(profile.getCaste() != null ? profile.getCaste().getName() : null);
            dto.setEducation(profile.getEducationLevel() != null ? profile.getEducationLevel().getName() : null);
            dto.setOccupation(profile.getOccupation() != null ? profile.getOccupation().getName() : null);
            dto.setHeight(profile.getHeight() != null ? profile.getHeight().getHeight() : null);
            dto.setWeight(profile.getWeight() != null ? profile.getWeight().getValue() : null);

            if (profile.getCity() != null) {
                dto.setCity(profile.getCity().getName());

                if (profile.getCity().getState() != null) {
                    dto.setState(profile.getCity().getState().getName());

                    if (profile.getCity().getState().getCountry() != null) {
                        dto.setCountry(profile.getCity().getState().getCountry().getName());
                    }
                }
            }

            dto.setAbout(profile.getAbout());
        }

        // ===== FAMILY =====
        if (family != null) {
            dto.setFamilyType(family.getFamilyType() != null ? family.getFamilyType().getName() : null);
            dto.setFamily(family.getFamily() != null ? family.getFamily().getName() : null);
            dto.setBrotherType(family.getBrotherType() != null ? family.getBrotherType().getValue() : null);
            dto.setSisterType(family.getSisterType() != null ? family.getSisterType().getValue() : null);
            dto.setFatherOccupation(family.getFatherOccupation());
            dto.setMotherOccupation(family.getMotherOccupation());
        }

        // ===== PREFERENCE =====
        if (pref != null) {
            dto.setMinAge(pref.getMinAge());
            dto.setMaxAge(pref.getMaxAge());
            dto.setMinHeight(pref.getMinHeight());
            dto.setMaxHeight(pref.getMaxHeight());
            dto.setPreferredReligion(pref.getReligion() != null ? pref.getReligion().getName() : null);
            dto.setPreferredCaste(pref.getCaste() != null ? pref.getCaste().getName() : null);
            dto.setPreferredCity(pref.getCity() != null ? pref.getCity().getName() : null);
        }

        // ===== SUBSCRIPTION =====
        if (sub != null) {
            dto.setPlanName(sub.getSubscriptionPlan() != null ? sub.getSubscriptionPlan().getName() : null);
            dto.setSubscriptionStatus(sub.getStatus());
        }

        // ===== PAYMENT =====
        if (payment != null) {
            dto.setPaymentStatus(payment.getStatus());
        }

        // ===== AUDIT =====
        dto.setCreatedAt(user.getCreatedAt());

        return dto;
    }
}