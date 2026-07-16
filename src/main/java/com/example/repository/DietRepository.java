package com.example.repository;

import com.example.model.Diet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DietRepository extends JpaRepository<Diet, Long> {

    // ==========================
    // DUPLICATE CHECK
    // ==========================

    boolean existsByNameIgnoreCaseAndDeletedAtIsNull(String name);

    // ==========================
    // GET
    // ==========================

    List<Diet> findByDeletedAtIsNull();

    List<Diet> findByDeletedAtIsNotNull();

    List<Diet> findByIsActiveTrueAndDeletedAtIsNull();

    List<Diet> findByIsActiveFalseAndDeletedAtIsNull();

    // ==========================
    // ADMIN
    // ==========================

    List<Diet> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    List<Diet> findByAdmin_IdAndDeletedAtIsNotNull(Long adminId);

    List<Diet> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    // ==========================
    // SEARCH
    // ==========================

    List<Diet> findByNameContainingIgnoreCaseAndDeletedAtIsNull(String keyword);

    List<Diet> findByAdmin_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );
}