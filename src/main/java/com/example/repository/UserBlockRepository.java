package com.example.repository;

import com.example.model.UserBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBlockRepository extends JpaRepository<UserBlock, Long> {

    Optional<UserBlock> findByBlockerIdAndBlockedId(
            Long blockerId,
            Long blockedId
    );

    boolean existsByBlockerIdAndBlockedIdAndIsActiveTrue(
            Long blockerId,
            Long blockedId
    );

    List<UserBlock> findByBlockerIdAndIsActiveTrue(
            Long blockerId
    );

    boolean existsByBlockedIdAndIsActiveTrue(
            Long blockedId
    );

    @Query("""
            SELECT ub.blockedId
            FROM UserBlock ub
            WHERE ub.blockerId = :blockerId
            AND ub.isActive = true
            """)
    List<Long> findBlockedUserIds(
            Long blockerId
    );

    List<UserBlock> findByBlockedIdAndIsActiveTrue(
            Long blockedId
    );

    List<UserBlock> findAllByIsActiveTrue();
}