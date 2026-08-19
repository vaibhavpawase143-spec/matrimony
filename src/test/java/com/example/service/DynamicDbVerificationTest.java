package com.example.service;

import com.example.model.BroadcastJob;
import com.example.model.BroadcastJobStatus;
import com.example.model.NotificationType;
import com.example.repository.BroadcastJobRepository;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"broadcast.test-mode=false"})
class DynamicDbVerificationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminBroadcastService broadcastService;

    @Autowired
    private BroadcastJobRepository broadcastJobRepository;

    @Test
    @DisplayName("Verify Dynamic DB Recipient Calculation and Production Broadcast Execution")
    void testDynamicDbRecipientCalculation() throws InterruptedException {
        // 1. Query exact eligible users count in PostgreSQL
        long dbEligibleCount = userRepository.countByIsActiveTrueAndIsDeletedFalse();
        System.out.println("=================================================");
        System.out.println("[VERIFICATION] PostgreSQL Active & Non-deleted User Count = " + dbEligibleCount);
        System.out.println("=================================================");

        assertTrue(dbEligibleCount > 0, "Database must contain eligible users");

        long startTime = System.currentTimeMillis();

        // 2. Initiate Broadcast
        Long jobId = broadcastService.initiateBroadcast(
                "Dynamic Verification Broadcast",
                "Verifying dynamic DB recipient calculation and high-throughput execution",
                NotificationType.ANNOUNCEMENT,
                1L
        );

        assertNotNull(jobId);

        // 3. Fetch created job
        BroadcastJob job = broadcastJobRepository.findById(jobId).orElseThrow();

        System.out.println("[VERIFICATION] Created Job ID: " + job.getId());
        System.out.println("[VERIFICATION] Job totalRecipients: " + job.getTotalRecipients());
        System.out.println("[VERIFICATION] Job isTestMode: " + job.getIsTestMode());

        // Exact match invariant check
        assertEquals(dbEligibleCount, job.getTotalRecipients(), "BroadcastJob.totalRecipients MUST exactly match DB eligible user count");
        assertFalse(Boolean.TRUE.equals(job.getIsTestMode()), "Production job must have isTestMode = false");

        // 4. Wait for worker processing to finish
        int maxWaitSeconds = 60;
        int waited = 0;
        while (waited < maxWaitSeconds) {
            job = broadcastJobRepository.findById(jobId).orElseThrow();
            if (job.getStatus() == BroadcastJobStatus.COMPLETED
                    || job.getStatus() == BroadcastJobStatus.COMPLETED_WITH_FAILURES
                    || job.getStatus() == BroadcastJobStatus.FAILED
                    || job.getEnqueuedRecipients() >= dbEligibleCount) {
                break;
            }
            TimeUnit.SECONDS.sleep(1);
            waited++;
        }

        long endTime = System.currentTimeMillis();
        double totalSeconds = Math.max(0.001, (endTime - startTime) / 1000.0);
        double throughput = job.getProcessedRecipients() / totalSeconds;

        System.out.println("=================================================");
        System.out.println("[VERIFICATION RESULTS]");
        System.out.println("DB Eligible Count: " + dbEligibleCount);
        System.out.println("Job TotalRecipients: " + job.getTotalRecipients());
        System.out.println("Job EnqueuedRecipients: " + job.getEnqueuedRecipients());
        System.out.println("Job ProcessedRecipients: " + job.getProcessedRecipients());
        System.out.println("Job SuccessfulRecipients: " + job.getSuccessfulRecipients());
        System.out.println("Job FailedRecipients: " + job.getFailedRecipients());
        System.out.println("Job Final Status: " + job.getStatus());
        System.out.println("Execution Time: " + String.format("%.2f", totalSeconds) + " seconds");
        System.out.println("Throughput: " + String.format("%.2f", throughput) + " rec/sec");
        System.out.println("=================================================");

        assertEquals(dbEligibleCount, job.getTotalRecipients());
        assertTrue(job.getProcessedRecipients() <= job.getEnqueuedRecipients());
        assertTrue(job.getEnqueuedRecipients() <= job.getTotalRecipients());
        assertEquals(job.getProcessedRecipients(), job.getSuccessfulRecipients() + job.getFailedRecipients());
    }
}
