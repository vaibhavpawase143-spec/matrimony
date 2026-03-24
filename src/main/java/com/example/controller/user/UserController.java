package com.example.controller.user;

import com.example.dto.request.UserLoginRequestDTO;
import com.example.dto.request.UserRegisterRequestDTO;
import com.example.dto.response.UserResponseDTO;
import com.example.model.Role;
import com.example.model.User;
import com.example.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    // =========================
    // ✅ REGISTER
    // =========================
    @PostMapping("/register")
    public UserResponseDTO register(@Valid @RequestBody UserRegisterRequestDTO dto) {

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setPassword(dto.getPassword());

        User saved = service.register(user);

        return mapToResponse(saved);
    }

    // =========================
    // 🔐 LOGIN
    // =========================
    @PostMapping("/login")
    public UserResponseDTO login(@Valid @RequestBody UserLoginRequestDTO dto) {

        User user = service.login(dto.getEmail(), dto.getPassword());

        return mapToResponse(user);
    }

    // =========================
    // 🔍 GET USER
    // =========================
    @GetMapping("/{id}")
    public UserResponseDTO getById(@PathVariable Long id) {

        User user = service.getById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapToResponse(user);
    }

    // =========================
    // ✅ VERIFY EMAIL
    // =========================
    @PutMapping("/{id}/verify-email")
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
    public String verifyPhone(@PathVariable Long id) {

        User user = service.getById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPhoneVerifiedAt(LocalDateTime.now());

        service.update(id, user);

        return "Phone verified successfully";
    }

    // =========================
    // ❌ DEACTIVATE
    // =========================
    @DeleteMapping("/{id}")
    public String deactivate(@PathVariable Long id) {
        service.deactivate(id);
        return "User deactivated successfully";
    }

    // =========================
    // 🔥 MAPPING
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