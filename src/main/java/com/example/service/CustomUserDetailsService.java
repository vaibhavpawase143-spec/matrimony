package com.example.service;

import com.example.model.UserDetails;
import com.example.repository.AdminRepository;
import com.example.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 🔥 IMPORTANT IMPORT (SPRING ONE)
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    // =========================
    // 🔐 LOAD USER BY EMAIL (SPRING SECURITY)
    // =========================
    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        // 🔥 CHECK ADMIN FIRST
        return adminRepository.findByEmail(email)
                .map(admin -> {
                    logger.info("LOGIN AS ADMIN: {}", email);

                    return new org.springframework.security.core.userdetails.User(
                            admin.getEmail(),
                            admin.getPassword(),
                            List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                    );
                })

                // 🔥 IF NOT ADMIN → CHECK USER
                .orElseGet(() ->
                        userRepository.findByEmailIgnoreCaseAndIsActiveTrue(email)
                                .map(user -> {
                                    logger.info("LOGIN AS USER: {}", email);

                                    return new org.springframework.security.core.userdetails.User(
                                            user.getEmail(),
                                            user.getPassword(),
                                            user.getRoles().stream()
                                                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                                                    .toList()
                                    );
                                })
                                .orElseThrow(() -> {
                                    logger.error("USER NOT FOUND: {}", email);
                                    return new UsernameNotFoundException("User not found with email: " + email);
                                })
                );
    }

    // =========================
    // 🔍 GET USER DETAILS (CUSTOM DTO)
    // =========================
    public UserDetails getUserDetails(Long userId) {

        // 🔍 Check USER first
        return userRepository.findById(userId)
                .map(user -> {
                    UserDetails dto = new UserDetails();

                    dto.setId(user.getId());
                    dto.setEmail(user.getEmail());
                    dto.setRoles(
                            user.getRoles().stream()
                                    .map(role -> role.getName())
                                    .collect(Collectors.toSet())
                    );

                    return dto;
                })

                // 🔥 If not found → ADMIN
                .orElseGet(() ->
                        adminRepository.findById(userId)
                                .map(admin -> {
                                    UserDetails dto = new UserDetails();

                                    dto.setId(admin.getId());
                                    dto.setEmail(admin.getEmail());
                                    dto.setRoles(java.util.Set.of("ROLE_ADMIN"));

                                    return dto;
                                })
                                .orElseThrow(() ->
                                        new RuntimeException("User/Admin not found with id: " + userId)
                                )
                );
    }
}