package com.example.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    // Exchange
    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";
    public static final String NOTIFICATION_DLX = "notification.dlx";

    // Queues & Routing Keys by Priority
    public static final String QUEUE_CRITICAL = "notification.critical.queue";
    public static final String ROUTING_CRITICAL = "notification.critical";
    public static final String DLQ_CRITICAL = "notification.critical.dlq";

    public static final String QUEUE_HIGH = "notification.high.queue";
    public static final String ROUTING_HIGH = "notification.high";
    public static final String DLQ_HIGH = "notification.high.dlq";

    public static final String QUEUE_MEDIUM = "notification.medium.queue";
    public static final String ROUTING_MEDIUM = "notification.medium";
    public static final String DLQ_MEDIUM = "notification.medium.dlq";

    public static final String QUEUE_LOW = "notification.low.queue";
    public static final String ROUTING_LOW = "notification.low";
    public static final String DLQ_LOW = "notification.low.dlq";

    // Decoupled Bulk Email Queue & Routing
    public static final String QUEUE_BULK_EMAIL = "notification.bulk.email.queue";
    public static final String ROUTING_BULK_EMAIL = "notification.bulk.email";
    public static final String DLQ_BULK_EMAIL = "notification.bulk.email.dlq";

    // Legacy / Specific Subscription Expiry Queue Architecture
    public static final String SUBSCRIPTION_EXPIRY_EMAIL_QUEUE = "subscription.expiry.email";
    public static final String SUBSCRIPTION_EXPIRY_EXCHANGE = "subscription.expiry.exchange";
    public static final String SUBSCRIPTION_EXPIRY_EMAIL_ROUTING_KEY = "subscription.expiry.email.routingKey";

    public static final String SUBSCRIPTION_EXPIRY_EMAIL_DLQ = "subscription.expiry.email.dlq";
    public static final String SUBSCRIPTION_EXPIRY_EMAIL_DLX = "subscription.expiry.email.dlx";
    public static final String SUBSCRIPTION_EXPIRY_EMAIL_DLQ_ROUTING_KEY = "subscription.expiry.email.dlq.routingKey";

    // Exchanges
    @Bean
    public DirectExchange notificationExchange() {
        return new DirectExchange(NOTIFICATION_EXCHANGE, true, false);
    }

    @Bean
    public DirectExchange notificationDlx() {
        return new DirectExchange(NOTIFICATION_DLX, true, false);
    }

    // Helper to create queued args with DLX
    private Map<String, Object> createQueueArgs(String dlqRoutingKey) {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", NOTIFICATION_DLX);
        args.put("x-dead-letter-routing-key", dlqRoutingKey);
        return args;
    }

    // --- CRITICAL QUEUE ---
    @Bean
    public Queue criticalQueue() {
        return new Queue(QUEUE_CRITICAL, true, false, false, createQueueArgs(ROUTING_CRITICAL + ".dlq"));
    }

    @Bean
    public Binding criticalBinding() {
        return BindingBuilder.bind(criticalQueue()).to(notificationExchange()).with(ROUTING_CRITICAL);
    }

    @Bean
    public Queue criticalDlq() {
        return new Queue(DLQ_CRITICAL, true);
    }

    @Bean
    public Binding criticalDlqBinding() {
        return BindingBuilder.bind(criticalDlq()).to(notificationDlx()).with(ROUTING_CRITICAL + ".dlq");
    }

    // --- HIGH QUEUE ---
    @Bean
    public Queue highQueue() {
        return new Queue(QUEUE_HIGH, true, false, false, createQueueArgs(ROUTING_HIGH + ".dlq"));
    }

    @Bean
    public Binding highBinding() {
        return BindingBuilder.bind(highQueue()).to(notificationExchange()).with(ROUTING_HIGH);
    }

    @Bean
    public Queue highDlq() {
        return new Queue(DLQ_HIGH, true);
    }

    @Bean
    public Binding highDlqBinding() {
        return BindingBuilder.bind(highDlq()).to(notificationDlx()).with(ROUTING_HIGH + ".dlq");
    }

    // --- MEDIUM QUEUE ---
    @Bean
    public Queue mediumQueue() {
        return new Queue(QUEUE_MEDIUM, true, false, false, createQueueArgs(ROUTING_MEDIUM + ".dlq"));
    }

    @Bean
    public Binding mediumBinding() {
        return BindingBuilder.bind(mediumQueue()).to(notificationExchange()).with(ROUTING_MEDIUM);
    }

    @Bean
    public Queue mediumDlq() {
        return new Queue(DLQ_MEDIUM, true);
    }

    @Bean
    public Binding mediumDlqBinding() {
        return BindingBuilder.bind(mediumDlq()).to(notificationDlx()).with(ROUTING_MEDIUM + ".dlq");
    }

    // --- LOW QUEUE (BULK APP/IN-APP NOTIFICATIONS) ---
    @Bean
    public Queue lowQueue() {
        return new Queue(QUEUE_LOW, true, false, false, createQueueArgs(ROUTING_LOW + ".dlq"));
    }

    @Bean
    public Binding lowBinding() {
        return BindingBuilder.bind(lowQueue()).to(notificationExchange()).with(ROUTING_LOW);
    }

    @Bean
    public Queue lowDlq() {
        return new Queue(DLQ_LOW, true);
    }

    @Bean
    public Binding lowDlqBinding() {
        return BindingBuilder.bind(lowDlq()).to(notificationDlx()).with(ROUTING_LOW + ".dlq");
    }

    // --- BULK EMAIL QUEUE (DECOUPLED ADMIN BROADCAST EMAILS) ---
    @Bean
    public Queue bulkEmailQueue() {
        return new Queue(QUEUE_BULK_EMAIL, true, false, false, createQueueArgs(ROUTING_BULK_EMAIL + ".dlq"));
    }

    @Bean
    public Binding bulkEmailBinding() {
        return BindingBuilder.bind(bulkEmailQueue()).to(notificationExchange()).with(ROUTING_BULK_EMAIL);
    }

    @Bean
    public Queue bulkEmailDlq() {
        return new Queue(DLQ_BULK_EMAIL, true);
    }

    @Bean
    public Binding bulkEmailDlqBinding() {
        return BindingBuilder.bind(bulkEmailDlq()).to(notificationDlx()).with(ROUTING_BULK_EMAIL + ".dlq");
    }

    // --- SUBSCRIPTION EXPIRY QUEUES ---
    @Bean
    public Queue subscriptionExpiryEmailQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", SUBSCRIPTION_EXPIRY_EMAIL_DLX);
        args.put("x-dead-letter-routing-key", SUBSCRIPTION_EXPIRY_EMAIL_DLQ_ROUTING_KEY);
        return new Queue(SUBSCRIPTION_EXPIRY_EMAIL_QUEUE, true, false, false, args);
    }

    @Bean
    public DirectExchange subscriptionExpiryExchange() {
        return new DirectExchange(SUBSCRIPTION_EXPIRY_EXCHANGE, true, false);
    }

    @Bean
    public Binding subscriptionExpiryEmailBinding(Queue subscriptionExpiryEmailQueue, DirectExchange subscriptionExpiryExchange) {
        return BindingBuilder.bind(subscriptionExpiryEmailQueue)
                .to(subscriptionExpiryExchange)
                .with(SUBSCRIPTION_EXPIRY_EMAIL_ROUTING_KEY);
    }

    @Bean
    public Queue subscriptionExpiryEmailDlq() {
        return new Queue(SUBSCRIPTION_EXPIRY_EMAIL_DLQ, true);
    }

    @Bean
    public DirectExchange subscriptionExpiryEmailDlx() {
        return new DirectExchange(SUBSCRIPTION_EXPIRY_EMAIL_DLX, true, false);
    }

    @Bean
    public Binding subscriptionExpiryDlqBinding(Queue subscriptionExpiryEmailDlq, DirectExchange subscriptionExpiryEmailDlx) {
        return BindingBuilder.bind(subscriptionExpiryEmailDlq)
                .to(subscriptionExpiryEmailDlx)
                .with(SUBSCRIPTION_EXPIRY_EMAIL_DLQ_ROUTING_KEY);
    }

    @Value("${broadcast.rabbitmq.bulk-app.concurrency:15}")
    private int bulkAppConcurrency;

    @Value("${broadcast.rabbitmq.bulk-app.max-concurrency:25}")
    private int bulkAppMaxConcurrency;

    @Value("${broadcast.rabbitmq.bulk-app.prefetch:200}")
    private int bulkAppPrefetch;

    @Value("${broadcast.rabbitmq.bulk-email.concurrency:5}")
    private int bulkEmailConcurrency;

    @Value("${broadcast.rabbitmq.bulk-email.max-concurrency:10}")
    private int bulkEmailMaxConcurrency;

    @Value("${broadcast.rabbitmq.bulk-email.prefetch:50}")
    private int bulkEmailPrefetch;

    // --- CONVERTER & TEMPLATE ---
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    // --- CONTAINER FACTORIES ---
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        factory.setPrefetchCount(10);
        factory.setDefaultRequeueRejected(false);
        return factory;
    }

    // Dedicated Container Factory for Critical Queues (low prefetch, fast worker dedicated pool)
    @Bean
    public SimpleRabbitListenerContainerFactory criticalRabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        factory.setConcurrentConsumers(5);
        factory.setMaxConcurrentConsumers(15);
        factory.setPrefetchCount(1); // Immediate processing, no queuing in JVM
        factory.setDefaultRequeueRejected(false);
        return factory;
    }

    // Dedicated Container Factory for Bulk App Notifications (higher prefetch for fast DB/WebSocket throughput)
    @Bean
    public SimpleRabbitListenerContainerFactory bulkRabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        factory.setConcurrentConsumers(bulkAppConcurrency);
        factory.setMaxConcurrentConsumers(bulkAppMaxConcurrency);
        factory.setPrefetchCount(bulkAppPrefetch);
        factory.setDefaultRequeueRejected(false);
        return factory;
    }

    // Dedicated Container Factory for Bulk Email Queue (rate-controlled workers)
    @Bean
    public SimpleRabbitListenerContainerFactory bulkEmailRabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        factory.setConcurrentConsumers(bulkEmailConcurrency);
        factory.setMaxConcurrentConsumers(bulkEmailMaxConcurrency);
        factory.setPrefetchCount(bulkEmailPrefetch);
        factory.setDefaultRequeueRejected(false);
        return factory;
    }
}
