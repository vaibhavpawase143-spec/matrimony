package com.example.controller.master;

import com.example.dto.response.ApiResponse;
import com.example.service.MasterDataCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/master")
@RequiredArgsConstructor
public class BulkMasterDataController {

    private final MasterDataCacheService masterDataCacheService;

    @GetMapping("/all")
    public ApiResponse<Map<String, Object>> getAllMasterData() {
        return ApiResponse.success(
                "All master data loaded successfully",
                masterDataCacheService.getAllMasterData()
        );
    }
}
