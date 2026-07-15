package com.example.serviceimpl;

import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.City;
import com.example.model.State;
import com.example.repository.AdminRepository;
import com.example.repository.CityRepository;
import com.example.repository.StateRepository;
import com.example.service.CityService;
import com.example.util.AuditHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final AdminRepository adminRepository;
    private final StateRepository stateRepository;
    private final AuditHelper auditHelper;

    // 🔍 Basic CRUD
    @Override
    @Transactional
    public City create(City city) {

        Admin admin = adminRepository.findById(city.getAdmin().getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found"));

        State state = stateRepository.findById(city.getState().getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("State not found"));

        city.setName(city.getName().trim());

        if (city.getName().isBlank()) {
            throw new BadRequestException("City name is required");
        }

        if (cityRepository.existsByNameIgnoreCaseAndStateIdAndDeletedAtIsNull(
                city.getName(),
                state.getId())) {

            throw new BadRequestException("City already exists");
        }

        city.setAdmin(admin);
        city.setState(state);

        City saved = cityRepository.save(city);

        auditHelper.logCreate(
                "MASTER_DATA",
                "CITY",
                saved.getId(),
                saved.getName(),
                "Name=" + saved.getName()
                        + ", State=" + state.getName()
                        + ", Active=" + saved.getIsActive()
        );

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public City getById(Long id) {

        return cityRepository.findById(id)
                .filter(city -> city.getDeletedAt() == null)
                .orElseThrow(() ->
                        new ResourceNotFoundException("City not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<City> getAll() {

        return cityRepository.findByDeletedAtIsNull();
    }
    @Override
    @Transactional
    public void delete(Long id, Long deletedBy) {

        City city = cityRepository.findById(id)
                .filter(c -> c.getDeletedAt() == null)
                .orElseThrow(() ->
                        new ResourceNotFoundException("City not found"));

        String oldValue = "Name=" + city.getName()
                + ", State=" + city.getState().getName()
                + ", Active=" + city.getIsActive();

        city.setDeletedAt(LocalDateTime.now());
        city.setDeletedBy(deletedBy);

        cityRepository.save(city);

        auditHelper.logDelete(
                "MASTER_DATA",
                "CITY",
                city.getId(),
                city.getName(),
                oldValue
        );
    }

    // 🔍 Find by name


    // 🔍 Active / Inactive
    @Override
    @Transactional(readOnly = true)
    public List<City> getActive() {

        return cityRepository.findByIsActiveTrueAndDeletedAtIsNull();
    }

    @Override
    @Transactional
    public City update(Long id, City updated) {

        City existing = getById(id);

        State state = stateRepository.findById(updated.getState().getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("State not found"));

        String oldValue = "Name=" + existing.getName()
                + ", State=" + existing.getState().getName()
                + ", Active=" + existing.getIsActive();

        boolean wasActive = Boolean.TRUE.equals(existing.getIsActive());

        updated.setName(updated.getName().trim());

        if (!existing.getName().equalsIgnoreCase(updated.getName())
                || !existing.getState().getId().equals(state.getId())) {

            if (cityRepository.existsByNameIgnoreCaseAndStateIdAndDeletedAtIsNull(
                    updated.getName(),
                    state.getId())) {

                throw new BadRequestException("City already exists");
            }
        }

        existing.setName(updated.getName());
        existing.setState(state);

        existing.setIsActive(
                updated.getIsActive() != null
                        ? updated.getIsActive()
                        : existing.getIsActive()
        );

        City saved = cityRepository.save(existing);

        String newValue = "Name=" + saved.getName()
                + ", State=" + saved.getState().getName()
                + ", Active=" + saved.getIsActive();

        boolean isActive = Boolean.TRUE.equals(saved.getIsActive());

        auditHelper.logUpdate(
                "MASTER_DATA",
                "CITY",
                saved.getId(),
                saved.getName(),
                oldValue,
                newValue,
                wasActive,
                isActive
        );

        return saved;
    }



    @Override
    @Transactional
    public void hardDelete(Long id) {

        City city = cityRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("City not found"));

        String oldValue = "Name=" + city.getName()
                + ", State=" + city.getState().getName()
                + ", Active=" + city.getIsActive();

        cityRepository.delete(city);

        auditHelper.logHardDelete(
                "MASTER_DATA",
                "CITY",
                city.getId(),
                city.getName(),
                oldValue
        );
    }
    @Override
    @Transactional
    public City restore(Long id) {

        City city = cityRepository.findById(id)
                .filter(c -> c.getDeletedAt() != null)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Deleted city not found"));

        city.setDeletedAt(null);
        city.setDeletedBy(null);
        city.setUpdatedAt(LocalDateTime.now());
        City restored = cityRepository.save(city);

        auditHelper.logRestore(
                "MASTER_DATA",
                "CITY",
                restored.getId(),
                restored.getName(),
                "Name=" + restored.getName()
                        + ", State=" + restored.getState().getName()
                        + ", Active=" + restored.getIsActive()
        );

        return restored;
    }

    @Override
    @Transactional(readOnly = true)
    public List<City> getDeleted() {

        return cityRepository.findByDeletedAtIsNotNull();
    }
    @Override
    @Transactional(readOnly = true)
    public List<City> getByState(Long stateId) {

        return cityRepository.findByState_IdAndDeletedAtIsNull(stateId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<City> getActiveByState(Long stateId) {

        return cityRepository.findByState_IdAndIsActiveTrueAndDeletedAtIsNull(stateId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<City> getByAdmin(Long adminId) {

        return cityRepository.findByAdminIdAndDeletedAtIsNull(adminId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<City> getActiveByAdmin(Long adminId) {

        return cityRepository.findByAdminIdAndIsActiveTrueAndDeletedAtIsNull(adminId);
    }



    // 🔍 Admin-based

    // 🔍 Search
    @Override
    @Transactional(readOnly = true)
    public List<City> searchByName(String keyword) {

        return cityRepository.findByNameContainingIgnoreCaseAndDeletedAtIsNull(
                keyword.trim()
        );
    }
    @Override
    @Transactional(readOnly = true)
    public List<City> searchByState(Long stateId, String keyword) {

        return cityRepository
                .findByState_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
                        stateId,
                        keyword.trim()
                );
    }

    @Override
    @Transactional(readOnly = true)
    public List<City> searchByAdmin(Long adminId, String keyword) {

        return cityRepository
                .findByAdminIdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
                        adminId,
                        keyword.trim()
                );
    }
}