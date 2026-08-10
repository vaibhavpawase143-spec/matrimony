package com.example.repository;

import com.example.model.Match;
import com.example.model.User;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {

    // 🔥 BEST METHOD (USE THIS EVERYWHERE)
    boolean existsByUser1_IdAndUser2_Id(Long user1Id, Long user2Id);

    // 🔍 Get match by user IDs
    Optional<Match> findByUser1_IdAndUser2_Id(Long user1Id, Long user2Id);
    @Query("""
SELECT COUNT(m)
FROM Match m
WHERE m.user1.id = :userId
OR m.user2.id = :userId
""")
    long countMatches(
            @Param("userId") Long userId
    );

    // 📄 Pagination
    @Query("""
SELECT m

FROM Match m
WHERE m.user1 = :user1
OR m.user2 = :user2
""")
    Page<Match> findByUser1OrUser2(
            @Param("user1") User user1,
            @Param("user2") User user2,
            Pageable pageable
    );

    // 📋 List
    List<Match> findByUser1OrUser2(User user1, User user2);
}