package com.example.service;

import com.example.model.Interest;

import java.util.List;

public interface InterestService {

    // ✅ Create new interest
    Interest create(Long senderId, Long receiverId);

    // ✅ Get all interests
    List<Interest> getAll();

    // ✅ Get by ID
    Interest getById(Long id);

    // ✅ Get by sender
    List<Interest> getBySender(Long senderId);

    // ✅ Get by receiver
    List<Interest> getByReceiver(Long receiverId);

    // ✅ Update isActive
    Interest updateisActive(Long id, String isActive);

    // ✅ Delete interest
    void delete(Long id);

    Interest sendInterest(Long senderId, Long receiverId);

    List<Interest> getSentInterests(Long senderId);

    List<Interest> getReceivedInterests(Long receiverId);

    List<Interest> getReceivedPending(Long receiverId);

    Interest acceptInterest(Long senderId, Long receiverId);

    Interest rejectInterest(Long senderId, Long receiverId);

    void cancelInterest(Long senderId, Long receiverId);
}