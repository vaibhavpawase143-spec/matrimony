package com.example.repository;

import com.example.model.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {

    // ✅ Find by sender
    List<Interest> findBySender_Id(Long senderId);

    // ✅ Find by receiver
    List<Interest> findByReceiver_Id(Long receiverId);

    // ✅ Find by sender + receiver
    Optional<Interest> findBySender_IdAndReceiver_Id(Long senderId, Long receiverId);

    // ✅ Find by receiver + status (PENDING)
    List<Interest> findByReceiver_IdAndIsActiveIgnoreCase(Long receiverId, String isActive);

    // ✅ Find by sender + status
    List<Interest> findBySender_IdAndIsActiveIgnoreCase(Long senderId, String isActive);

    // ✅ Find all by status
    List<Interest> findByIsActiveIgnoreCase(String isActive);
}