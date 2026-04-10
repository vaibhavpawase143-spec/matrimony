package com.example.service;

import com.example.model.UserBlock;
import com.example.repository.UserBlockRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional   // 🔥 REQUIRED for delete() to work
public class UserBlockService {

    private final UserBlockRepository userBlockRepository;

    // ✅ BLOCK USER
    public void blockUser(Long blockerId, Long blockedId) {

        if (blockerId.equals(blockedId)) {
            throw new RuntimeException("You cannot block yourself");
        }

        // Already blocked → ignore
        if (userBlockRepository.existsByBlockerIdAndBlockedId(blockerId, blockedId)) {
            return;
        }

        UserBlock block = new UserBlock();
        block.setBlockerId(blockerId);
        block.setBlockedId(blockedId);

        userBlockRepository.save(block);
    }

    // ✅ UNBLOCK USER (FIXED 🔥)
    public void unblockUser(Long blockerId, Long blockedId) {

        boolean exists = userBlockRepository
                .existsByBlockerIdAndBlockedId(blockerId, blockedId);

        if (!exists) {
            throw new RuntimeException("Block record not found");
        }

        userBlockRepository.deleteByBlockerIdAndBlockedId(blockerId, blockedId);
    }

    // ✅ CHECK BLOCK (BOTH SIDES)
    public boolean isBlocked(Long user1, Long user2) {

        return userBlockRepository.existsByBlockerIdAndBlockedId(user1, user2)
                || userBlockRepository.existsByBlockerIdAndBlockedId(user2, user1);
    }
}