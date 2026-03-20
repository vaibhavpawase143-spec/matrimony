package com.example.serviceimpl;

import com.example.model.BrotherType;
import com.example.repository.BrotherTypeRepository;
import com.example.service.BrotherTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrotherTypeServiceImpl implements BrotherTypeService {

    private final BrotherTypeRepository repo;

    public BrotherTypeServiceImpl(BrotherTypeRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<BrotherType> getAll() {
        return repo.findAll();
    }

    @Override
    public List<BrotherType> getActive() {
        return repo.findByIsActiveTrue();
    }

    @Override
    public BrotherType getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("BrotherType not found with id: " + id));
    }

    @Override
    public BrotherType create(BrotherType brotherType) {
        brotherType.setIsActive(true);
        return repo.save(brotherType);
    }

    @Override
    public BrotherType update(Long id, BrotherType brotherType) {
        BrotherType existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("BrotherType not found with id: " + id));

        existing.setValue(brotherType.getValue());
        existing.setIsActive(brotherType.getIsActive());

        return repo.save(existing);
    }

    @Override
    public void delete(Long id) {
        BrotherType existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("BrotherType not found with id: " + id));
        repo.delete(existing);
    }

    @Override
    public List<BrotherType> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    @Override
    public List<BrotherType> getActiveByAdmin(Long adminId) {
      return   repo.findByAdminIdAndIsActiveTrue(adminId);
    }

    @Override
    public List<BrotherType> search(String keyword) {
        return repo.findByValueContainingIgnoreCase(keyword);
    }
}