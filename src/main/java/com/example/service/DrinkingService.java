package com.example.service;

import com.example.model.Drinking;
import java.util.List;

public interface DrinkingService {

    List<Drinking> getAll();

    List<Drinking> getActive();

    Drinking getById(Long id);

    Drinking create(Drinking drinking);

    Drinking update(Long id, Drinking updated);

    void delete(Long id);

    List<Drinking> getByAdmin(Long adminId);

    List<Drinking> getActiveByAdmin(Long adminId);

    List<Drinking> search(String keyword);
}