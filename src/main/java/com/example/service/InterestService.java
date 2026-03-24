package com.example.service;

import com.example.model.Interest;

import java.util.List;
import java.util.Optional;

public interface InterestService {

    Interest sendInterest(Interest interest);

    Interest updateStatus(Long id, String status);

    void delete(Long id);

    Optional<Interest> getById(Long id);

    List<Interest> getBySender(Long senderId);

    List<Interest> getBySenderAndStatus(Long senderId, String status);

    List<Interest> getByReceiver(Long receiverId);

    List<Interest> getByReceiverAndStatus(Long receiverId, String status);

    Optional<Interest> getBySenderAndReceiver(Long senderId, Long receiverId);

    List<Interest> getByStatus(String status);
}