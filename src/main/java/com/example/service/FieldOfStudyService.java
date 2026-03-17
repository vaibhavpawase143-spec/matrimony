package com.example.service;

import com.example.model.FieldOfStudy;

import java.util.List;
import java.util.Optional;

public interface FieldOfStudyService {

    FieldOfStudy save(FieldOfStudy fieldOfStudy);

    Optional<FieldOfStudy> getById(Long id);

    Optional<FieldOfStudy> getByName(String name);

    List<FieldOfStudy> getAll();

    List<FieldOfStudy> getAllActive();

    List<FieldOfStudy> getAllInactive();

    List<FieldOfStudy> getByAdmin(Long adminId);

    List<FieldOfStudy> getActiveByAdmin(Long adminId);

    List<FieldOfStudy> searchByName(String keyword);

    FieldOfStudy update(Long id, FieldOfStudy updated);

    void delete(Long id); // soft delete
}