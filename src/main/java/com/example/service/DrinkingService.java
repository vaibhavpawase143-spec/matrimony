package com.example.service;

import com.example.model.Drinking;
import java.util.List;

public interface DrinkingService {

    // Get all
    List<Drinking> getAll();

    // Get active
    List<Drinking> getActive();

    // Get by ID
    Drinking getById(Long id);

    // Create
    Drinking create(Drinking drinking);

    // Update
    Drinking update(Long id, Drinking updated);

    // Delete
    void delete(Long id);

    // Get by admin
    List<Drinking> getByAdmin(Long adminId);

    // Get active by admin
    List<Drinking> getActiveByAdmin(Long adminId);

    // Search
    List<Drinking> search(String keyword);
}