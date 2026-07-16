package com.example.repository;

import com.example.model.Swipe;
import com.example.model.SwipeType;
import com.example.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SwipeRepository extends JpaRepository<Swipe, Long> {

    // 🔥 All swipes by user
    List<Swipe> findByFromUser(User user);

    // 🔥 Check already swiped
    boolean existsByFromUserAndToUser(User from, User to);

    // 🔥 Get specific swipe (important for match logic)
    Optional<Swipe> findByFromUserAndToUser(User from, User to);

    // 🔥 Get all likes (AI learning)
    List<Swipe> findByFromUserAndType(User user, SwipeType type);

    // 🔥 Get users who liked me (for mutual match later)
    List<Swipe> findByToUserAndType(User user, SwipeType type);

    // 🔥 Count swipes (for daily limits later)
    long countByFromUser(User user);
}