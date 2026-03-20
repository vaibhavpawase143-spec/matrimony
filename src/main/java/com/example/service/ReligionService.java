package com.example.service;

import com.example.model.Religion;

import java.util.List;
import java.util.Optional;

public interface ReligionService {

    Religion create(Religion religion);

    List<Religion> getAll();

    List<Religion> getAllActive();

    List<Religion> getAllInactive();

    Optional<Religion> getById(Long id);

    Religion update(Long id, Religion religion);

    void delete(Long id); // soft delete

    List<Religion> search(String keyword);

    List<Religion> getByAdmin(Long adminId);

    List<Religion> getActiveByAdmin(Long adminId);
}