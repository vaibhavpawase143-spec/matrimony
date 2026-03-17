package com.example.service;

import com.example.model.Shortlist;

import java.util.List;

public interface ShortlistService {

    Shortlist addToShortlist(Long userId, Long profileId);

    void removeFromShortlist(Long userId, Long profileId);

    List<Shortlist> getUserShortlist(Long userId);

    boolean isShortlisted(Long userId, Long profileId);
}