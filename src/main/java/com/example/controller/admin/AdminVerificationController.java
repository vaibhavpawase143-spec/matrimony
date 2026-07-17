//package com.example.controller.admin;
//
//import com.example.dto.response.ApiResponse;
//import com.example.dto.response.UserResponseDTO;
//import com.example.model.User;
//import com.example.service.UserVerificationService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/admin/verification")
//@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
//public class AdminVerificationController {
//
//    private final UserVerificationService userVerificationService;
//
//    @GetMapping("/pending-email")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse<Page<UserResponseDTO>> getPendingEmailVerifications(Pageable pageable) {
//        Page<User> users = userVerificationService.getUsersWithPendingEmailVerification(pageable);
//        Page<UserResponseDTO> response = users.map(this::convertToDTO);
//        return new ApiResponse<>(true, "Pending email verifications retrieved", response);
//    }
//
//    @GetMapping("/pending-phone")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse<Page<UserResponseDTO>> getPendingPhoneVerifications(Pageable pageable) {
//        Page<User> users = userVerificationService.getUsersWithPendingPhoneVerification(pageable);
//        Page<UserResponseDTO> response = users.map(this::convertToDTO);
//        return new ApiResponse<>(true, "Pending phone verifications retrieved", response);
//    }
//
//    @PostMapping("/verify-email/{userId}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse<String> verifyUserEmail(@PathVariable Long userId) {
//        userVerificationService.verifyEmailByAdmin(userId);
//        return new ApiResponse<>(true, "Email verified successfully", null);
//    }
//
//    @PostMapping("/verify-phone/{userId}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse<String> verifyUserPhone(@PathVariable Long userId) {
//        userVerificationService.verifyPhoneByAdmin(userId);
//        return new ApiResponse<>(true, "Phone verified successfully", null);
//    }
//
//    @PostMapping("/verify-both/{userId}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse<String> verifyBoth(@PathVariable Long userId) {
//        userVerificationService.verifyBothByAdmin(userId);
//        return new ApiResponse<>(true, "Email and phone verified successfully", null);
//    }
//
//    @PostMapping("/reject-verification/{userId}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse<String> rejectVerification(@PathVariable Long userId, @RequestBody Map<String, String> request) {
//        String reason = request.get("reason");
//        userVerificationService.rejectVerification(userId, reason);
//        return new ApiResponse<>(true, "Verification rejected", null);
//    }
//
//    @GetMapping("/stats")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse<Map<String, Long>> getVerificationStats() {
//        Map<String, Long> stats = Map.of(
//                "pendingEmailVerification", userVerificationService.getPendingEmailVerificationCount(),
//                "pendingPhoneVerification", userVerificationService.getPendingPhoneVerificationCount(),
//                "verifiedEmail", userVerificationService.getVerifiedEmailCount(),
//                "verifiedPhone", userVerificationService.getVerifiedPhoneCount(),
//                "fullyVerified", userVerificationService.getFullyVerifiedCount()
//        );
//        return new ApiResponse<>(true, "Verification statistics retrieved", stats);
//    }
//
//    private UserResponseDTO convertToDTO(User user) {
//        UserResponseDTO dto = new UserResponseDTO();
//        dto.setId(user.getId());
//        dto.setFirstName(user.getFirstName());
//        dto.setLastName(user.getLastName());
//        dto.setEmail(user.getEmail());
//        dto.setPhone(user.getPhone());
//        dto.setEmailVerified(user.getEmailVerified());
//        dto.setPhoneVerified(user.getPhoneVerified());
//        dto.setEmailVerifiedAt(user.getEmailVerifiedAt());
//        dto.setPhoneVerifiedAt(user.getPhoneVerifiedAt());
//        dto.setCreatedAt(user.getCreatedAt());
//        dto.setIsActive(user.getIsActive());
//        return dto;
//    }
//}
