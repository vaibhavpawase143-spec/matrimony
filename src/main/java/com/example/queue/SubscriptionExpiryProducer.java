package com.example.queue;

import com.example.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionExpiryProducer {

    private final RabbitTemplate rabbitTemplate;

    public void enqueueJob(SubscriptionExpiryJobPayload payload) {
        log.info("[SUB EXPIRY ENQUEUED] JobID={} | SubID={} | UserID={} | Key={}",
                payload.getJobId(), payload.getSubscriptionId(), payload.getUserId(), payload.getIdempotencyKey());

        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.SUBSCRIPTION_EXPIRY_EXCHANGE,
                    RabbitMQConfig.SUBSCRIPTION_EXPIRY_EMAIL_ROUTING_KEY,
                    payload
            );
        } catch (Exception e) {
            log.error("[SUB EXPIRY ENQUEUE FAILED] JobID={} | SubID={} | Error={}",
                    payload.getJobId(), payload.getSubscriptionId(), e.getMessage(), e);
            throw new RuntimeException("Failed to enqueue subscription expiry job: " + e.getMessage(), e);
        }
    }
}
