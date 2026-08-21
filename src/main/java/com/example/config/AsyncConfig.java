package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "applicationTaskExecutor")
    @Primary
    public Executor applicationTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }

    /**
     * Dedicated Executor for CRITICAL Transactional Emails (OTP, Forgot Password, Verification, Security).
     * Guaranteed zero queueing delay behind bulk broadcast emails.
     */
    @Bean(name = "criticalEmailExecutor")
    public Executor criticalEmailExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(30);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("CritEmail-");
        executor.initialize();
        return executor;
    }

    /**
     * Dedicated Executor for BULK Broadcast Emails.
     * Fully isolated from critical transactional emails.
     */
    @Bean(name = "bulkEmailExecutor")
    public Executor bulkEmailExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(15);
        executor.setQueueCapacity(10000);
        executor.setThreadNamePrefix("BulkEmail-");
        executor.initialize();
        return executor;
    }

    /**
     * Alias for emailTaskExecutor pointing to criticalEmailExecutor for default compatibility.
     */
    @Bean(name = "emailTaskExecutor")
    public Executor emailTaskExecutor() {
        return criticalEmailExecutor();
    }
}