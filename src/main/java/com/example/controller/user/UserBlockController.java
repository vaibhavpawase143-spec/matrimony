package com.example.controller.user;

import com.example.service.UserBlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/block")
@RequiredArgsConstructor
public class UserBlockController {

    private final UserBlockService userBlockService;

    // ✅ BLOCK USER
    @PostMapping
    public String blockUser(
            @RequestParam Long blockerId,
            @RequestParam Long blockedId
    ) {
        userBlockService.blockUser(blockerId, blockedId);
        return "User blocked successfully";
    }

    // ✅ UNBLOCK USER
    @DeleteMapping
    public String unblockUser(
            @RequestParam Long blockerId,
            @RequestParam Long blockedId
    ) {
        userBlockService.unblockUser(blockerId, blockedId);
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