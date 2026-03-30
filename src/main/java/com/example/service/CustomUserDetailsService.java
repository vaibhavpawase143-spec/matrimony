package com.example.service;

import com.example.repository.AdminRepository;
import com.example.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return adminRepository.findByEmail(email)
                .map(admin -> {
                    logger.info("LOGIN AS ADMIN");

                    return new org.springframework.security.core.userdetails.User(
                            admin.getEmail(),
                            admin.getPassword(),
                            List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                    );
                })

                .orElseGet(() ->
                        userRepository.findByEmailIgnoreCaseAndIsActiveTrue(email)
                                .map(user -> {
                                    logger.info("LOGIN AS USER");

                                    return new org.springframework.security.core.userdetails.User(
                                            user.getEmail(),
                                            user.getPassword(),
                                            user.getRoles().stream()
                                                    .map(role -> new SimpleGrantedAuthority(role.getName())) // ✅ FIXED
                                                    .toList()
                                    );
                                })
                                .orElseThrow(() -> {
                                    logger.error("USER NOT FOUND");
                                    return new UsernameNotFoundException("User not found with email: " + email);
                                })
                );
    }
}