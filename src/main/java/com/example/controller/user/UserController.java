package com.example.controller.user;

import com.example.dto.request.UserRegisterRequestDTO;
import com.example.dto.response.*;
import com.example.model.User;
import com.example.service.UserService;
import com.example.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService service;
    private final CustomUserDetailsService customUserDetailsService;

    // ================= CREATE =================
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserResponseDTO> create(@RequestBody UserRegisterRequestDTO dto) {

        User savedUser = service.register(dto);

        UserResponseDTO response = service.getById(savedUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ApiResponse.<UserResponseDTO>builder()
                .success(true)
                .message("User created successfully")
                .data(response)
                .build();
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ApiResponse<UserResponseDTO> getById(@PathVariable Long id) {

        UserResponseDTO user = service.getById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ApiResponse.<UserResponseDTO>builder()
                .success(true)
                .message("User fetched successfully")
                .data(user)
                .build();
    }

    // ================= GET ALL =================
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<UserResponseDTO>> getAll() {

        return ApiResponse.<List<UserResponseDTO>>builder()
                .success(true)
                .message("Users fetched successfully")
                .data(service.getAll())
                .build();
    }

    // ================= PAGINATION =================
    @GetMapping("/paginated")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<PageResponse<UserResponseDTO>> getPaginatedUsers(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sortBy,
            @RequestParam String direction
    ) {

        return ApiResponse.<PageResponse<UserResponseDTO>>builder()
                .success(true)
                .message("Users fetched successfully")
                .data(service.getAllUsers(page, size, sortBy, direction))
                .build();
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ApiResponse<UserResponseDTO> update(@PathVariable Long id,
                                               @RequestBody User user) {

        service.update(id, user);

        UserResponseDTO updated = service.getById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ApiResponse.<UserResponseDTO>builder()
                .success(true)
                .message("User updated successfully")
                .data(updated)
                .build();
    }

    // ================= VERIFY EMAIL =================
    @PutMapping("/{id}/verify-email")
    public ApiResponse<String> verifyEmail(@PathVariable Long id) {

        User user = service.update(id, new User());
        user.setEmailVerifiedAt(LocalDateTime.now());
        service.update(id, user);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Email verified successfully")
                .data(null)
                .build();
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ApiResponse<String> deactivate(@PathVariable Long id) {

        service.deactivate(id);

        return ApiResponse.<String>builder()
                .success(true)
                .message("User deactivated successfully")
                .data(null)
                .build();
    }
}