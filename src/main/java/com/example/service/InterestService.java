package com.example.service;

import com.example.dto.request.InterestRequestDTO;
import com.example.dto.response.InterestResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InterestService {

    InterestResponseDTO sendInterest(InterestRequestDTO request);

    InterestResponseDTO updateStatus(Long id, String status);

    void delete(Long id);

    InterestResponseDTO getById(Long id);

    List<InterestResponseDTO> getBySender(Long senderId);

    List<InterestResponseDTO> getBySenderAndStatus(Long senderId, String status);

    List<InterestResponseDTO> getByReceiver(Long receiverId);

    List<InterestResponseDTO> getByReceiverAndStatus(Long receiverId, String status);

    InterestResponseDTO getBySenderAndReceiver(Long senderId, Long receiverId);

    List<InterestResponseDTO> getByStatus(String status);
    // =====================================================
// PAGINATION (1M+ USERS)
// =====================================================

    Page<InterestResponseDTO> getBySender(
            Long senderId,
            Pageable pageable
    );

    Page<InterestResponseDTO> getByReceiver(
            Long receiverId,
            Pageable pageable
    );

    Page<InterestResponseDTO> getBySenderAndStatus(
            Long senderId,
            String status,
            Pageable pageable
    );

    Page<InterestResponseDTO> getByReceiverAndStatus(
            Long receiverId,
            String status,
            Pageable pageable
    );

    Page<InterestResponseDTO> getByStatus(
            String status,
            Pageable pageable
    );
}