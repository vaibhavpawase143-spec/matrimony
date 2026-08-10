package com.example.security;

import com.example.model.Admin;
import com.example.model.Role;
import com.example.model.User;
import com.example.repository.AdminRepository;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        long start = System.currentTimeMillis();
        // ================= 🔥 ADMIN LOGIN =================
        Admin admin = adminRepository.findByEmailWithRole(email).orElse(null);

        if (admin != null) {

            if (admin.getRole() == null) {
                throw new UsernameNotFoundException("Admin role not assigned");
            }

            Role role = roleRepository.findByNameWithPermissions(
                    admin.getRole().getName()
            ).orElseThrow(() ->
                    new UsernameNotFoundException("Role not found")
            );

            List<GrantedAuthority> authorities = new java.util.ArrayList<>();
            System.out.println("Authorities : " + authorities);
// Add Role
            authorities.add(new SimpleGrantedAuthority(role.getName()));

// Add Permissions
            role.getPermissions().stream()
                    .filter(permission -> Boolean.TRUE.equals(permission.getIsActive()))
                    .forEach(permission ->
                            authorities.add(
                                    new SimpleGrantedAuthority(permission.getCode())
                            )
                    );
            System.out.println(
                    "LOAD USER (ADMIN) = "
                            + (System.currentTimeMillis() - start)
                            + " ms"
            );

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
        long totalStart = System.currentTimeMillis();

// ================= USER DB =================
        long dbStart = System.currentTimeMillis();

        User user = userRepository.findByEmailWithRoles(email)
                .orElse(null);

        long dbEnd = System.currentTimeMillis();

        System.out.println("AUTH DB = " + (dbEnd - dbStart) + " ms");

        if (user != null) {

            if (user.getRoles() == null || user.getRoles().isEmpty()) {
                throw new UsernameNotFoundException("User has no roles assigned");
            }

            List<GrantedAuthority> authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());

            if (!Boolean.TRUE.equals(user.getIsActive())
                    || Boolean.TRUE.equals(user.getIsDeleted())
                    || Boolean.TRUE.equals(user.getIsBlocked())) {

                throw new UsernameNotFoundException("User is disabled");
            }

            long objectStart = System.currentTimeMillis();

            CustomUserDetails details = new CustomUserDetails(user);

            long objectEnd = System.currentTimeMillis();

            System.out.println("AUTH OBJECT = " + (objectEnd - objectStart) + " ms");
            System.out.println("AUTH TOTAL = " + (System.currentTimeMillis() - totalStart) + " ms");

            return details;
        }
        throw new UsernameNotFoundException("User/Admin not found with email: " + email);
    }
}