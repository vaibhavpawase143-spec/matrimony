package com.example.controller.admin;

import com.example.dto.response.ApiResponse;
import com.example.model.User;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AdminUserManagementController {

    private final UserRepository userRepository;

    // ================= GET USERS =================

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return new ApiResponse<>(true, "All users retrieved", users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<User> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new ApiResponse<>(true, "User retrieved", user);
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<User>> getActiveUsers() {
        List<User> users = userRepository.findByIsActiveTrue();
        return new ApiResponse<>(true, "Active users retrieved", users);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<User>> searchUsers(@RequestParam String keyword) {
        List<User> users = userRepository.searchWithRoles(keyword);
        return new ApiResponse<>(true, "Users found", users);
    }

    // ================= USER ACTIONS =================

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<User> activateUser(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setIsActive(true);
            userRepository.save(user);
            return new ApiResponse<>(true, "User activated successfully", user);
        }
        throw new RuntimeException("User not found");
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<User> deactivateUser(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setIsActive(false);
            userRepository.save(user);
            return new ApiResponse<>(true, "User deactivated successfully", user);
        }
        throw new RuntimeException("User not found");
    }

    @PutMapping("/{id}/verify-email")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<User> verifyUserEmail(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setEmailVerified(true);
            user.setEmailVerifiedAt(java.time.LocalDateTime.now());
            userRepository.save(user);
            return new ApiResponse<>(true, "Email verified successfully", user);
        }
        throw new RuntimeException("User not found");
    }

    @PutMapping("/{id}/verify-phone")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<User> verifyUserPhone(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPhoneVerified(true);
            user.setPhoneVerifiedAt(java.time.LocalDateTime.now());
            userRepository.save(user);
            return new ApiResponse<>(true, "Phone verified successfully", user);
        }
        throw new RuntimeException("User not found");
    }

    // ================= USER DELETION =================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ApiResponse<String> deleteUser(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
            return new ApiResponse<>(true, "User deleted successfully", null);
        }
        throw new RuntimeException("User not found");
    }

    @PutMapping("/{id}/soft-delete")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ApiResponse<User> softDeleteUser(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setIsDeleted(true);
            user.setIsActive(false);
            userRepository.save(user);
            return new ApiResponse<>(true, "User soft deleted successfully", user);
        }
        throw new RuntimeException("User not found");
    }

    // ================= STATISTICS =================

    @GetMapping("/stats/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Map<String, Long>> getUserCount() {
        long total = userRepository.count();
        long active = userRepository.findByIsActiveTrue().size();
        long inactive = total - active;
        
        Map<String, Long> stats = Map.of(
                "total", total,
                "active", active,
                "inactive", inactive
        );
        return new ApiResponse<>(true, "User count statistics", stats);
    }

    @GetMapping("/export")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<String> exportUsers() {
        // TODO: Implement CSV/Excel export
        return ResponseEntity.ok("Export functionality coming soon");
    }
}

