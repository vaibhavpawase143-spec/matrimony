package com.example.repository;

import com.example.model.Complexion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComplexionRepository extends JpaRepository<Complexion, Long> {

    Optional<Complexion> findByValue(String value);

    boolean existsByValue(String value);

    List<Complexion> findByIsActiveTrue();

    List<Complexion> findByIsActiveFalse();

    List<Complexion> findByAdminId(Long adminId);

    List<Complexion> findByAdminIdAndIsActiveTrue(Long adminId);

    List<Complexion> findByValueContainingIgnoreCase(String keyword);
}