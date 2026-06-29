package com.example.serviceimpl;

import com.example.dto.request.ReportFilterDTO;
import com.example.dto.response.AdminReportResponseDTO;
import com.example.dto.response.AdminReportStatsDTO;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.AdminReportMapper;
import com.example.model.ReportStatus;
import com.example.model.UserReport;
import com.example.repository.UserReportRepository;
import com.example.service.AdminReportService;
import com.example.specification.ReportSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminReportServiceImpl implements AdminReportService {

    private final UserReportRepository userReportRepository;
    private final AdminReportMapper adminReportMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<AdminReportResponseDTO> getAllReports(
            ReportFilterDTO filter,
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<UserReport> reports = userReportRepository.findAll(
                ReportSpecification.getReports(filter),
                pageable
        );

        return reports.map(adminReportMapper::toDTO);
    }
    @Override
    @Transactional(readOnly = true)
    public AdminReportResponseDTO getReportById(Long id) {

        UserReport report = userReportRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Report not found with ID: " + id
                        ));

        return adminReportMapper.toDTO(report);

    }

    @Override
    @Transactional(readOnly =true)
    public List<AdminReportResponseDTO> getPendingReports() {

        List<UserReport> reports =
                userReportRepository.findByStatus(ReportStatus.PENDING);

        return reports.stream()
                .map(adminReportMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AdminReportResponseDTO markAsReviewed(Long id) {

        UserReport report = userReportRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Report not found with ID: " + id
                        ));

        report.setStatus(ReportStatus.UNDER_REVIEW);

        UserReport updatedReport = userReportRepository.save(report);

        return adminReportMapper.toDTO(updatedReport);
    }

    @Override
    public AdminReportResponseDTO approveReport(Long id) {

        UserReport report = userReportRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Report not found with ID: " + id
                        ));

        if (report.getStatus() != ReportStatus.UNDER_REVIEW) {
            throw new IllegalArgumentException(
                    "Only reports under review can be approved."
            );
        }

        report.setStatus(ReportStatus.APPROVED);

        UserReport updatedReport = userReportRepository.save(report);

        return adminReportMapper.toDTO(updatedReport);
    }

    @Override
    public AdminReportResponseDTO rejectReport(Long id) {

        UserReport report = userReportRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Report not found with ID: " + id
                        ));

        if (report.getStatus() != ReportStatus.UNDER_REVIEW) {
            throw new IllegalArgumentException(
                    "Only reports under review can be rejected."
            );
        }

        report.setStatus(ReportStatus.REJECTED);

        UserReport updatedReport = userReportRepository.save(report);

        return adminReportMapper.toDTO(updatedReport);
    }

    @Override
    public AdminReportStatsDTO getReportStatistics() {
        return null;
    }
}