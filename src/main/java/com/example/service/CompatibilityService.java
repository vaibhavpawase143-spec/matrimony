package com.example.service;

import com.example.model.UserDetails;

public interface CompatibilityService {
    double calculateScore(UserDetails currentUser, UserDetails targetUser);
}