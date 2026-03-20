package com.example.repository;

import com.example.model.Weight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeightRepository extends JpaRepository<Weight, Long> {


    // Find all active weights
    List<Weight> findByIsActiveTrue();

    // Find by value (example: "60 kg")
    List<Weight> findByValue(String value);

    // Find by admin id
    List<Weight> findByAdminId(Long adminId);

    // Find active weights by admin
    List<Weight> findByAdminIdAndIsActiveTrue(Long adminId);


}
