package com.example.service;

import com.example.model.BodyType;
import com.example.repository.BodyTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BodyTypeService {

    @Autowired
    private BodyTypeRepository repo;

    // ✅ Get all body types
    public List<BodyType> getAll() {
        return repo.findAll();
    }

    // ✅ Get active body types
    public List<BodyType> getActive() {
        return repo.findByStatusTrue();
    }

    // ✅ Get by ID
    public BodyType getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Body type not found"));
    }

    // ✅ Create body type
    public BodyType create(BodyType bodyType) {

        if (repo.existsByValue(bodyType.getValue())) {
            throw new RuntimeException("Body type already exists");
        }

        bodyType.setStatus(true);

        return repo.save(bodyType);
    }

    // ✅ Update body type
    public BodyType update(Long id, BodyType updated) {

        BodyType existing = getById(id);

        // Check duplicate if value changed
        if (!existing.getValue().equals(updated.getValue())
                && repo.existsByValue(updated.getValue())) {
            throw new RuntimeException("Body type already exists");
        }

        existing.setValue(updated.getValue());
        existing.setStatus(updated.getStatus());
        existing.setAdmin(updated.getAdmin());

        return repo.save(existing);
    }

    // ✅ Delete (hard delete)
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // ✅ Get by admin
    public List<BodyType> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Get active by admin
    public List<BodyType> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndStatusTrue(adminId);
    }
}