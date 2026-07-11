package com.example.mapper;

import com.example.dto.response.AdminUserResponseDTO;
import com.example.model.Profile;
import com.example.model.User;

public class AdminUserMapper {

    private AdminUserMapper() {
    }

    public static AdminUserResponseDTO toDTO(User user) {

        if (user == null) {
            return null;
        }

        AdminUserResponseDTO dto = new AdminUserResponseDTO();

        // ================= BASIC =================

        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());

        // ================= STATUS =================

        dto.setActive(user.getIsActive());
        dto.setBlocked(user.getIsBlocked());
        dto.setDeleted(user.getIsDeleted());

        // ================= VERIFICATION =================

        dto.setEmailVerified(user.getEmailVerified());
        dto.setPhoneVerified(user.getPhoneVerified());

        // ================= USER ACTIVITY =================

        dto.setOnline(user.getIsOnline());
        dto.setLastSeen(user.getLastSeen());
        dto.setLastLogin(user.getLastLogin());

        // ================= AUDIT =================

        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        // ================= PROFILE =================

        Profile profile = user.getProfile();

        if (profile != null) {

            dto.setImageUrl(profile.getImageUrl());

            dto.setProfileCompleted(profile.getProfileCompleted());

            dto.setPremium(profile.getIsPremium());

            if (profile.getPremiumPlan() != null) {
                dto.setPremiumPlan(profile.getPremiumPlan().name());
            }

            if (profile.getGender() != null) {
                dto.setGender(profile.getGender().getName());
            }

            if (profile.getReligion() != null) {
                dto.setReligion(profile.getReligion().getName());
            }

            if (profile.getCaste() != null) {
                dto.setCaste(profile.getCaste().getName());
            }

            if (profile.getCity() != null) {
                dto.setCity(profile.getCity().getName());
            }
        }

        return dto;
    }
}