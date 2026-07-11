package com.example.serviceimpl;

import com.example.model.SubCaste;
import com.example.repository.SubCasteRepository;
import com.example.service.SubCasteService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubCasteServiceImpl implements SubCasteService {

    private final SubCasteRepository repository;

    public SubCasteServiceImpl(SubCasteRepository repository) {
        this.repository = repository;
    }

    // =========================================
    // SAVE
    // =========================================

    @Override
    public SubCaste save(SubCaste subCaste) {

        String name = subCaste.getName();

        Long adminId = subCaste.getAdmin() != null
                ? subCaste.getAdmin().getId()
                : null;

        Optional<SubCaste> existing =
                repository.findAccessibleByName(
                        name,
                        adminId
                );

        if (existing.isPresent()
                && !existing.get().getId().equals(subCaste.getId())) {

            throw new RuntimeException(
                    "SubCaste already exists!"
            );
        }

        return repository.save(subCaste);
    }

    // =========================================
    // GET BY ID
    // =========================================

    @Override
    public Optional<SubCaste> getById(Long id) {

        return repository.findById(id);
    }

    // =========================================
    // GET ALL
    // =========================================

    @Override
    public List<SubCaste> getAll() {

        return repository.findAll();
    }

    // =========================================
    // GET BY ADMIN
    // =========================================

    @Override
    public List<SubCaste> getByAdmin(Long adminId) {

        return repository.findAllAvailable(adminId);
    }

    // =========================================
    // ACTIVE
    // =========================================

    @Override
    public List<SubCaste> getActiveByAdmin(Long adminId) {

        return repository.findAllActiveAvailable(adminId);
    }

    // =========================================
    // INACTIVE
    // =========================================

    @Override
    public List<SubCaste> getInactiveByAdmin(Long adminId) {

        return repository.findAllInactiveAvailable(adminId);
    }

    // =========================================
    // BY CASTE
    // =========================================

    @Override
    public List<SubCaste> getByCasteAndAdmin(
            Long casteId,
            Long adminId
    ) {

        return repository.findAvailableByCaste(
                casteId,
                adminId
        );
    }

    // =========================================
    // ACTIVE BY CASTE
    // =========================================

    @Override
    public List<SubCaste> getActiveByCasteAndAdmin(
            Long casteId,
            Long adminId
    ) {

        return repository.findActiveAvailableByCaste(
                casteId,
                adminId
        );
    }

    // =========================================
    // SEARCH
    // =========================================

    @Override
    public List<SubCaste> searchByAdmin(
            Long adminId,
            String keyword
    ) {

        return repository.searchAvailable(
                keyword,
                adminId
        );
    }

    // =========================================
    // FIND BY NAME
    // =========================================

    @Override
    public Optional<SubCaste> getByNameAndAdmin(
            String name,
            Long adminId
    ) {

        return repository.findAccessibleByName(
                name,
                adminId
        );
    }

    // =========================================
    // DELETE
    // =========================================

    @Override
    public void delete(Long id) {

        repository.deleteById(id);
    }
}