package com.example.service;

import com.example.model.Weight;

import java.util.List;
import java.util.Optional;

public interface WeightService {

    Weight create(Weight weight);

    List<Weight> getAll();

    List<Weight> getAllActive();

    Optional<Weight> getById(Long id);

    Weight update(Long id, Weight weight);

    void delete(Long id);

    List<Weight> getByAdmin(Long adminId);

    List<Weight> getActiveByAdmin(Long adminId);

    List<Weight> search(String keyword);
}