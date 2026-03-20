package com.example.serviceimpl;

import com.example.model.BodyType;
import com.example.repository.BodyTypeRepository;
import com.example.service.BodyTypeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BodyTypeServiceImpl implements BodyTypeService {

    private final BodyTypeRepository repo;

    public BodyTypeServiceImpl(BodyTypeRepository repo) {
        this.repo = repo;
    }

    // ✅ Get all body types
    @Override
    public List<BodyType> getAll() {
        return repo.findAll();
    }

    // ✅ Get active body types
    @Override
    public List<BodyType> getActive() {
        return repo.findByIsActiveTrue();
    }

    // ✅ Get inactive body types
    @Override
    public List<BodyType> getInactive() {
        return repo.findByIsActiveFalse();
    }

    // ✅ Get by ID
    @Override
    public BodyType getById(Long id) {
        return repo.findById(id).get();
    }

    // ✅ Create body type
    @Override
    public BodyType create(BodyType bodyType) {
        bodyType.setisActive(true);   // default active
        bodyType.setCreatedAt(LocalDateTime.now());
        bodyType.setUpdatedAt(LocalDateTime.now());
        return repo.save(bodyType);
    }

    // ✅ Update body type
    @Override
    public BodyType update(Long id, BodyType bodyType) {
        BodyType existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("BodyType not found with id: " + id));

        existing.setValue(bodyType.getValue());
        existing.setisActive(bodyType.getisActive());
        existing.setUpdatedAt(LocalDateTime.now());

        return repo.save(existing);
    }

    // ✅ Delete body type
    @Override
    public void delete(Long id) {
        BodyType existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("BodyType not found with id: " + id));
        repo.delete(existing);
    }

    // ✅ Get by admin
    @Override
    public List<BodyType> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Get active by admin
    @Override
    public List<BodyType> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndIsActiveTrue(adminId);
    }
}