package com.example.service;

import com.example.model.Shortlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ShortlistService {

    // ✅ Add to shortlist
    Shortlist addToShortlist(Shortlist shortlist);

    // 🔍 Get by ID
    Optional<Shortlist> getById(Long id);

    // 🔍 Get specific shortlist (user + profile)
    Optional<Shortlist> getByUserAndProfile(Long userId, Long profileId);

    // 🔍 Get all shortlisted profiles by user
    List<Shortlist> getByUser(Long userId);

    // 🔍 Get paginated shortlisted profiles by user
    Page<Shortlist> getByUser(Long userId, Pageable pageable);

    // 🔥 Who shortlisted a profile
    List<Shortlist> getByProfile(Long profileId);

    // ❌ Remove from shortlist
    void removeFromShortlist(Long userId, Long profileId);

    // 🔍 Get all
    List<Shortlist> getAll();
}