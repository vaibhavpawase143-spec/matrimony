package com.example.service;

import com.example.exception.PremiumRequiredException;
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

        // =====================================================
        // PRELOAD NORMAL MATCHES
        // =====================================================

        try {

            matchService.getMatches(userId, 0, 10);

            log.info(
                    "✅ Matches preloaded successfully for user {}",
                    userId
            );

        } catch (PremiumRequiredException e) {

            // Non-premium users are not allowed to access matches.
            // Do not treat this as an application error.
            log.info(
                    "ℹ️ Skipping match preload for non-premium user {}",
                    userId
            );

        } catch (Exception e) {

            log.error(
                    "❌ Failed to preload matches for user {}",
                    userId,
                    e
            );
        }

        // =====================================================
        // PRELOAD TOP MATCHES
        // =====================================================

        try {

            matchService.getTopMatches(userId, 0, 20);

            log.info(
                    "✅ Top matches preloaded successfully for user {}",
                    userId
            );

        } catch (PremiumRequiredException e) {

            // Non-premium users cannot access top matches.
            log.info(
                    "ℹ️ Skipping top-match preload for non-premium user {}",
                    userId
            );

        } catch (Exception e) {

            log.error(
                    "❌ Failed to preload top matches for user {}",
                    userId,
                    e
            );
        }

        log.info("🏁 Match preload process completed for user {}", userId);
    }
}