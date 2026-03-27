package com.example.service;

import com.example.model.Admin;
import com.example.model.User;
import com.example.repository.AdminRepository;
import com.example.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // =========================
        // 🔥 CHECK ADMIN FIRST
        // =========================
        return adminRepository.findByEmail(email)
                .map(admin -> {
                    System.out.println("LOGIN AS ADMIN ✅");

                    return new org.springframework.security.core.userdetails.User(
                            admin.getEmail(),
                            admin.getPassword(),
                            List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                    );
                })

                // =========================
                // 🔥 CHECK USER (ACTIVE ONLY)
                // =========================
                .orElseGet(() ->
                        userRepository.findByEmailIgnoreCaseAndIsActiveTrue(email)
                                .map(user -> {
                                    System.out.println("LOGIN AS USER ✅");

                                    return new org.springframework.security.core.userdetails.User(
                                            user.getEmail(),
                                            user.getPassword(),
                                            user.getRoles().stream()
                                                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                                                    .toList()
                                    );
                                })
                                .orElseThrow(() -> {
                                    System.out.println("USER NOT FOUND ❌");
                                    return new UsernameNotFoundException("User not found with email: " + email);
                                })
                );
    }
}