package com.example.service;

import com.example.dto.response.AdminDashboardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardCacheService {

    private final CacheManager cacheManager;

    private static final String CACHE_NAME = "admin-dashboard";
    private static final String CACHE_KEY = "overview";

    public AdminDashboardDTO getDashboard() {

        var cache = cacheManager.getCache(CACHE_NAME);

        if (cache == null) {
            return null;
        }

        return cache.get(CACHE_KEY, AdminDashboardDTO.class);
    }

    public void saveDashboard(AdminDashboardDTO dto) {

        var cache = cacheManager.getCache(CACHE_NAME);

        if (cache != null) {
            cache.put(CACHE_KEY, dto);
        }
    }

    public void clearDashboard() {

        var cache = cacheManager.getCache(CACHE_NAME);

        if (cache != null) {
            cache.evict(CACHE_KEY);
        }
    }
}