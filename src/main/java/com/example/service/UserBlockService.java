package com.example.service;

import com.example.model.UserBlock;
import com.example.repository.UserBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.dto.response.BlockedUserResponseDTO;
import com.example.model.User;
import com.example.model.UserPhoto;
import com.example.repository.UserPhotoRepository;
import com.example.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserBlockService {

    // ✅ Missing Field
    private final UserBlockRepository userBlockRepository;
    private final UserRepository userRepository;

    private final UserPhotoRepository userPhotoRepository;

    // ================= BLOCK USER =================
    public void blockUser(Long blockerId, Long blockedId) {

        if (blockerId.equals(blockedId)) {
            throw new RuntimeException("You cannot block yourself");
        }

        Optional<UserBlock> existing =
                userBlockRepository.findByBlockerIdAndBlockedId(
                        blockerId,
                        blockedId
                );

        if (existing.isPresent()) {

            UserBlock block = existing.get();

            if (Boolean.TRUE.equals(block.getIsActive())) {
                throw new RuntimeException("User already blocked");
            }

            // Reactivate
            block.setIsActive(true);

            userBlockRepository.save(block);

            return;
        }

        UserBlock block = new UserBlock();

        block.setBlockerId(blockerId);

        block.setBlockedId(blockedId);

        userBlockRepository.save(block);
    }

    // ================= UNBLOCK USER =================
    public void unblockUser(Long blockerId, Long blockedId) {

        UserBlock block = userBlockRepository
                .findByBlockerIdAndBlockedId(
                        blockerId,
                        blockedId
                )
                .orElseThrow(() ->
                        new RuntimeException("Block record not found"));

        block.setIsActive(false);

        userBlockRepository.save(block);
    }

    // ================= CHECK BLOCK =================
    public boolean isBlocked(Long user1, Long user2) {

        return userBlockRepository
                .existsByBlockerIdAndBlockedIdAndIsActiveTrue(
                        user1,
                        user2
                )

                ||

                userBlockRepository
                        .existsByBlockerIdAndBlockedIdAndIsActiveTrue(
                                user2,
                                user1
                        );
    }

    // ================= MY BLOCKED USERS =================
    public List<BlockedUserResponseDTO> getBlockedUsers(Long blockerId) {

        List<UserBlock> blocks =
                userBlockRepository.findByBlockerIdAndIsActiveTrue(blockerId);

        return blocks.stream().map(block -> {

            User user = userRepository.findById(block.getBlockedId())
                    .orElseThrow(() ->
                            new RuntimeException("Blocked user not found"));

            String photoUrl = userPhotoRepository
                    .findFirstByUserIdAndPrimaryPhotoTrue(user.getId())
                    .map(UserPhoto::getPhotoUrl)
                    .orElse(null);

            return BlockedUserResponseDTO.builder()
                    .blockedUserId(user.getId())
                    .fullName(user.getFullName())
                    .photoUrl(photoUrl)
                    .blockedDate(block.getCreatedAt())
                    .build();

        }).toList();

    }
}