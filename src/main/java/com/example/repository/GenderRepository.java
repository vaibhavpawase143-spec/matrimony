package com.example.repository;

import com.example.model.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GenderRepository extends JpaRepository<Gender, Long> {

    List<Gender> findByIsActiveTrue();

}