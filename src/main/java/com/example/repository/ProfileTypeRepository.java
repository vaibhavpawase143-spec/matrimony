package com.example.repository;

import com.example.model.ProfileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileTypeRepository extends JpaRepository<ProfileType, Long> {

    // 🔍 Find by name (admin-specific)
    Optional<ProfileType> findByNameIgnoreCaseAndAdminId(String name, Long adminId);

    // 🔍 Check duplicate (admin-specific)
    boolean existsByNameIgnoreCaseAndAdminId(String name, Long adminId);

    // 🔍 Get all records by admin
    List<ProfileType> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<ProfileType> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Inactive records by admin
    List<ProfileType> findByAdminIdAndIsActiveFalse(Long adminId);

    // 🔍 Search (admin + keyword)
    List<ProfileType> findByAdminIdAndNameContainingIgnoreCase(Long adminId, String keyword);
}