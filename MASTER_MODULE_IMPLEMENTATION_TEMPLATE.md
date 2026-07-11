# Master Data Modules - Implementation Template & Guide

## Overview
After the Religion module upgrade, all future master data modules should follow this exact template to ensure consistency, security, and maintainability.

---

## **Step-by-Step Implementation Template**

### **Module Example: Caste**

---

## **STEP 1: Entity Model**

### File: `src/main/java/com/example/model/Caste.java`

```java
package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Caste Master Data Entity
 * Represents caste categories available in the matrimonial system.
 * Supports soft delete and audit trail for compliance.
 */
@Entity
@Table(
        name = "castes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"admin_id", "name"}),
        indexes = {
                @Index(name = "idx_caste_name", columnList = "name"),
                @Index(name = "idx_caste_is_active", columnList = "is_active"),
                @Index(name = "idx_caste_deleted_at", columnList = "deleted_at")
        }
)
public class Caste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ================= SOFT DELETE FIELDS =================
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by")
    private Long deletedBy;

    public Caste() {}

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.isActive == null) {
            this.isActive = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters (Use Lombok @Data in production)
    // ... (truncated for brevity)
}
```

---

## **STEP 2: DTOs**

### File: `src/main/java/com/example/dto/request/CasteRequestDTO.java`

```java
package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Caste Request DTO
 * Used for creating and updating caste master data.
 */
@Data
public class CasteRequestDTO {

    @NotBlank(message = "Caste name is required")
    @Size(max = 120, message = "Name must be less than 120 characters")
    private String name;

    private Boolean isActive;
}
```

### File: `src/main/java/com/example/dto/response/CasteResponseDTO.java`

```java
package com.example.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CasteResponseDTO {

    private Long id;
    private Long adminId;
    private String adminName;
    private String name;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

---

## **STEP 3: Mapper**

### File: `src/main/java/com/example/mapper/CasteMapper.java`

```java
package com.example.mapper;

import com.example.dto.request.CasteRequestDTO;
import com.example.dto.response.CasteResponseDTO;
import com.example.model.Admin;
import com.example.model.Caste;

/**
 * Mapper utility for Caste entity ↔ DTO conversions.
 */
public class CasteMapper {

    private CasteMapper() {}

    public static Caste toEntity(CasteRequestDTO dto, Admin admin) {
        if (dto == null) return null;
        
        Caste caste = new Caste();
        caste.setName(dto.getName());
        caste.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        caste.setAdmin(admin);
        
        return caste;
    }

    public static CasteResponseDTO toResponseDTO(Caste caste) {
        if (caste == null) return null;
        
        return CasteResponseDTO.builder()
                .id(caste.getId())
                .adminId(caste.getAdmin() != null ? caste.getAdmin().getId() : null)
                .adminName(caste.getAdmin() != null ? caste.getAdmin().getName() : null)
                .name(caste.getName())
                .isActive(caste.getIsActive())
                .createdAt(caste.getCreatedAt())
                .updatedAt(caste.getUpdatedAt())
                .build();
    }

    public static void updateEntity(Caste caste, CasteRequestDTO dto) {
        if (caste == null || dto == null) return;
        
        if (dto.getName() != null && !dto.getName().isBlank()) {
            caste.setName(dto.getName());
        }
        if (dto.getIsActive() != null) {
            caste.setIsActive(dto.getIsActive());
        }
    }
}
```

---

## **STEP 4: Repository**

### File: `src/main/java/com/example/repository/CasteRepository.java`

```java
package com.example.repository;

import com.example.model.Caste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CasteRepository extends JpaRepository<Caste, Long> {

    // Active records
    @Query("SELECT c FROM Caste c WHERE c.deletedAt IS NULL AND c.isActive = true ORDER BY c.name ASC")
    List<Caste> findAllActive();

    @Query("SELECT c FROM Caste c WHERE c.admin.id = :adminId AND c.deletedAt IS NULL AND c.isActive = true ORDER BY c.name ASC")
    List<Caste> findActiveByAdmin(@Param("adminId") Long adminId);

    // By name
    @Query("SELECT c FROM Caste c WHERE LOWER(c.name) = LOWER(:name) AND c.deletedAt IS NULL")
    Optional<Caste> findByNameIgnoreCase(@Param("name") String name);

    @Query("SELECT c FROM Caste c WHERE LOWER(c.name) = LOWER(:name) AND c.admin.id = :adminId AND c.deletedAt IS NULL")
    Optional<Caste> findByNameIgnoreCaseAndAdmin(@Param("name") String name, @Param("adminId") Long adminId);

    // By admin
    List<Caste> findByAdminIdAndDeletedAtIsNull(Long adminId);
    List<Caste> findByAdminIdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);
    List<Caste> findByAdminIdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    // Search
    @Query("SELECT c FROM Caste c WHERE c.admin.id = :adminId AND c.deletedAt IS NULL AND LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY c.name ASC")
    List<Caste> searchByAdminAndKeyword(@Param("adminId") Long adminId, @Param("keyword") String keyword);

    // Deleted records
    List<Caste> findByAdminIdAndDeletedAtIsNotNull(Long adminId);

    @Query("SELECT CASE WHEN c.deletedAt IS NOT NULL THEN true ELSE false END FROM Caste c WHERE c.id = :id")
    boolean isDeleted(@Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Caste c WHERE LOWER(c.name) = LOWER(:name) AND c.admin.id = :adminId AND c.deletedAt IS NULL")
    boolean existsByNameIgnoreCaseAndAdmin(@Param("name") String name, @Param("adminId") Long adminId);
}
```

---

## **STEP 5: Service Interface**

### File: `src/main/java/com/example/service/CasteService.java`

```java
package com.example.service;

