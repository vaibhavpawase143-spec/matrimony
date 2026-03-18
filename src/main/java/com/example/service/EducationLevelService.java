package com.example.service;

import com.example.model.EducationLevel;
import java.util.List;

public interface EducationLevelService {

    List<EducationLevel> getAll();

    List<EducationLevel> getActive();

    EducationLevel getById(Long id);

    EducationLevel create(EducationLevel edu);

    EducationLevel update(Long id, EducationLevel updated);

    void delete(Long id);

    List<EducationLevel> getByAdmin(Long adminId);

    List<EducationLevel> getActiveByAdmin(Long adminId);

    List<EducationLevel> search(String keyword);
}