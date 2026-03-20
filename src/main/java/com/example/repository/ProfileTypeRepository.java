package com.example.repository;

import com.example.model.ProfileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileTypeRepository extends JpaRepository<ProfileType, Long> {

    // 🔍 Get all active records
    List<ProfileType> findByIsActiveTrue();

    // 🔍 Get all inactive records
    List<ProfileType> findByIsActiveFalse();

    // 🔍 Filter by admin
    List<ProfileType> findByAdminId(Long adminId);

    // 🔍 Search
    List<ProfileType> findByNameContainingIgnoreCase(String keyword);
}