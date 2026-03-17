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

    // 🔍 Get all active body types
    List<BodyType> findByStatusTrue();

    // 🔍 Get all inactive body types
    List<BodyType> findByStatusFalse();

    // 🔍 Filter by admin
    List<BodyType> findByAdminId(Long adminId);

    // 🔍 Active body types by admin
    List<BodyType> findByAdminIdAndStatusTrue(Long adminId);
}