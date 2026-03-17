package com.example.repository;

import com.example.model.Shortlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShortlistRepository extends JpaRepository<Shortlist, Long> {

    // 🔍 Get all shortlisted profiles by user
    List<Shortlist> findByUserId(Long userId);

    // 🔍 Check if already shortlisted (VERY IMPORTANT)
    boolean existsByUserIdAndProfileId(Long userId, Long profileId);

    // 🔍 Remove shortlist (used in un-shortlist)
    void deleteByUserIdAndProfileId(Long userId, Long profileId);

    // 🔍 Get specific shortlist
    Shortlist findByUserIdAndProfileId(Long userId, Long profileId);
}