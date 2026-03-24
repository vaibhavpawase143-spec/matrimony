package com.example.serviceimpl;

import com.example.model.Height;
import com.example.repository.HeightRepository;
import com.example.service.HeightService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HeightServiceImpl implements HeightService {

    private final HeightRepository heightRepository;

    public HeightServiceImpl(HeightRepository heightRepository) {
        this.heightRepository = heightRepository;
    }

    // ✅ Create
    @Override
    public Height create(Height height) {

        if (heightRepository.existsByHeightIgnoreCase(height.getHeight())) {
            throw new RuntimeException("Height already exists: " + height.getHeight());
        }

        return heightRepository.save(height);
    }

    // 🔄 Update
    @Override
    public Height update(Long id, Height height) {

        Height existing = heightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Height not found with id: " + id));

        heightRepository.findByHeightIgnoreCase(height.getHeight())
                .ifPresent(h -> {
                    if (!h.getId().equals(id)) {
                        throw new RuntimeException("Height already exists: " + height.getHeight());
                    }
                });

        // ✏️ Update fields
        existing.setHeight(height.getHeight());
        existing.setIsActive(height.getIsActive());

        return heightRepository.save(existing);
    }

    // ❌ Delete
    @Override
    public void delete(Long id) {
        Height existing = heightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Height not found with id: " + id));

        heightRepository.delete(existing);
    }

    // 🔍 Get by ID
    @Override
    public Optional<Height> getById(Long id) {
        return heightRepository.findById(id);
    }

    // 🔍 Get all
    @Override
    public List<Height> getAll() {
        return heightRepository.findAll();
    }

    // 🔍 Find by height
    @Override
    public Optional<Height> getByHeight(String height) {
        return heightRepository.findByHeight(height);
    }

    @Override
    public Optional<Height> getByHeightIgnoreCase(String height) {
        return heightRepository.findByHeightIgnoreCase(height);
    }

    // ✅ Duplicate check
    @Override
    public boolean existsByHeight(String height) {
        return heightRepository.existsByHeight(height);
    }

    @Override
    public boolean existsByHeightIgnoreCase(String height) {
        return heightRepository.existsByHeightIgnoreCase(height);
    }

    // 🔍 Active / Inactive
    @Override
    public List<Height> getActive() {
        return heightRepository.findByIsActiveTrue();
    }

    @Override
    public List<Height> getInactive() {
        return heightRepository.findByIsActiveFalse();
    }

    // 🔍 Admin-based
    @Override
    public List<Height> getByAdmin(Long adminId) {
        return heightRepository.findByAdminId(adminId);
    }

    @Override
    public List<Height> getActiveByAdmin(Long adminId) {
        return heightRepository.findByAdminIdAndIsActiveTrue(adminId);
    }

    // 🔍 Search
    @Override
    public List<Height> search(String keyword) {
        return heightRepository.findByHeightContainingIgnoreCase(keyword);
    }

    @Override
    public List<Height> searchByAdmin(Long adminId, String keyword) {
        return heightRepository.findByAdminIdAndHeightContainingIgnoreCase(adminId, keyword);
    }
}