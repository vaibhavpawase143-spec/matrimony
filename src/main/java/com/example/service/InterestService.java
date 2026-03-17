package com.example.service;

import com.example.model.Interest;

import java.util.List;

public interface InterestService {

    Interest sendInterest(Long senderId, Long receiverId);

    List<Interest> getSentInterests(Long senderId);

    List<Interest> getReceivedInterests(Long receiverId);

    List<Interest> getReceivedPending(Long receiverId);

    Interest acceptInterest(Long senderId, Long receiverId);

    Interest rejectInterest(Long senderId, Long receiverId);

    void cancelInterest(Long senderId, Long receiverId);
}