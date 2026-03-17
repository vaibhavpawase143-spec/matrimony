package com.example.repository;

import com.example.model.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {

    // 🔍 Interests sent by user
    List<Interest> findBySenderId(Long senderId);

    // 🔍 Interests received by user
    List<Interest> findByReceiverId(Long receiverId);

    // 🔍 Check if already sent (VERY IMPORTANT)
    boolean existsBySenderIdAndReceiverId(Long senderId, Long receiverId);

    // 🔍 Get by sender + status
    List<Interest> findBySenderIdAndStatus(Long senderId, String status);

    // 🔍 Get by receiver + status
    List<Interest> findByReceiverIdAndStatus(Long receiverId, String status);

    // 🔍 Get specific interest
    Interest findBySenderIdAndReceiverId(Long senderId, Long receiverId);
}