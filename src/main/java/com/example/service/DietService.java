package com.example.service;

import com.example.model.Diet;

import java.util.List;

public interface DietService {

    Diet create(Diet diet);

    Diet update(Long id, Diet diet);

    void delete(Long id, Long deletedBy);

    void hardDelete(Long id);

    Diet restore(Long id);

    Diet getById(Long id);

    List<Diet> getAll();

    List<Diet> getDeleted();

    List<Diet> getActive();

    List<Diet> getInactive();

    List<Diet> getByAdmin(Long adminId);

    List<Diet> getActiveByAdmin(Long adminId);

    List<Diet> search(String keyword);

    List<Diet> searchByAdmin(Long adminId, String keyword);
}