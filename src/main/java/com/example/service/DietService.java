package com.example.service;

import com.example.model.Diet;
import java.util.List;

public interface DietService {

    List<Diet> getAll();

    List<Diet> getActive();

    Diet getById(Long id);

    Diet create(Diet diet);

    Diet update(Long id, Diet updated);

    void delete(Long id);

    List<Diet> getByAdmin(Long adminId);

    List<Diet> getActiveByAdmin(Long adminId);

    List<Diet> search(String keyword);
}