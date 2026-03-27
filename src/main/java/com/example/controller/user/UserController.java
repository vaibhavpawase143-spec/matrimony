package com.example.controller.user;

import com.example.dto.response.UserResponseDTO;
import com.example.model.Role;
import com.example.model.User;
import com.example.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService service;

    // =========================
    // ➕ CREATE USER (ADMIN ONLY)
    // =========================
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDTO create(@RequestBody User user) {

        // 🔥 FIX: using register() instead of create()
        User savedUser = service.register(user);

        return mapToResponse(savedUser);
    }

    // =========================
    // 🔍 GET USER BY ID
    // =========================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public UserResponseDTO getById(@PathVariable Long id) {

        User user = service.getById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapToResponse(user);
    }

    // =========================
    // 🔍 GET ALL USERS
    // =========================
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Set<UserResponseDTO> getAll() {

        return service.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toSet());
    }

    // =========================
    // ✅ VERIFY EMAIL
    // =========================
    @PutMapping("/{id}/verify-email")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String verifyEmail(@PathVariable Long id) {

        User user = service.getById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmailVerifiedAt(LocalDateTime.now());
        service.update(id, user);

        return "Email verified successfully";
    }

    // =========================
    // ✅ VERIFY PHONE
    // =========================
    @PutMapping("/{id}/verify-phone")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String verifyPhone(@PathVariable Long id) {

        User user = service.getById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPhoneVerifiedAt(LocalDateTime.now());
        service.update(id, user);

        return "Phone verified successfully";
    }

    // =========================
    // ❌ DEACTIVATE USER (ADMIN ONLY)
    // =========================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deactivate(@PathVariable Long id) {

        service.deactivate(id);

        return "User deactivated successfully";
    }

    // =========================
    // 🔍 SEARCH USERS
    // =========================
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public Set<UserResponseDTO> search(@RequestParam String keyword) {

        return service.search(keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toSet());
    }

    // =========================
    // 🔥 TEST API (JWT CHECK)
    // =========================
    @GetMapping("/test")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String test() {
        return "JWT is working perfectly 🔥";
    }

    // =========================
    // 🔁 MAPPING METHOD
    // =========================
    private UserResponseDTO mapToResponse(User user) {

        Set<String> roles = user.getRoles() != null
                ? user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet())
                : null;

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
                .build();
    }
}