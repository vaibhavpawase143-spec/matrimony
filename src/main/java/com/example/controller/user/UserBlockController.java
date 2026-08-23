package com.example.controller.user;

import com.example.dto.response.BlockedUserResponseDTO;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.security.SecurityUtils;
import com.example.service.UserBlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/block")
@RequiredArgsConstructor
public class UserBlockController {

    private final UserBlockService userBlockService;
    private final UserRepository userRepository;

    private User getAuthenticatedUser() {
        String email = SecurityUtils.getCurrentUsername();
        if (email == null) {
            throw new AccessDeniedException("User not authenticated");
        }
        return userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new AccessDeniedException("User not found"));
    }

    private Long resolveEffectiveBlockerId(Long blockerId) {
        User currentUser = getAuthenticatedUser();
        boolean isAdmin = currentUser.getRoles() != null && currentUser.getRoles().stream()
                .anyMatch(r -> r.getName().contains("ADMIN"));
        if (blockerId != null && !blockerId.equals(currentUser.getId()) && !isAdmin) {
            throw new AccessDeniedException("Access denied: You cannot perform block operations for another user");
        }
        return blockerId != null ? blockerId : currentUser.getId();
    }

    // ✅ BLOCK USER
    @PostMapping
    public String blockUser(
            @RequestParam(required = false) Long blockerId,
            @RequestParam Long blockedId
    ) {
        Long effectiveBlockerId = resolveEffectiveBlockerId(blockerId);
        userBlockService.blockUser(effectiveBlockerId, blockedId);
        return "User blocked successfully";
    }

    @GetMapping("/my-blocked-users")
    public List<BlockedUserResponseDTO> getMyBlockedUsers(
            @RequestParam(required = false) Long blockerId
    ) {
        Long effectiveBlockerId = resolveEffectiveBlockerId(blockerId);
        return userBlockService.getBlockedUsers(effectiveBlockerId);
    }

    // ✅ UNBLOCK USER
    @DeleteMapping
    public String unblockUser(
            @RequestParam(required = false) Long blockerId,
            @RequestParam Long blockedId
    ) {
        Long effectiveBlockerId = resolveEffectiveBlockerId(blockerId);
        userBlockService.unblockUser(effectiveBlockerId, blockedId);
        return "User unblocked successfully";
    }

    // ✅ CHECK BLOCK
    @GetMapping("/check")
    public boolean isBlocked(
            @RequestParam Long user1,
            @RequestParam Long user2
    ) {
        return userBlockService.isBlocked(user1, user2);
    }
}