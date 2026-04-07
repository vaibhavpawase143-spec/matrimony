package com.example.repository;

import com.example.model.Match;
import com.example.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {

    boolean existsByUser1AndUser2(User user1, User user2);

    Optional<Match> findByUser1AndUser2(User user1, User user2);

    Page<Match> findByUser1OrUser2(User user1, User user2, Pageable pageable);

    List<Match> findByUser1OrUser2(User user1, User user2);
}