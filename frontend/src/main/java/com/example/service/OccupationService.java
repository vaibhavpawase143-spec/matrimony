package com.example.service;

import com.example.model.Occupation;

import java.util.List;
import java.util.Optional;

public interface OccupationService {

    Occupation create(Occupation occupation);

    Occupation update(Long id, Occupation occupation);

    void delete(Long id);

    Optional<Occupation> getById(Long id);

    List<Occupation> getAll();   // ✅ important

    List<Occupation> getByAdmin(Long adminId);

    List<Occupation> getActiveByAdmin(Long adminId);

    List<Occupation> getInactiveByAdmin(Long adminId);

    Optional<Occupation> getByNameAndAdmin(String name, Long adminId);

    boolean existsByNameAndAdmin(String name, Long adminId);

    List<Occupation> searchByAdmin(Long adminId, String keyword);
}