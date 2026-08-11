package com.example.service;

import com.example.model.Profile;
import com.example.model.UserBlock;
import com.example.repository.ProfileRepository;
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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

@Service
@RequiredArgsConstructor
@Transactional
public class UserBlockService {

    // ✅ Missing Field
    private final UserBlockRepository userBlockRepository;
    private final UserRepository userRepository;

    private final UserPhotoRepository userPhotoRepository;
    private final ProfileRepository profileRepository;

    // ================= BLOCK USER =================
    @CacheEvict(
            value = "blockedUsers",
            key = "#blockerId"
    )
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
        block.setIsActive(true);   // ✅ IMPORTANT

        userBlockRepository.save(block);
    }

    // ================= UNBLOCK USER =================
    @CacheEvict(
            value = "blockedUsers",
            key = "#blockerId"
    )
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
    @Cacheable(
            value = "blockedUsers",
            key = "#blockerId"
    )
    public List<BlockedUserResponseDTO> getBlockedUsers(Long blockerId) {

        List<UserBlock> blocks =
                userBlockRepository.findByBlockerIdAndIsActiveTrue(blockerId);

        return blocks.stream().map(block -> {

            User user = userRepository.findById(block.getBlockedId())
                    .orElseThrow(() ->
                            new RuntimeException("Blocked user not found"));

            String photoUrl = profileRepository
                    .findByUserId(user.getId())
                    .map(Profile::getImageUrl)
                    .orElse(null);

            return BlockedUserResponseDTO.builder()
                    .blockedUserId(user.getId())
                    .fullName(user.getFullName())
                    .photoUrl(photoUrl)
                    .blockedDate(block.getUpdatedAt())   // ✅ updated_at
                    .build();

        }).toList();

    }
}