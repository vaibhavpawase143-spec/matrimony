package com.example.controller.user;

import com.example.service.UserBlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/block")
@RequiredArgsConstructor
public class UserBlockController {

    private final UserBlockService userBlockService;

    @PostMapping
    public String block(@RequestParam Long blockerId,
                        @RequestParam Long blockedId) {

        userBlockService.blockUser(blockerId, blockedId);
        return "User blocked";
    }

    @DeleteMapping
    public String unblock(@RequestParam Long blockerId,
                          @RequestParam Long blockedId) {

        userBlockService.unblockUser(blockerId, blockedId);
        return "User unblocked";
    }
}