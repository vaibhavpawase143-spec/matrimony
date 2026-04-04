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

        return UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .isActive(user.getIsActive())

                // ✅ FIXED
                .emailVerified(user.getEmailVerifiedAt() != null)
                .phoneVerified(user.getPhoneVerifiedAt() != null)

                // ✅ FIXED
                .roles(
                        user.getRoles() == null
                                ? Collections.emptySet()
                                : user.getRoles()
                                .stream()
                                .map(role -> role.getName())
                                .collect(Collectors.toSet())
                )

                // ✅ FIXED
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())

                .build();
    }
}