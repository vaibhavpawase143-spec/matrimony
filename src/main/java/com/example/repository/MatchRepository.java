package com.example.repository;

import com.example.model.Match;
import com.example.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {

    // 🔥 Check if match exists (using User)
    boolean existsByUser1AndUser2(User user1, User user2);

    // 🔍 Find match by users (User object)
    Optional<Match> findByUser1AndUser2(User user1, User user2);

    // 🔥 IMPORTANT (for InterestServiceImpl integration)
    Optional<Match> findByUser1_IdAndUser2_Id(Long user1Id, Long user2Id);

    // 📄 Pagination (used in MatchServiceImpl)
    Page<Match> findByUser1OrUser2(User user1, User user2, Pageable pageable);

    // 📋 List (non-paginated)
    List<Match> findByUser1OrUser2(User user1, User user2);
}