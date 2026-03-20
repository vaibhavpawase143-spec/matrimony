package com.example.repository;

import com.example.model.BodyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BodyTypeRepository extends JpaRepository<BodyType, Long> {

    // 🔍 Find by value (example: Slim, Athletic)
    Optional<BodyType> findByValue(String value);

    // 🔍 Check duplicate before insert
    boolean existsByValue(String value);

    // ✅ STANDARD FIELD (isActive)
    List<BodyType> findByIsActiveTrue();

    List<BodyType> findByIsActiveFalse();

    // 🔍 Filter by admin
    List<BodyType> findByAdminId(Long adminId);

    // ✅ Active by admin
    List<BodyType> findByAdminIdAndIsActiveTrue(Long adminId);
}