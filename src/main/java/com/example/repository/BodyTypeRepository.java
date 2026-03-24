package com.example.repository;

import com.example.model.BodyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BodyTypeRepository extends JpaRepository<BodyType, Long> {

    // 🔍 Find by value
    Optional<BodyType> findByValue(String value);

    // 🔥 Case-insensitive (important for real-world data)
    Optional<BodyType> findByValueIgnoreCase(String value);

    // ✅ Duplicate check
    boolean existsByValue(String value);

    boolean existsByValueIgnoreCase(String value);

    // 🔍 Active / Inactive
    List<BodyType> findByIsActiveTrue();
    List<BodyType> findByIsActiveFalse();

    // 🔍 Admin-based filtering
    List<BodyType> findByAdminId(Long adminId);

    // ✅ Active by admin
    List<BodyType> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔥 Combined filter (best practice)
    List<BodyType> findByAdminIdAndValueIgnoreCase(Long adminId, String value);
}