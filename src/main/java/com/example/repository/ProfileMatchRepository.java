package com.example.repository;

import com.example.model.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProfileMatchRepository extends JpaRepository<Profile, Long> {

    @Query("""
        SELECT p
        FROM Profile p
        JOIN FETCH p.user u
        LEFT JOIN FETCH p.religion r
        LEFT JOIN FETCH p.caste c
        LEFT JOIN FETCH p.city ci

        WHERE p.isActive = true
        AND p.profileCompleted = true
        AND u.isActive = true
        AND u.isDeleted = false
        AND u.id <> :userId

        AND (:religionId IS NULL OR r.id = :religionId)
        AND (:casteId IS NULL OR c.id = :casteId)
        AND (:cityId IS NULL OR ci.id = :cityId)

        ORDER BY
        p.isPremium DESC,
        p.boostScore DESC,
        p.createdAt DESC
    """)
    Page<Profile> findMatches(
            @Param("userId") Long userId,
            @Param("religionId") Long religionId,
            @Param("casteId") Long casteId,
            @Param("cityId") Long cityId,
            Pageable pageable
    );
}