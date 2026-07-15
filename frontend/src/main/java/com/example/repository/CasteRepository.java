package com.example.repository;

import com.example.model.Caste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CasteRepository extends JpaRepository<Caste, Long> {

    // 🔍 Find (optional use)
    Optional<Caste> findByNameIgnoreCase(String name);

    // 🔒 Secure fetch (VERY IMPORTANT)
    Optional<Caste> findByIdAndAdminId(Long id, Long adminId);

    // 🔥 Duplicate check (admin + religion scoped)
    boolean existsByNameIgnoreCaseAndReligionIdAndAdminId(
            String name, Long religionId, Long adminId
    );

    // 🔍 Admin-based filtering
    List<Caste> findByAdminId(Long adminId);

    List<Caste> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Religion + Admin
    List<Caste> findByReligionIdAndAdminId(Long religionId, Long adminId);

    List<Caste> findByReligionIdAndAdminIdAndIsActiveTrue(Long religionId, Long adminId);

    // 🔍 Search (admin scoped)
    List<Caste> findByNameContainingIgnoreCaseAndAdminId(String keyword, Long adminId);
}