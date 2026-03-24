package com.example.service;

import com.example.model.Shortlist;

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

    // 🔥 Who shortlisted a profile
    List<Shortlist> getByProfile(Long profileId);

    // ❌ Remove from shortlist
    void removeFromShortlist(Long userId, Long profileId);

    // 🔍 Get all
    List<Shortlist> getAll();
}