package com.example.mapper;

import com.example.dto.response.UserResponseDTO;
import com.example.model.User;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    private UserMapper() {}

    public static UserResponseDTO toDTO(User user) {

        if (user == null) return null;

        // 🔥 SAFE ROLE MAPPING (NO LAZY ERROR)
        Set<String> roles = Collections.emptySet();

        try {
            if (user.getRoles() != null) {
                roles = user.getRoles()
                        .stream()
                        .map(role -> role.getName())
                        .collect(Collectors.toSet());
            }
        } catch (Exception e) {
            // 🔥 fallback (prevents crash in edge cases)
            roles = Collections.emptySet();
        }

        return UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .isActive(user.getIsActive())

                .emailVerified(user.getEmailVerifiedAt() != null)
                .phoneVerified(user.getPhoneVerifiedAt() != null)

                .roles(roles)

                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())

                .isOnline(user.getIsOnline())
                .lastSeen(user.getLastSeen())

                .build();
    }
}