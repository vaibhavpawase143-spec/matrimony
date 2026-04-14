package com.example.repository;

import com.example.model.UserBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserBlockRepository extends JpaRepository<UserBlock, Long> {

    // 🔍 Find block relation (used in service)
    Optional<UserBlock> findByBlockerIdAndBlockedId(Long blockerId, Long blockedId);

    // 🔍 Check active block (IMPORTANT)
    boolean existsByBlockerIdAndBlockedIdAndIsActiveTrue(Long blockerId, Long blockedId);

    // 🔍 Check if user is blocked by anyone
    boolean existsByBlockedIdAndIsActiveTrue(Long blockedId);

}