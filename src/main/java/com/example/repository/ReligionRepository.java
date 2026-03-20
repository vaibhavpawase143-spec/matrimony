package com.example.repository;

import com.example.model.Religion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReligionRepository extends JpaRepository<Religion, Long> {

    List<Religion> findByIsActiveTrue();

    List<Religion> findByIsActiveFalse();

    List<Religion> findByAdminId(Long adminId);

    List<Religion> findByAdminIdAndIsActiveTrue(Long adminId);

    List<Religion> findByNameContainingIgnoreCase(String keyword);
}