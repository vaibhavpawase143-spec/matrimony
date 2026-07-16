package com.example.serviceimpl;

import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.Country;
import com.example.repository.AdminRepository;
import com.example.repository.CountryRepository;
import com.example.service.CountryService;
import com.example.util.AuditHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final AdminRepository adminRepository;
    private final AuditHelper auditHelper;
    // ✅ Create
    @Override
    @Transactional
    public Country create(Country country) {

        Admin admin = adminRepository.findById(
                country.getAdmin().getId()
        ).orElseThrow(() ->
                new ResourceNotFoundException("Admin not found"));

        country.setName(country.getName().trim());

        if (countryRepository.existsByNameIgnoreCaseAndDeletedAtIsNull(
                country.getName())) {

            throw new BadRequestException("Country already exists");
        }

        country.setAdmin(admin);

        Country saved = countryRepository.save(country);

        auditHelper.logCreate(
                "MASTER_DATA",
                "COUNTRY",
                saved.getId(),
                saved.getName(),
                "Name=" + saved.getName()
                        + ", Active=" + saved.getIsActive()
        );

        return saved;
    }

    // 🔄 Update
    @Override
    @Transactional
    public Country update(Long id, Country updated) {

        Country existing = getById(id);

        String oldValue = "Name=" + existing.getName()
                + ", Active=" + existing.getIsActive();

        boolean wasActive = Boolean.TRUE.equals(existing.getIsActive());

        updated.setName(updated.getName().trim());

        if (!existing.getName().equalsIgnoreCase(updated.getName())
                && countryRepository.existsByNameIgnoreCaseAndDeletedAtIsNull(
                updated.getName())) {

            throw new BadRequestException("Country already exists");
        }

        existing.setName(updated.getName());

        existing.setIsActive(
                updated.getIsActive() != null
                        ? updated.getIsActive()
                        : existing.getIsActive()
        );

        Country saved = countryRepository.save(existing);

        String newValue = "Name=" + saved.getName()
                + ", Active=" + saved.getIsActive();

        auditHelper.logUpdate(
                "MASTER_DATA",
                "COUNTRY",
                saved.getId(),
                saved.getName(),
                oldValue,
                newValue,
                wasActive,
                Boolean.TRUE.equals(saved.getIsActive())
        );

        return saved;
    }
    // ❌ Delete
    @Override
    @Transactional
    public void delete(Long id, Long deletedBy) {

        Country country = getById(id);

        String oldValue = "Name=" + country.getName()
                + ", Active=" + country.getIsActive();

        country.setDeletedAt(LocalDateTime.now());
        country.setDeletedBy(deletedBy);

        countryRepository.save(country);

        auditHelper.logDelete(
                "MASTER_DATA",
                "COUNTRY",
                country.getId(),
                country.getName(),
                oldValue
        );
    }
    @Override
    @Transactional
    public void hardDelete(Long id) {

        Country country = countryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Country not found"));

        String oldValue = "Name=" + country.getName()
                + ", Active=" + country.getIsActive();

        countryRepository.delete(country);

        auditHelper.logHardDelete(
                "MASTER_DATA",
                "COUNTRY",
                country.getId(),
                country.getName(),
                oldValue
        );
    }
    @Override
    @Transactional
    public Country restore(Long id) {

        Country country = countryRepository.findById(id)
                .filter(c -> c.getDeletedAt() != null)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Deleted country not found"));

        country.setDeletedAt(null);
        country.setDeletedBy(null);
        country.setUpdatedAt(LocalDateTime.now());

        Country restored = countryRepository.save(country);

        auditHelper.logRestore(
                "MASTER_DATA",
                "COUNTRY",
                restored.getId(),
                restored.getName(),
                "Name=" + restored.getName()
                        + ", Active=" + restored.getIsActive()
        );

        return restored;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Country> getDeleted() {

        return countryRepository.findByDeletedAtIsNotNull();
    }
    // 🔍 Get by ID
    @Override
    @Transactional(readOnly = true)
    public Country getById(Long id) {

        return countryRepository.findById(id)
                .filter(country -> country.getDeletedAt() == null)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Country not found"));
    }
    // 🔍 Get all
    @Override
    @Transactional(readOnly = true)
    public List<Country> getAll() {

        return countryRepository.findByDeletedAtIsNull();
    }
    // 🔍 Find by name

    // 🔍 Active / Inactive
    @Override
    @Transactional(readOnly = true)
    public List<Country> getActive() {

        return countryRepository.findByIsActiveTrueAndDeletedAtIsNull();
    }


    // 🔍 Admin-based
    @Override
    @Transactional(readOnly = true)
    public List<Country> getByAdmin(Long adminId) {

        return countryRepository.findByAdminIdAndDeletedAtIsNull(adminId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Country> getActiveByAdmin(Long adminId) {

        return countryRepository.findByAdminIdAndIsActiveTrueAndDeletedAtIsNull(adminId);
    }

    // 🔍 Search
    @Override
    @Transactional(readOnly = true)
    public List<Country> search(String keyword) {

        return countryRepository.findByNameContainingIgnoreCaseAndDeletedAtIsNull(
                keyword.trim()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Country> searchByAdmin(Long adminId, String keyword) {

        return countryRepository.findByAdminIdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
                adminId,
                keyword.trim()
        );
    }
}