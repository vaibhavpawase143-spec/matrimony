package com.example.repository;

import com.example.model.Caste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CasteRepository extends JpaRepository<Caste, Long> {

    // 🔍 Find by name
    Optional<Caste> findByName(String name);

    // 🔍 Check duplicate
    boolean existsByName(String name);

    // 🔍 Get all active castes
    List<Caste> findByStatusTrue();

    // 🔍 Get castes by religion (IMPORTANT relation)
    List<Caste> findByReligionId(Long religionId);

    // 🔍 Active castes by religion (for dropdown)
    List<Caste> findByReligionIdAndStatusTrue(Long religionId);

    // 🔍 Filter by admin
    List<Caste> findByAdminId(Long adminId);

    // 🔍 Search caste by name
    List<Caste> findByNameContainingIgnoreCase(String keyword);
}
