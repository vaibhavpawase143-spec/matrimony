package com.example.controller.user;

import com.example.dto.request.ProfileSearchRequestDTO;
import com.example.dto.response.PageResponse;
import com.example.dto.response.ProfileSearchResultDTO;
import com.example.service.ProfileSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ProfileSearchController {

    private final ProfileSearchService profileSearchService;

    @GetMapping("/profiles")
    public ResponseEntity<PageResponse<ProfileSearchResultDTO>> searchProfilesGet(
            @ModelAttribute ProfileSearchRequestDTO request
    ) {
        return ResponseEntity.ok(profileSearchService.searchProfiles(request));
    }

    @PostMapping("/profiles")
    public ResponseEntity<PageResponse<ProfileSearchResultDTO>> searchProfilesPost(
            @RequestBody ProfileSearchRequestDTO request
    ) {
        return ResponseEntity.ok(profileSearchService.searchProfiles(request));
    }
}
