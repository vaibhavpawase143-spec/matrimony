package com.example.service;

import com.example.model.Drinking;
import java.util.List;

public interface DrinkingService {

<<<<<<< HEAD
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
=======
    List<Drinking> getAll();

    List<Drinking> getActive();

    Drinking getById(Long id);

    Drinking create(Drinking drinking);

    Drinking update(Long id, Drinking updated);

    void delete(Long id);

    List<Drinking> getByAdmin(Long adminId);

    List<Drinking> getActiveByAdmin(Long adminId);

>>>>>>> 2f23817ea585dfe5bb41fc13a9d53b5bd72a1f3f
    List<Drinking> search(String keyword);
}