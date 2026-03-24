package com.example.repository;

import com.example.model.Shortlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShortlistRepository extends JpaRepository<Shortlist, Long> {

    // 🔍 Prevent duplicate shortlist
    boolean existsByUser_IdAndProfile_Id(Long userId, Long profileId);

    // 🔍 Get specific shortlist
    Optional<Shortlist> findByUser_IdAndProfile_Id(Long userId, Long profileId);

    // 🔍 Get all shortlisted profiles by user
    List<Shortlist> findByUser_Id(Long userId);

    // 🔥 Who shortlisted a profile
    List<Shortlist> findByProfile_Id(Long profileId);

    // 🔥 Remove from shortlist
    void deleteByUser_IdAndProfile_Id(Long userId, Long profileId);
}