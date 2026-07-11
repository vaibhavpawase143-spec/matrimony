package com.example.service;

import com.example.dto.request.InterestRequestDTO;
import com.example.dto.response.InterestResponseDTO;

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
}