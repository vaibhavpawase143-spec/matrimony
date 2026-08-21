package com.example.service;

import com.example.dto.request.SuccessStoryCreateRequestDTO;
import com.example.dto.request.SuccessStoryUpdateRequestDTO;
import com.example.dto.response.SuccessStoryResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SuccessStoryService {

    SuccessStoryResponseDTO createSuccessStory(SuccessStoryCreateRequestDTO dto, Long adminId);

    SuccessStoryResponseDTO updateSuccessStory(Long id, SuccessStoryUpdateRequestDTO dto, Long adminId);

    SuccessStoryResponseDTO getSuccessStoryById(Long id);

    SuccessStoryResponseDTO getPublicSuccessStoryById(Long id);

    Page<SuccessStoryResponseDTO> getAdminSuccessStories(String search, Boolean published, Pageable pageable);

    Page<SuccessStoryResponseDTO> getPublishedSuccessStories(Pageable pageable);

    void deleteSuccessStory(Long id, Long adminId);

    SuccessStoryResponseDTO publishSuccessStory(Long id, Long adminId);

    SuccessStoryResponseDTO unpublishSuccessStory(Long id, Long adminId);
}
