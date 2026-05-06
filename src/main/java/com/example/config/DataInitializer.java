package com.example.config;

import com.example.model.Admin;
import com.example.model.Role;
import com.example.model.User;
import com.example.repository.AdminRepository;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initializeRoles();
        initializeSuperAdmin();
        initializeTestUser();
    }

    private void initializeRoles() {
        try {
            if (!roleRepository.findByNameIgnoreCase("ROLE_SUPER_ADMIN").isPresent()) {
                Role superAdminRole = new Role();
                superAdminRole.setName("ROLE_SUPER_ADMIN");
                superAdminRole.setIsActive(true);
                roleRepository.save(superAdminRole);
                log.info("Created ROLE_SUPER_ADMIN");
            }

            if (!roleRepository.findByNameIgnoreCase("ROLE_ADMIN").isPresent()) {
                Role adminRole = new Role();
                adminRole.setName("ROLE_ADMIN");
                adminRole.setIsActive(true);
                roleRepository.save(adminRole);
                log.info("Created ROLE_ADMIN");
            }

            if (!roleRepository.findByNameIgnoreCase("ROLE_USER").isPresent()) {
                Role userRole = new Role();
                userRole.setName("ROLE_USER");
                userRole.setIsActive(true);
                roleRepository.save(userRole);
                log.info("Created ROLE_USER");
            }

        } catch (Exception e) {
            log.error("Error initializing roles: {}", e.getMessage());
        }
    }

    private void initializeSuperAdmin() {
        try {
            if (!adminRepository.findByEmailIgnoreCase("admin@gathbandhan.com").isPresent()) {
                Role superAdminRole = roleRepository.findByNameIgnoreCase("ROLE_SUPER_ADMIN")
                    .orElseThrow(() -> new RuntimeException("ROLE_SUPER_ADMIN not found"));

                Admin superAdmin = new Admin();
                superAdmin.setName("Super Admin");
                superAdmin.setUsername("superadmin");
                superAdmin.setEmail("admin@gathbandhan.com");
                superAdmin.setPassword(passwordEncoder.encode("admin123"));
                superAdmin.setPhone("0000000000");
                superAdmin.setIsActive(true);
                superAdmin.setRole(superAdminRole);
                superAdmin.setCreatedAt(LocalDateTime.now());

                adminRepository.save(superAdmin);
                log.info("Created super admin: admin@gathbandhan.com / admin123");
            }
        } catch (Exception e) {
            log.error("Error initializing super admin: {}", e.getMessage());
        }
    }

    private void initializeTestUser() {
        try {
            if (!userRepository.existsByEmail("testuser@example.com")) {
                Role userRole = roleRepository.findByNameIgnoreCase("ROLE_USER")
                        .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));

                User testUser = new User();
                testUser.setFirstName("Test");
                testUser.setLastName("User");
                testUser.setEmail("testuser@example.com");
                testUser.setPassword(passwordEncoder.encode("test123"));
                testUser.setPhone("1234567890");
                testUser.setEmailVerified(true);
                testUser.setPhoneVerified(true);
                testUser.setIsActive(true);
                testUser.setCreatedAt(LocalDateTime.now());
                testUser.setRoles(new java.util.HashSet<>(java.util.Set.of(userRole)));

                userRepository.save(testUser);
                log.info("Created test user: testuser@example.com / test123");
            }
        } catch (Exception e) {
            log.error("Error initializing test user: {}", e.getMessage());
        }
    }
}
