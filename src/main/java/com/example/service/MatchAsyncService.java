package com.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchAsyncService {

    private final MatchService matchService;

    @Async
    public void preloadMatches(Long userId) {

        log.info("🔥 Preloading matches for user {}", userId);

        matchService.getMatches(userId, 0, 10);
        matchService.getTopMatches(userId, 10);

        log.info("✅ Preloading done for user {}", userId);
    }
}