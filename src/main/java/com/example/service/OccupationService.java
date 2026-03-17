package com.example.service;

import com.example.model.Occupation;

import java.util.List;
import java.util.Optional;

public interface OccupationService {

    Occupation create(Occupation occupation);

    List<Occupation> getAll();

    List<Occupation> getAllActive();

    List<Occupation> getAllInactive();

    Optional<Occupation> getById(Long id);

    Occupation update(Long id, Occupation occupation);

    void delete(Long id);

    List<Occupation> search(String keyword);

    List<Occupation> getByAdmin(Long adminId);

    List<Occupation> getActiveByAdmin(Long adminId);
}