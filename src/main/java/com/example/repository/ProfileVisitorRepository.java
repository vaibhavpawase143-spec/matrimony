package com.example.repository;

import com.example.model.ProfileVisitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileVisitorRepository
        extends JpaRepository<ProfileVisitor, Long> {

    List<ProfileVisitor>
    findByVisitedUser_IdOrderByViewedAtDesc(
            Long userId
    );

    Optional<ProfileVisitor>
    findByVisitor_IdAndVisitedUser_Id(
            Long visitorId,
            Long visitedUserId
    );
}