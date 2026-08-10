package com.example.repository;

import com.example.model.ProfileVisitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileVisitorRepository
        extends JpaRepository<ProfileVisitor, Long> {

    List<ProfileVisitor> findByVisitedUser_IdOrderByViewedAtDesc(
            Long userId
    );

    Optional<ProfileVisitor> findByVisitor_IdAndVisitedUser_Id(
            Long visitorId,
            Long visitedUserId
    );

    // ADD THIS
    long countByVisitedUser_Id(Long userId);

}