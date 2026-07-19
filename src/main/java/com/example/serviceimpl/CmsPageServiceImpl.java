package com.example.serviceimpl;

import com.example.dto.request.CmsPageRequestDTO;
import com.example.dto.response.CmsPageResponseDTO;
import com.example.mapper.CmsPageMapper;
import com.example.model.Admin;
import com.example.model.CmsPage;
import com.example.repository.CmsPageRepository;
import com.example.service.AdminAuditLogService;
import com.example.service.CmsPageService;
import com.example.service.CurrentAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CmsPageServiceImpl implements CmsPageService {

    private final CmsPageRepository cmsPageRepository;

    private final CurrentAdminService currentAdminService;

    private final AdminAuditLogService auditLogService;

    @Override
    public CmsPageResponseDTO create(CmsPageRequestDTO dto) {

        if (cmsPageRepository.existsByPageKey(dto.getPageKey())) {
            throw new RuntimeException("CMS page already exists with page key: " + dto.getPageKey());
        }

        Admin currentAdmin = currentAdminService.getCurrentAdmin();

        CmsPage page = CmsPageMapper.toEntity(dto);

        page.setCreatedBy(currentAdmin);
        page.setUpdatedBy(currentAdmin);

        CmsPage savedPage = cmsPageRepository.save(page);

        auditLogService.log(
                currentAdmin.getId(),
                "CMS_MANAGEMENT",
                "CMS_CREATED",
                "CMS_PAGE",
                savedPage.getId(),
                "Created CMS page: " + savedPage.getTitle(),
                null,
                "PageKey=" + savedPage.getPageKey()

        );

        return CmsPageMapper.toDTO(savedPage);
    }

    @Override
    public CmsPageResponseDTO update(Long id, CmsPageRequestDTO dto) {

        CmsPage page = cmsPageRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("CMS page not found"));

        Admin currentAdmin = currentAdminService.getCurrentAdmin();

        String oldValue =
                "Title=" + page.getTitle()
                        + ", Published=" + page.getPublished()
                        + ", Active=" + page.getIsActive();

        page.setTitle(dto.getTitle());
        page.setContent(dto.getContent());
        page.setPublished(dto.getPublished());
        page.setIsActive(dto.getIsActive());

        page.setUpdatedBy(currentAdmin);

        CmsPage updatedPage = cmsPageRepository.save(page);

        String newValue =
                "Title=" + updatedPage.getTitle()
                        + ", Published=" + updatedPage.getPublished()
                        + ", Active=" + updatedPage.getIsActive();

        auditLogService.log(
                currentAdmin.getId(),
                "CMS_MANAGEMENT",
                "CMS_UPDATED",
                "CMS_PAGE",
                updatedPage.getId(),
                "Updated CMS page: " + updatedPage.getTitle(),
                oldValue,
                newValue

        );

        return CmsPageMapper.toDTO(updatedPage);
    }

    @Override
    @Transactional(readOnly = true)
    public CmsPageResponseDTO getById(Long id) {

        CmsPage page = cmsPageRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("CMS page not found"));

        return CmsPageMapper.toDTO(page);
    }

    @Override
    @Transactional(readOnly = true)
    public CmsPageResponseDTO getByPageKey(String pageKey) {

        CmsPage page = cmsPageRepository
                .findByPageKeyAndPublishedTrueAndIsActiveTrue(pageKey)
                .orElseThrow(() ->
                        new RuntimeException("CMS page not found"));

        return CmsPageMapper.toDTO(page);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CmsPageResponseDTO> getAll() {

        return cmsPageRepository.findAll()
                .stream()
                .map(CmsPageMapper::toDTO)
                .toList();
    }
    @Override
    public void publish(Long id) {

        CmsPage page = cmsPageRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("CMS page not found"));

        Admin currentAdmin = currentAdminService.getCurrentAdmin();

        page.setPublished(true);
        page.setUpdatedBy(currentAdmin);

        cmsPageRepository.save(page);

        auditLogService.log(
                currentAdmin.getId(),
                "CMS_MANAGEMENT",
                "CMS_PUBLISHED",
                "CMS_PAGE",
                page.getId(),
                "Published CMS page: " + page.getTitle(),
                "Published=false",
                "Published=true"

        );
    }

    @Override
    public void unpublish(Long id) {

        CmsPage page = cmsPageRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("CMS page not found"));

        Admin currentAdmin = currentAdminService.getCurrentAdmin();

        page.setPublished(false);
        page.setUpdatedBy(currentAdmin);

        cmsPageRepository.save(page);

        auditLogService.log(
                currentAdmin.getId(),
                "CMS_MANAGEMENT",
                "CMS_UNPUBLISHED",
                "CMS_PAGE",
                page.getId(),
                "Unpublished CMS page: " + page.getTitle(),
                "Published=true",
                "Published=false"
        );
    }

    @Override
    public void delete(Long id) {

        CmsPage page = cmsPageRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("CMS page not found"));

        Admin currentAdmin = currentAdminService.getCurrentAdmin();

        page.setIsActive(false);
        page.setUpdatedBy(currentAdmin);

        cmsPageRepository.save(page);

        auditLogService.log(
                currentAdmin.getId(),
                "CMS_MANAGEMENT",
                "CMS_DELETED",
                "CMS_PAGE",
                page.getId(),
                "Soft deleted CMS page: " + page.getTitle(),
                "Active=true",
                "Active=false"
        );
    }
}