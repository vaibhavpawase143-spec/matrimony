package com.example.repository;

import com.example.model.Drinking;
import org.springframework.data.jpa.repository.EntityGraph;
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

    @EntityGraph(attributePaths = "admin")
    List<Drinking> findByDeletedAtIsNull();

    List<Drinking> findByDeletedAtIsNotNull();

    @EntityGraph(attributePaths = "admin")
    List<Drinking> findByIsActiveTrueAndDeletedAtIsNull();

    List<Drinking> findByIsActiveFalseAndDeletedAtIsNull();

    // ==========================
    // ADMIN
    // ==========================

    @EntityGraph(attributePaths = "admin")
    List<Drinking> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    List<Drinking> findByAdmin_IdAndDeletedAtIsNotNull(Long adminId);

    @EntityGraph(attributePaths = "admin")
    List<Drinking> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    // ==========================
    // SEARCH
    // ==========================

    @EntityGraph(attributePaths = "admin")
    List<Drinking> findByValueContainingIgnoreCaseAndDeletedAtIsNull(
            String keyword
    );

    @EntityGraph(attributePaths = "admin")
    List<Drinking> findByAdmin_IdAndValueContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );
}