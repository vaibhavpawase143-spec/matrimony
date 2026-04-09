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

    // 🔍 Prevent duplicate shortlist
    boolean existsByUser_IdAndProfile_Id(Long userId, Long profileId);

    // 🔍 Get specific shortlist
    Optional<Shortlist> findByUser_IdAndProfile_Id(Long userId, Long profileId);

    // 🔥 FIX: Fetch user eagerly (prevents lazy error)
    @Query("SELECT s FROM Shortlist s JOIN FETCH s.user WHERE s.user.id = :userId")
    List<Shortlist> findByUser_Id(@Param("userId") Long userId);

    // 🔥 FIX: Fetch user eagerly
    @Query("SELECT s FROM Shortlist s JOIN FETCH s.user WHERE s.profile.id = :profileId")
    List<Shortlist> findByProfile_Id(@Param("profileId") Long profileId);

    // 🔥 FIX: Also fetch user for getAll()
    @Query("SELECT s FROM Shortlist s JOIN FETCH s.user")
    List<Shortlist> findAll();

    // 🔥 Remove from shortlist
    void deleteByUser_IdAndProfile_Id(Long userId, Long profileId);
}