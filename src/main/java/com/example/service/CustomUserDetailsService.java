package com.example.service;

import com.example.model.Admin;
import com.example.model.User;
import com.example.repository.AdminRepository;
import com.example.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 🔥 SPRING SECURITY
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    // =========================
    // 🔐 AUTHENTICATION (LOGIN)
    // =========================
    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        // 🔥 1. CHECK ADMIN FIRST
        Admin admin = adminRepository.findByEmail(email).orElse(null);

        if (admin != null && Boolean.TRUE.equals(admin.getIsActive())) {
            logger.info("LOGIN AS ADMIN: {}", email);

            return new org.springframework.security.core.userdetails.User(
                    admin.getEmail(),
                    admin.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
            );
        }

        // 🔥 2. CHECK USER
        User user = userRepository.findByEmailIgnoreCaseAndIsActiveTrue(email)
                .orElseThrow(() -> {
                    logger.error("USER NOT FOUND: {}", email);
                    return new UsernameNotFoundException("User not found or inactive");
                });

        logger.info("LOGIN AS USER: {}", email);

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList())
        );
    }

    // =========================
    // 🔍 CUSTOM DTO (FOR TOKEN / RESPONSE)
    // =========================

    public com.example.model.UserDetails getUserDetails(Long userId) {

        // 🔍 CHECK USER FIRST
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {

            com.example.model.UserDetails details = new com.example.model.UserDetails();

            details.setId(user.getId());
            details.setUserId(user.getId());
            details.setFullName(user.getFullName());
            details.setEmail(user.getEmail());
            details.setPhone(user.getPhone());
            details.setIsActive(user.getIsActive());

            details.setRoles(
                    user.getRoles().stream()
                            .map(role -> role.getName())
                            .collect(Collectors.toSet())
            );

            return details;
        }

        // 🔥 CHECK ADMIN
        Admin admin = adminRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User/Admin not found with id: " + userId));

        com.example.model.UserDetails details = new com.example.model.UserDetails();

        details.setId(admin.getId());
        details.setUserId(admin.getId());
        details.setFullName(admin.getName());
        details.setEmail(admin.getEmail());
        details.setPhone(admin.getPhone());
        details.setIsActive(admin.getIsActive());

        details.setRoles(Set.of("ROLE_ADMIN"));

        return details;
    }
}