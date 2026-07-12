package com.example.service;

public interface MatchNotificationService {

    void generateForPreferenceUpdate(Long userId);

    void generateForProfileUpdate(Long userId);

}