package com.example.serviceimpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminBroadcastAsyncWorker {

    private final AdminBroadcastServiceImpl broadcastServiceImpl;

    @Async("bulkEmailExecutor")
    public void processBroadcastJobAsync(Long broadcastJobId) {
        log.info("[ASYNC BROADCAST WORKER START] JobID={} executing on thread: {}", broadcastJobId, Thread.currentThread().getName());
        broadcastServiceImpl.processBroadcastJob(broadcastJobId);
    }
}
