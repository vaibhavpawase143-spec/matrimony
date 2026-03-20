package com.example.repository;

import com.example.model.Shortlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShortlistRepository extends JpaRepository<Shortlist, Long> {

    boolean existsByUser_IdAndProfile_Id(Long userId, Long profileId);

    Optional<Shortlist> findByUser_IdAndProfile_Id(Long userId, Long profileId);

    List<Shortlist> findByUser_Id(Long userId);
}