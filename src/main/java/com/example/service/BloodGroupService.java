package com.example.service;

import com.example.model.BloodGroup;
import java.util.List;

public interface BloodGroupService {

    // ✅ Get all blood groups
    List<BloodGroup> getAll();

    // ✅ Get only active blood groups
    List<BloodGroup> getActive();

    // ✅ Get by ID
    BloodGroup getById(Long id);

    // ✅ Create blood group
    BloodGroup create(BloodGroup bg);

    // ✅ Update blood group
    BloodGroup update(Long id, BloodGroup bg);

    // ✅ Delete blood group
    void delete(Long id);

    // ✅ Get by admin
    List<BloodGroup> getByAdmin(Long adminId);

    // ✅ Get active blood groups by admin
    List<BloodGroup> getActiveByAdmin(Long adminId);
}