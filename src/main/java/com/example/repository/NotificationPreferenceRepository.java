package com.example.repository;

import com.example.model.NotificationPreference;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationPreferenceRepository
        extends JpaRepository<NotificationPreference, Long> {

    Optional<NotificationPreference> findByUser(User user);

    Optional<NotificationPreference> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}