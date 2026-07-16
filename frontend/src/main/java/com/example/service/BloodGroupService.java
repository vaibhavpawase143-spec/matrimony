package com.example.service;

import com.example.model.BloodGroup;
import java.util.List;

public interface BloodGroupService {

    BloodGroup create(BloodGroup bloodGroup, Long adminId);

    BloodGroup getById(Long id, Long adminId);

    List<BloodGroup> getAll(Long adminId);

    List<BloodGroup> getActive(Long adminId);

    BloodGroup update(Long id, BloodGroup bloodGroup, Long adminId);

    void delete(Long id, Long adminId);
}