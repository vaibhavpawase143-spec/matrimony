package com.example.service;

import com.example.model.Height;

import java.util.List;
import java.util.Optional;

public interface HeightService {

    Height save(Height height);

    Optional<Height> getById(Long id);

    Optional<Height> getByHeight(String height);

    List<Height> getAll();

    List<Height> getAllActive();

    List<Height> getAllInactive();

    List<Height> getByAdmin(Long adminId);

    List<Height> getActiveByAdmin(Long adminId);

    List<Height> searchByHeight(String keyword);

    Height update(Long id, Height updated);

    void delete(Long id); // soft delete
}