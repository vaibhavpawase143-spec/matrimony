package com.example.service;

import com.example.model.Smoking;

import java.util.List;
import java.util.Optional;

public interface SmokingService {

    Smoking create(Smoking smoking);

    List<Smoking> getAll();

    List<Smoking> getAllActive();

    List<Smoking> getAllInactive();

    Optional<Smoking> getById(Long id);

    Smoking update(Long id, Smoking smoking);

    void delete(Long id);

    List<Smoking> search(String keyword);

    List<Smoking> getByAdmin(Long adminId);

    List<Smoking> getActiveByAdmin(Long adminId);
}