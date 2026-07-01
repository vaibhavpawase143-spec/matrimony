package com.example.serviceimpl;

import com.example.dto.request.FaqRequestDTO;
import com.example.dto.response.FaqResponseDTO;
import com.example.mapper.FaqMapper;
import com.example.model.Admin;
import com.example.model.Faq;
import com.example.repository.FaqRepository;
import com.example.service.AdminAuditLogService;
import com.example.service.CurrentAdminService;
import com.example.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FaqServiceImpl implements FaqService {

    private final FaqRepository faqRepository;

    private final CurrentAdminService currentAdminService;

    private final AdminAuditLogService auditLogService;

    @Override
    public FaqResponseDTO create(FaqRequestDTO dto) {

        if (faqRepository.existsByQuestionIgnoreCase(dto.getQuestion())) {
            throw new RuntimeException("FAQ already exists with the same question");
        }

        Admin currentAdmin = currentAdminService.getCurrentAdmin();

        Faq faq = FaqMapper.toEntity(dto);

        faq.setCreatedBy(currentAdmin);
        faq.setUpdatedBy(currentAdmin);

        Faq savedFaq = faqRepository.save(faq);

        auditLogService.log(
                currentAdmin.getId(),
                "FAQ_MANAGEMENT",
                "FAQ_CREATED",
                "FAQ",
                savedFaq.getId(),
                "Created FAQ: " + savedFaq.getQuestion(),
                null,
                "Question=" + savedFaq.getQuestion(),
                "SYSTEM",
                "SYSTEM"
        );

        return FaqMapper.toDTO(savedFaq);
    }

    @Override
    public FaqResponseDTO update(Long id, FaqRequestDTO dto) {

        Faq faq = faqRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() ->
                        new RuntimeException("FAQ not found"));

        Admin currentAdmin = currentAdminService.getCurrentAdmin();

        String oldValue =
                "Question=" + faq.getQuestion()
                        + ", Published=" + faq.getPublished()
                        + ", Active=" + faq.getIsActive();

        faq.setQuestion(dto.getQuestion());
        faq.setAnswer(dto.getAnswer());
        faq.setDisplayOrder(dto.getDisplayOrder());
        faq.setPublished(dto.getPublished());
        faq.setIsActive(dto.getIsActive());

        faq.setUpdatedBy(currentAdmin);

        Faq updatedFaq = faqRepository.save(faq);

        String newValue =
                "Question=" + updatedFaq.getQuestion()
                        + ", Published=" + updatedFaq.getPublished()
                        + ", Active=" + updatedFaq.getIsActive();

        auditLogService.log(
                currentAdmin.getId(),
                "FAQ_MANAGEMENT",
                "FAQ_UPDATED",
                "FAQ",
                updatedFaq.getId(),
                "Updated FAQ: " + updatedFaq.getQuestion(),
                oldValue,
                newValue,
                "SYSTEM",
                "SYSTEM"
        );

        return FaqMapper.toDTO(updatedFaq);
    }

    @Override
    @Transactional(readOnly = true)
    public FaqResponseDTO getById(Long id) {

        Faq faq = faqRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() ->
                        new RuntimeException("FAQ not found"));

        return FaqMapper.toDTO(faq);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FaqResponseDTO> getAll() {

        return faqRepository.findByIsActiveTrueOrderByDisplayOrderAsc()
                .stream()
                .map(FaqMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FaqResponseDTO> getPublishedFaqs() {

        return faqRepository
                .findByPublishedTrueAndIsActiveTrueOrderByDisplayOrderAsc()
                .stream()
                .map(FaqMapper::toDTO)
                .toList();
    }

    @Override
    public void publish(Long id) {

        Faq faq = faqRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() ->
                        new RuntimeException("FAQ not found"));

        Admin currentAdmin = currentAdminService.getCurrentAdmin();

        faq.setPublished(true);
        faq.setUpdatedBy(currentAdmin);

        faqRepository.save(faq);

        auditLogService.log(
                currentAdmin.getId(),
                "FAQ_MANAGEMENT",
                "FAQ_PUBLISHED",
                "FAQ",
                faq.getId(),
                "Published FAQ: " + faq.getQuestion(),
                "Published=false",
                "Published=true",
                "SYSTEM",
                "SYSTEM"
        );
    }

    @Override
    public void unpublish(Long id) {

        Faq faq = faqRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() ->
                        new RuntimeException("FAQ not found"));

        Admin currentAdmin = currentAdminService.getCurrentAdmin();

        faq.setPublished(false);
        faq.setUpdatedBy(currentAdmin);

        faqRepository.save(faq);

        auditLogService.log(
                currentAdmin.getId(),
                "FAQ_MANAGEMENT",
                "FAQ_UNPUBLISHED",
                "FAQ",
                faq.getId(),
                "Unpublished FAQ: " + faq.getQuestion(),
                "Published=true",
                "Published=false",
                "SYSTEM",
                "SYSTEM"
        );
    }

    @Override
    public void delete(Long id) {

        Faq faq = faqRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() ->
                        new RuntimeException("FAQ not found"));

        Admin currentAdmin = currentAdminService.getCurrentAdmin();

        faq.setIsActive(false);
        faq.setUpdatedBy(currentAdmin);

        faqRepository.save(faq);

        auditLogService.log(
                currentAdmin.getId(),
                "FAQ_MANAGEMENT",
                "FAQ_DELETED",
                "FAQ",
                faq.getId(),
                "Soft deleted FAQ: " + faq.getQuestion(),
                "Active=true",
                "Active=false",
                "SYSTEM",
                "SYSTEM"
        );
    }
}