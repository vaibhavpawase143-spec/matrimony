package com.example.service;

import com.example.model.UserBlock;
import com.example.repository.UserBlockRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserBlockService {

    private final UserBlockRepository userBlockRepository;

    // ✅ BLOCK
    public void blockUser(Long blockerId, Long blockedId) {

        if (blockerId.equals(blockedId)) {
            throw new RuntimeException("You cannot block yourself");
        }

        // Already blocked
        if (userBlockRepository.existsByBlockerIdAndBlockedId(blockerId, blockedId)) {
            return;
        }

        UserBlock block = new UserBlock();
        block.setBlockerId(blockerId);
        block.setBlockedId(blockedId);

        userBlockRepository.save(block);
    }

    // ✅ UNBLOCK
    public void unblockUser(Long blockerId, Long blockedId) {

        userBlockRepository.deleteByBlockerIdAndBlockedId(blockerId, blockedId);
    }

    // 🔥 MOST IMPORTANT
    public boolean isBlocked(Long user1, Long user2) {

        return userBlockRepository.existsByBlockerIdAndBlockedId(user1, user2)
                || userBlockRepository.existsByBlockerIdAndBlockedId(user2, user1);
    }
}