import com.example.dto.request.CasteRequestDTO;
import com.example.dto.response.CasteResponseDTO;

import java.util.List;

/**
 * Service interface for Caste master data.
 */
public interface CasteService {

    // CRUD
    CasteResponseDTO create(CasteRequestDTO dto, Long adminId);
    CasteResponseDTO getById(Long id);
    List<CasteResponseDTO> getAll();
    CasteResponseDTO update(Long id, CasteRequestDTO dto);
    void delete(Long id, Long deletedBy);
    void hardDelete(Long id);

    // Admin-specific
    List<CasteResponseDTO> getByAdmin(Long adminId);
    List<CasteResponseDTO> getActiveByAdmin(Long adminId);
    List<CasteResponseDTO> getInactiveByAdmin(Long adminId);
    List<CasteResponseDTO> searchByAdmin(Long adminId, String keyword);
    List<CasteResponseDTO> getDeletedByAdmin(Long adminId);

    // Utility
    boolean existsForAdmin(String name, Long adminId);
    CasteResponseDTO restore(Long id);
}
```

---

## **STEP 6: Service Implementation**

### File: `src/main/java/com/example/serviceimpl/CasteServiceImpl.java`

```java
package com.example.serviceimpl;

import com.example.dto.request.CasteRequestDTO;
import com.example.dto.response.CasteResponseDTO;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.CasteMapper;
import com.example.model.Admin;
import com.example.model.Caste;
import com.example.repository.AdminRepository;
import com.example.repository.CasteRepository;
import com.example.service.CasteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of CasteService with business logic for CRUD and search operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CasteServiceImpl implements CasteService {

    private final CasteRepository casteRepository;
    private final AdminRepository adminRepository;

    @Override
    @Transactional
    public CasteResponseDTO create(CasteRequestDTO dto, Long adminId) {
        log.debug("Creating new caste: {} for admin: {}", dto.getName(), adminId);

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> {
                    log.error("Admin not found: {}", adminId);
                    return new ResourceNotFoundException("Admin not found");
                });

        if (casteRepository.existsByNameIgnoreCaseAndAdmin(dto.getName(), adminId)) {
            log.warn("Caste already exists: {} for admin: {}", dto.getName(), adminId);
            throw new BadRequestException("Caste '" + dto.getName() + "' already exists for this admin");
        }

        Caste caste = CasteMapper.toEntity(dto, admin);
        Caste saved = casteRepository.save(caste);
        log.info("Caste created successfully with ID: {}", saved.getId());

        return CasteMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CasteResponseDTO getById(Long id) {
        log.debug("Fetching caste by ID: {}", id);

        Caste caste = casteRepository.findById(id)
                .filter(c -> c.getDeletedAt() == null)
                .orElseThrow(() -> {
                    log.warn("Caste not found: {}", id);
                    return new ResourceNotFoundException("Caste not found");
                });

        return CasteMapper.toResponseDTO(caste);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CasteResponseDTO> getAll() {
        log.debug("Fetching all active castes");
        return casteRepository.findAllActive()
                .stream()
                .map(CasteMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CasteResponseDTO update(Long id, CasteRequestDTO dto) {
        log.debug("Updating caste ID: {}", id);

        Caste caste = casteRepository.findById(id)
                .filter(c -> c.getDeletedAt() == null)
                .orElseThrow(() -> {
                    log.warn("Caste not found for update: {}", id);
                    return new ResourceNotFoundException("Caste not found");
                });

        if (dto.getName() != null && !dto.getName().equalsIgnoreCase(caste.getName())) {
            if (casteRepository.existsByNameIgnoreCaseAndAdmin(dto.getName(), caste.getAdmin().getId())) {
                log.warn("Caste name already exists: {}", dto.getName());
                throw new BadRequestException("Caste '" + dto.getName() + "' already exists for this admin");
            }
        }

        CasteMapper.updateEntity(caste, dto);
        Caste updated = casteRepository.save(caste);
        log.info("Caste updated successfully with ID: {}", updated.getId());

        return CasteMapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void delete(Long id, Long deletedBy) {
        log.debug("Soft deleting caste ID: {} by admin: {}", id, deletedBy);

        Caste caste = casteRepository.findById(id)
                .filter(c -> c.getDeletedAt() == null)
                .orElseThrow(() -> {
                    log.warn("Caste not found for deletion: {}", id);
                    return new ResourceNotFoundException("Caste not found");
                });

        caste.setDeletedAt(LocalDateTime.now());
        caste.setDeletedBy(deletedBy);
        casteRepository.save(caste);
        log.info("Caste soft deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional
    public void hardDelete(Long id) {
        log.warn("Hard deleting caste ID: {} - permanent operation", id);

        if (!casteRepository.existsById(id)) {
            log.error("Caste not found for hard deletion: {}", id);
            throw new ResourceNotFoundException("Caste not found");
        }

        casteRepository.deleteById(id);
        log.info("Caste hard deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CasteResponseDTO> getByAdmin(Long adminId) {
        log.debug("Fetching all castes for admin: {}", adminId);
        return casteRepository.findByAdminIdAndDeletedAtIsNull(adminId)
                .stream()
                .map(CasteMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CasteResponseDTO> getActiveByAdmin(Long adminId) {
        log.debug("Fetching active castes for admin: {}", adminId);
        return casteRepository.findByAdminIdAndIsActiveTrueAndDeletedAtIsNull(adminId)
                .stream()
                .map(CasteMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CasteResponseDTO> getInactiveByAdmin(Long adminId) {
        log.debug("Fetching inactive castes for admin: {}", adminId);
        return casteRepository.findByAdminIdAndIsActiveFalseAndDeletedAtIsNull(adminId)
                .stream()
                .map(CasteMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CasteResponseDTO> searchByAdmin(Long adminId, String keyword) {
        log.debug("Searching castes for admin: {} with keyword: {}", adminId, keyword);
        if (keyword == null || keyword.trim().isEmpty()) {
            return getByAdmin(adminId);
        }
        return casteRepository.searchByAdminAndKeyword(adminId, keyword)
                .stream()
                .map(CasteMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CasteResponseDTO> getDeletedByAdmin(Long adminId) {
        log.debug("Fetching deleted castes for admin: {}", adminId);
        return casteRepository.findByAdminIdAndDeletedAtIsNotNull(adminId)
                .stream()
                .map(CasteMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsForAdmin(String name, Long adminId) {
        log.debug("Checking if caste exists: {} for admin: {}", name, adminId);
        return casteRepository.existsByNameIgnoreCaseAndAdmin(name, adminId);
    }

    @Override
    @Transactional
    public CasteResponseDTO restore(Long id) {
        log.debug("Restoring deleted caste ID: {}", id);

        Caste caste = casteRepository.findById(id)
                .filter(c -> c.getDeletedAt() != null)
                .orElseThrow(() -> {
                    log.warn("Deleted caste not found for restoration: {}", id);
                    return new ResourceNotFoundException("Deleted caste not found");
                });

        caste.setDeletedAt(null);
        caste.setDeletedBy(null);
        Caste restored = casteRepository.save(caste);
        log.info("Caste restored successfully with ID: {}", restored.getId());

        return CasteMapper.toResponseDTO(restored);
    }
}
```

---

## **STEP 7: Controller**

### File: `src/main/java/com/example/controller/master/CasteController.java`

```java
package com.example.controller.master;

import com.example.dto.request.CasteRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.CasteResponseDTO;
import com.example.security.SecurityUtils;
import com.example.service.AdminService;
import com.example.service.CasteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Caste Master Data Management
 * Provides endpoints for CRUD operations, search, and filtering.
 */
@RestController
@RequestMapping("/api/castes")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class CasteController {

    private final CasteService casteService;
    private final AdminService adminService;

    // ================= PUBLIC ENDPOINTS =================

    @GetMapping
    public ResponseEntity<ApiResponse<List<CasteResponseDTO>>> getAll() {
        log.debug("Fetching all active castes");
        List<CasteResponseDTO> castes = casteService.getAll();
        
        return ResponseEntity.ok(
                ApiResponse.<List<CasteResponseDTO>>builder()
                        .success(true)
                        .message("Castes fetched successfully")
                        .data(castes)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CasteResponseDTO>> getById(@PathVariable Long id) {
        log.debug("Fetching caste by ID: {}", id);
        CasteResponseDTO caste = casteService.getById(id);
        
        return ResponseEntity.ok(
                ApiResponse.<CasteResponseDTO>builder()
                        .success(true)
                        .message("Caste fetched successfully")
                        .data(caste)
                        .build()
        );
    }

    // ================= ADMIN ENDPOINTS =================

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<CasteResponseDTO>> create(@Valid @RequestBody CasteRequestDTO dto) {
        log.info("Creating new caste: {}", dto.getName());
        
        String currentEmail = SecurityUtils.getCurrentUsername();
        Long currentAdminId = adminService.findByEmail(currentEmail).getId();
        
        CasteResponseDTO created = casteService.create(dto, currentAdminId);
        
        log.info("Caste created successfully with ID: {}", created.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<CasteResponseDTO>builder()
                                .success(true)
                                .message("Caste created successfully")
                                .data(created)
                                .build()
                );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<CasteResponseDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody CasteRequestDTO dto) {
        
        log.info("Updating caste ID: {}", id);
        CasteResponseDTO updated = casteService.update(id, dto);
        
        log.info("Caste updated successfully with ID: {}", id);
        return ResponseEntity.ok(
                ApiResponse.<CasteResponseDTO>builder()
                        .success(true)
                        .message("Caste updated successfully")
                        .data(updated)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        log.info("Deleting caste ID: {}", id);
        
        String currentEmail = SecurityUtils.getCurrentUsername();
        Long currentAdminId = adminService.findByEmail(currentEmail).getId();
        
        casteService.delete(id, currentAdminId);
        
        log.info("Caste soft deleted successfully with ID: {}", id);
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Caste deleted successfully")
                        .data("Caste ID: " + id)
                        .build()
        );
    }

    @PostMapping("/{id}/restore")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<CasteResponseDTO>> restore(@PathVariable Long id) {
        log.info("Restoring caste ID: {}", id);
        CasteResponseDTO restored = casteService.restore(id);
        
        log.info("Caste restored successfully with ID: {}", id);
        return ResponseEntity.ok(
                ApiResponse.<CasteResponseDTO>builder()
                        .success(true)
                        .message("Caste restored successfully")
                        .data(restored)
                        .build()
        );
    }

    // ================= SEARCH & FILTER =================

    @GetMapping("/admin/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<CasteResponseDTO>>> getForAdmin() {
        log.debug("Fetching castes for authenticated admin");
        
        String currentEmail = SecurityUtils.getCurrentUsername();
        Long currentAdminId = adminService.findByEmail(currentEmail).getId();
        
        List<CasteResponseDTO> castes = casteService.getByAdmin(currentAdminId);
        
        return ResponseEntity.ok(
                ApiResponse.<List<CasteResponseDTO>>builder()
                        .success(true)
                        .message("Castes fetched successfully")
                        .data(castes)
                        .build()
        );
    }

    @GetMapping("/admin/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<CasteResponseDTO>>> getActiveForAdmin() {
        log.debug("Fetching active castes for authenticated admin");
        
        String currentEmail = SecurityUtils.getCurrentUsername();
        Long currentAdminId = adminService.findByEmail(currentEmail).getId();
        
        List<CasteResponseDTO> castes = casteService.getActiveByAdmin(currentAdminId);
        
        return ResponseEntity.ok(
                ApiResponse.<List<CasteResponseDTO>>builder()
                        .success(true)
                        .message("Active castes fetched successfully")
                        .data(castes)
                        .build()
        );
    }

    @GetMapping("/admin/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<CasteResponseDTO>>> search(@RequestParam(required = false) String keyword) {
        log.debug("Searching castes with keyword: {}", keyword);
        
        String currentEmail = SecurityUtils.getCurrentUsername();
        Long currentAdminId = adminService.findByEmail(currentEmail).getId();
        
        List<CasteResponseDTO> castes = casteService.searchByAdmin(currentAdminId, keyword);
        
        return ResponseEntity.ok(
                ApiResponse.<List<CasteResponseDTO>>builder()
                        .success(true)
                        .message("Search completed successfully")
                        .data(castes)
                        .build()
        );
    }
}
```

---

## **STEP 8: Flyway Migration**

### File: `src/main/resources/migration/V57__add_soft_delete_to_castes.sql`

```sql
-- V57__add_soft_delete_to_castes.sql
-- Add soft delete support to castes table

ALTER TABLE castes 
ADD COLUMN deleted_at TIMESTAMP NULL,
ADD COLUMN deleted_by BIGINT NULL;

-- Create indexes for soft delete queries
CREATE INDEX idx_caste_deleted_at ON castes(deleted_at);
CREATE INDEX idx_caste_deleted_by ON castes(deleted_by);
CREATE INDEX idx_caste_is_active ON castes(is_active);

-- Add foreign key constraint for deleted_by
ALTER TABLE castes
ADD CONSTRAINT fk_castes_deleted_by
    FOREIGN KEY (deleted_by)
    REFERENCES admins(id)
    ON DELETE SET NULL;
```

---

## **Summary of Files Created**

For each master module (Caste, SubCaste, State, City, etc.), create:

1. **Model** (Entity) - `CasteModel.java`
2. **DTOs** - `CasteRequestDTO.java`, `CasteResponseDTO.java`
3. **Mapper** - `CasteMapper.java`
4. **Repository** - `CasteRepository.java`
5. **Service Interface** - `CasteService.java`
6. **Service Implementation** - `CasteServiceImpl.java`
7. **Controller** - `CasteController.java`
8. **Migration** - `V##__add_soft_delete_to_castes.sql`

---

## **Checklist for Each Module**

- [ ] Entity with soft delete fields (deleted_at, deleted_by)
- [ ] Proper indexes on frequently searched columns
- [ ] Request and Response DTOs with validation
- [ ] Mapper utility class for conversions
- [ ] Repository with optimized queries (JOIN FETCH, custom @Query)
- [ ] Service interface with clear method contracts
- [ ] Service implementation with:
  - [ ] @Slf4j logging
  - [ ] @Transactional annotations
  - [ ] Custom exception handling
  - [ ] Duplicate checking
  - [ ] Soft delete support
- [ ] Controller with:
  - [ ] @PreAuthorize on admin endpoints
  - [ ] ApiResponse wrapper on all responses
  - [ ] Admin ID from SecurityContext
  - [ ] Proper HTTP status codes
  - [ ] Cross-origin configuration
- [ ] Flyway migration for soft delete columns
- [ ] Documentation
- [ ] Postman test collection
- [ ] Backward compatibility maintained

---

## **Migration Sequence**

Execute migrations in this order with proper version numbers:

```
V56 - Religion soft delete columns
V57 - Caste soft delete columns
V58 - SubCaste soft delete columns
V59 - Country soft delete columns
V60 - State soft delete columns
V61 - City soft delete columns
... (continue for all master tables)
```

---

## **Timeline Estimate**

- **Religion Module:** ✅ COMPLETE (4-5 hours)
- **Per Additional Module:** 1.5-2 hours (using template)
- **All 29 Master Modules:** 40-50 hours total

---

## **Quick Reference**

### ApiResponse Format
```json
{
  "success": true,
  "message": "Description",
  "data": {...or [...]}
}
```

### Error Format (from GlobalExceptionHandler)
```json
{
  "timestamp": "2024-01-16T12:00:00",
  "status": 400,
  "error": "ERROR_TYPE",
  "errorCode": "ERR_CODE",
  "message": "Error description"
}
```

### HTTP Status Codes
- 200: GET, PUT, DELETE, RESTORE
- 201: POST
- 400: Validation/bad request
- 401: Unauthorized (missing/invalid JWT)
- 403: Forbidden (insufficient permissions)
- 404: Not found
- 500: Server error

### Security Annotations
```java
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")  // Admin only
@PreAuthorize("hasRole('SUPER_ADMIN')")              // Super admin only
@PreAuthorize("isAuthenticated()")                   // Any authenticated user
```

---

## **Common Mistakes to Avoid**

❌ **WRONG:**
1. Getting admin_id from request
2. Not wrapping responses in ApiResponse
3. Using @PostMapping without @PreAuthorize for mutations
4. Throwing generic RuntimeException
5. Missing logging in business logic
6. Not validating inputs in service layer
7. Inline mapping in controller
8. No soft delete support
9. Missing indexes on frequently searched columns
10. Not documenting API endpoints

✅ **RIGHT:**
1. Get admin_id from SecurityContext
2. Always wrap in ApiResponse<T>
3. Enforce role-based access with @PreAuthorize
4. Use custom exceptions (BadRequestException, ResourceNotFoundException)
5. Add @Slf4j and log important operations
6. Validate in both controller (syntax) and service (business logic)
7. Use dedicated mapper classes
8. Implement soft delete (deleted_at, deleted_by)
9. Create indexes for performance
10. Document all public methods with JavaDocs

---

**This template ensures all master data modules follow production-ready standards consistently.**

