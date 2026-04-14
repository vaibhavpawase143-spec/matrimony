package com.example.repository;

import com.example.model.Shortlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShortlistRepository extends JpaRepository<Shortlist, Long> {

    // 🔍 Prevent duplicate shortlist (used in service)
    boolean existsByUser_IdAndProfile_Id(Long userId, Long profileId);

    // 🔍 Get specific shortlist
    Optional<Shortlist> findByUser_IdAndProfile_Id(Long userId, Long profileId);

    // ✅ Get only ACTIVE shortlist by user (with user + profile to avoid lazy error)
    @Query("""
        SELECT s FROM Shortlist s
        JOIN FETCH s.user
        JOIN FETCH s.profile
        WHERE s.user.id = :userId
        AND s.isActive = true
    """)
    List<Shortlist> findByUser_IdAndIsActiveTrue(@Param("userId") Long userId);

    // ✅ Get who shortlisted a profile (ACTIVE only)
    @Query("""
        SELECT s FROM Shortlist s
        JOIN FETCH s.user
        JOIN FETCH s.profile
        WHERE s.profile.id = :profileId
        AND s.isActive = true
    """)
    List<Shortlist> findByProfile_IdAndIsActiveTrue(@Param("profileId") Long profileId);

    // ✅ Get all ACTIVE (with relations)
    @Query("""
        SELECT s FROM Shortlist s
        JOIN FETCH s.user
        JOIN FETCH s.profile
        WHERE s.isActive = true
    """)
    List<Shortlist> findByIsActiveTrue();
}