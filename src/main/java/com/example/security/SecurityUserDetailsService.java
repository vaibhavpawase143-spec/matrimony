package com.example.security;

import com.example.model.Admin;
import com.example.model.User;
import com.example.repository.AdminRepository;
import com.example.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        // ================= 🔥 ADMIN LOGIN =================
        Admin admin = adminRepository.findByEmailWithRole(email).orElse(null);

        if (admin != null) {

            // 🔥 FORCE LOAD ROLE
            admin.getRole().getName();

            return new org.springframework.security.core.userdetails.User(
                    admin.getEmail(),
                    admin.getPassword(),
                    List.of(new SimpleGrantedAuthority(admin.getRole().getName()))
            );
        }

        // ================= 🔥 USER LOGIN =================
        User user = userRepository.findByEmailWithRoles(email).orElse(null);

        if (user != null) {

            // 🔥 FORCE LOAD ROLES
            user.getRoles().forEach(role -> role.getName());

            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    user.getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority(role.getName()))
                            .collect(Collectors.toList())
            );
        }

        throw new UsernameNotFoundException("User/Admin not found with email: " + email);
    }
}