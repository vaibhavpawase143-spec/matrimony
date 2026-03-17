package com.example.service;

import com.example.model.Qualification;

import java.util.List;
import java.util.Optional;

public interface QualificationService {

    Qualification create(Qualification qualification);

    List<Qualification> getAll();

    List<Qualification> getAllActive();

    List<Qualification> getAllInactive();

    Optional<Qualification> getById(Long id);

    Qualification update(Long id, Qualification qualification);

    void delete(Long id);

    List<Qualification> search(String keyword);

    List<Qualification> getByAdmin(Long adminId);

    List<Qualification> getActiveByAdmin(Long adminId);
}