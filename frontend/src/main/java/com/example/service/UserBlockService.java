package com.example.service;

import com.example.model.UserBlock;
import com.example.repository.UserBlockRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserBlockService {

    private final UserBlockRepository userBlockRepository;

    // ✅ BLOCK USER (soft + reactivation)
    public void blockUser(Long blockerId, Long blockedId) {

        if (blockerId.equals(blockedId)) {
            throw new RuntimeException("You cannot block yourself");
        }

        Optional<UserBlock> existing =
                userBlockRepository.findByBlockerIdAndBlockedId(blockerId, blockedId);

        if (existing.isPresent()) {
            UserBlock block = existing.get();

            if (Boolean.TRUE.equals(block.getIsActive())) {
                throw new RuntimeException("User already blocked");
            }

            // 🔥 Reactivate
            block.setIsActive(true);
            userBlockRepository.save(block);
            return;
        }

        // ✅ New block
        UserBlock block = new UserBlock();
        block.setBlockerId(blockerId);
        block.setBlockedId(blockedId);

        userBlockRepository.save(block);
    }

    // ✅ UNBLOCK USER (soft delete)
    public void unblockUser(Long blockerId, Long blockedId) {

        UserBlock block = userBlockRepository
                .findByBlockerIdAndBlockedId(blockerId, blockedId)
                .orElseThrow(() -> new RuntimeException("Block record not found"));

        block.setIsActive(false);
        userBlockRepository.save(block);
    }

    // ✅ CHECK BLOCK (BOTH SIDES)
    public boolean isBlocked(Long user1, Long user2) {

        return userBlockRepository
                .existsByBlockerIdAndBlockedIdAndIsActiveTrue(user1, user2)
                || userBlockRepository
                .existsByBlockerIdAndBlockedIdAndIsActiveTrue(user2, user1);
    }
}