package com.example.repository;

import com.example.model.Drinking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrinkingRepository extends JpaRepository<Drinking, Long> {

    // ==========================
    // DUPLICATE CHECK
    // ==========================

    boolean existsByValueIgnoreCaseAndDeletedAtIsNull(String value);

    // ==========================
    // GET
    // ==========================

    List<Drinking> findByDeletedAtIsNull();

    List<Drinking> findByDeletedAtIsNotNull();

    List<Drinking> findByIsActiveTrueAndDeletedAtIsNull();

    List<Drinking> findByIsActiveFalseAndDeletedAtIsNull();

    // ==========================
    // ADMIN
    // ==========================

    List<Drinking> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    List<Drinking> findByAdmin_IdAndDeletedAtIsNotNull(Long adminId);

    List<Drinking> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    // ==========================
    // SEARCH
    // ==========================

    List<Drinking> findByValueContainingIgnoreCaseAndDeletedAtIsNull(
            String keyword
    );

    List<Drinking> findByAdmin_IdAndValueContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );
}