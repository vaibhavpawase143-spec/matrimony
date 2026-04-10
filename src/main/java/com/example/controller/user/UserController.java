package com.example.controller.user;

import com.example.dto.request.LoginRequest;
import com.example.dto.request.UserFilterDTO;
import com.example.dto.request.UserRegisterRequestDTO;
import com.example.dto.response.*;
import com.example.model.User;
import com.example.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService service;

    // ================= REGISTER =================
    @PostMapping("/register")
    public ApiResponse<UserResponseDTO> register(@RequestBody UserRegisterRequestDTO dto) {

        User savedUser = service.register(dto);

        UserResponseDTO response = service.getById(savedUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ApiResponse.<UserResponseDTO>builder()
                .success(true)
                .message("User registered successfully")
                .data(response)
                .build();
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {

        String token = service.loginAndGenerateToken(
                request.getEmail(),
                request.getPassword()
        );

        return ResponseEntity.ok(new AuthResponse(token));
    }

    // ================= CREATE =================
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
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

    // ================= SEARCH (🔥 FIXED POSITION) =================
    @GetMapping("/search")
    public ApiResponse<List<UserResponseDTO>> search(@RequestParam String keyword) {

        List<UserResponseDTO> users = service.search(keyword);

        return ApiResponse.<List<UserResponseDTO>>builder()
                .success(true)
                .message("Search result")
                .data(users)
                .build();
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN') or @userSecurity.isOwner(#id, authentication.name)")
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
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<List<UserResponseDTO>> getAll() {

        return ApiResponse.<List<UserResponseDTO>>builder()
                .success(true)
                .message("Users fetched successfully")
                .data(service.getAll())
                .build();
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN') or @userSecurity.isOwner(#id, authentication.name)")
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

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN') or @userSecurity.isOwner(#id, authentication.name)")
    public ApiResponse<String> delete(@PathVariable Long id) {

        service.deleteUser(id);

        return ApiResponse.<String>builder()
                .success(true)
                .message("User deleted successfully")
                .data(null)
                .build();
    }

    // ================= PAGINATION =================
    @GetMapping("/paged")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<PageResponse<UserResponseDTO>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Boolean isDeleted,
            @RequestParam(required = false) String role
    ) {

        UserFilterDTO filter = new UserFilterDTO();
        filter.setSearch(search);
        filter.setIsActive(isActive);
        filter.setIsDeleted(isDeleted);
        filter.setRole(role);

        return ApiResponse.<PageResponse<UserResponseDTO>>builder()
                .success(true)
                .message("Users fetched successfully")
                .data(service.getAllUsers(page, size, sortBy, direction, filter))
                .build();
    }

    // ================= USER STATUS =================
    @GetMapping("/status/{email}")
    public ApiResponse<UserResponseDTO> getUserStatus(@PathVariable String email) {

        User user = service.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponseDTO dto = UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .isOnline(user.getIsOnline())
                .lastSeen(user.getLastSeen())
                .build();

        return ApiResponse.<UserResponseDTO>builder()
                .success(true)
                .message("User status fetched")
                .data(dto)
                .build();
    }
}