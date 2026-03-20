package com.example.serviceimpl;

import com.example.model.SisterType;
import com.example.repository.SisterTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SisterTypeServiceImpl {

    private final SisterTypeRepository repo;

    public SisterTypeServiceImpl(SisterTypeRepository repo) {
        this.repo = repo;
    }

    public List<SisterType> getAllActive() {
        return repo.findByIsActiveTrue();
    }

    public List<SisterType> getAllInactive() {
        return repo.findByIsActiveFalse();
    }

    public Optional<SisterType> getByValue(String value) {
        return repo.findByValue(value);
    }

    public boolean existsByValue(String value) {
        return repo.existsByValue(value);
    }

    public List<SisterType> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    public List<SisterType> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndIsActiveTrue(adminId);
    }

    public List<SisterType> searchByKeyword(String keyword) {
        return repo.findByValueContainingIgnoreCase(keyword);
    }

    public SisterType save(SisterType sisterType) {
        // Ensure new records default to active
        sisterType.setisActive(true);
        return repo.save(sisterType);
    }
}