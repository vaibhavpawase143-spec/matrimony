package com.example.serviceimpl;

import com.example.model.Diet;
import com.example.repository.DietRepository;
import com.example.service.DietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DietServiceImpl implements DietService {

    @Autowired
    private DietRepository repo;

    // ✅ Get all
    @Override
    public List<Diet> getAll() {
        return repo.findAll();
    }

    // ✅ Get active
    @Override
    public List<Diet> getActive() {
        return repo.findByIsActiveTrue();   // ✅ fixed
    }

    // ✅ Get by ID
    @Override
    public Diet getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Diet not found with id: " + id));
    }

    // ✅ Create
    @Override
    public Diet create(Diet diet) {
        diet.setCreatedAt(LocalDateTime.now());
        diet.setUpdatedAt(LocalDateTime.now());
        diet.setisActive(true);   // ✅ fixed
        return repo.save(diet);
    }

    // ✅ Update
    @Override
    public Diet update(Long id, Diet updated) {
        Diet existing = getById(id);

        existing.setName(updated.getName());
        existing.setisActive(updated.getisActive());   // ✅ fixed
        existing.setUpdatedAt(LocalDateTime.now());

        return repo.save(existing);
    }

    // ✅ Delete
    @Override
    public void delete(Long id) {
        Diet existing = getById(id);
        repo.delete(existing);
    }

    // ✅ Get by admin
    @Override
    public List<Diet> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Get active by admin
    @Override
    public List<Diet> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndIsActiveTrue(adminId);   // ✅ fixed
    }

    // ✅ Search
    @Override
    public List<Diet> search(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }
}