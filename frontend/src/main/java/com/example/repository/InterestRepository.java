package com.example.repository;

import com.example.model.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {

    List<Interest> findBySender_Id(Long senderId);

    List<Interest> findByReceiver_Id(Long receiverId);

    Optional<Interest> findBySender_IdAndReceiver_Id(Long senderId, Long receiverId);

    List<Interest> findByReceiver_IdAndStatusIgnoreCase(Long receiverId, String status);

    List<Interest> findBySender_IdAndStatusIgnoreCase(Long senderId, String status);

    List<Interest> findByStatusIgnoreCase(String status);
}