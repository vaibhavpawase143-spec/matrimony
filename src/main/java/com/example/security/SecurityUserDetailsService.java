package com.example.security;

import com.example.model.Admin;
import com.example.model.User;
import com.example.repository.AdminRepository;
import com.example.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
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
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        // ================= 🔥 ADMIN LOGIN =================
        Admin admin = adminRepository.findByEmailWithRole(email).orElse(null);

        if (admin != null) {

            if (admin.getRole() == null) {
                throw new UsernameNotFoundException("Admin role not assigned");
            }

            String roleName = admin.getRole().getName();

            List<GrantedAuthority> authorities =
                    List.of(new SimpleGrantedAuthority(roleName));

            return new org.springframework.security.core.userdetails.User(
                    admin.getEmail(),
                    admin.getPassword(),

                    // ✅ USE YOUR FIELD (NO HARDCODE)
                    Boolean.TRUE.equals(admin.getIsActive()),

                    true,
                    true,
                    true,
                    authorities
            );
        }

        // ================= 🔥 USER LOGIN =================
        User user = userRepository.findByEmailWithRoles(email).orElse(null);

        if (user != null) {

            if (user.getRoles() == null || user.getRoles().isEmpty()) {
                throw new UsernameNotFoundException("User has no roles assigned");
            }

            List<GrantedAuthority> authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());

            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),

                    // ✅ MULTI SECURITY CHECK (BEST PRACTICE)
                    Boolean.TRUE.equals(user.getIsActive())
                            && !Boolean.TRUE.equals(user.getIsDeleted())
                            && !Boolean.TRUE.equals(user.getIsBlocked()),

                    true,
                    true,
                    true,
                    authorities
            );
        }

        throw new UsernameNotFoundException("User/Admin not found with email: " + email);
    }
}