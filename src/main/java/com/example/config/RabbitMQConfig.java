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

    public static final String NOTIFICATION_QUEUE = "notification.queue";
    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";
    public static final String NOTIFICATION_ROUTING_KEY = "notification.routingKey";

    public static final String NOTIFICATION_DLQ = "notification.dlq";
    public static final String NOTIFICATION_DLX = "notification.dlx";
    public static final String NOTIFICATION_DLQ_ROUTING_KEY = "notification.dlq.routingKey";

    // Subscription Expiry Queue Architecture
    public static final String SUBSCRIPTION_EXPIRY_EMAIL_QUEUE = "subscription.expiry.email";
    public static final String SUBSCRIPTION_EXPIRY_EXCHANGE = "subscription.expiry.exchange";
    public static final String SUBSCRIPTION_EXPIRY_EMAIL_ROUTING_KEY = "subscription.expiry.email.routingKey";

    public static final String SUBSCRIPTION_EXPIRY_EMAIL_DLQ = "subscription.expiry.email.dlq";
    public static final String SUBSCRIPTION_EXPIRY_EMAIL_DLX = "subscription.expiry.email.dlx";
    public static final String SUBSCRIPTION_EXPIRY_EMAIL_DLQ_ROUTING_KEY = "subscription.expiry.email.dlq.routingKey";

    @Bean
    public Queue notificationQueue() {
        Map<String, Object> args = new HashMap<>();
        // Configure Dead Letter Exchange (DLX) for failed messages
        args.put("x-dead-letter-exchange", NOTIFICATION_DLX);
        args.put("x-dead-letter-routing-key", NOTIFICATION_DLQ_ROUTING_KEY);
        return new Queue(NOTIFICATION_QUEUE, true, false, false, args);
    }

    @Bean
    public DirectExchange notificationExchange() {
        return new DirectExchange(NOTIFICATION_EXCHANGE);
    }

    @Bean
    public Binding notificationBinding(Queue notificationQueue, DirectExchange notificationExchange) {
        return BindingBuilder.bind(notificationQueue)
                .to(notificationExchange)
                .with(NOTIFICATION_ROUTING_KEY);
    }

    // Dead Letter Queue Configuration
    @Bean
    public Queue notificationDlq() {
        return new Queue(NOTIFICATION_DLQ, true);
    }

    @Bean
    public DirectExchange notificationDlx() {
        return new DirectExchange(NOTIFICATION_DLX);
    }

    @Bean
    public Binding dlqBinding(Queue notificationDlq, DirectExchange notificationDlx) {
        return BindingBuilder.bind(notificationDlq)
                .to(notificationDlx)
                .with(NOTIFICATION_DLQ_ROUTING_KEY);
    }

    // Subscription Expiry Beans
    @Bean
    public Queue subscriptionExpiryEmailQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", SUBSCRIPTION_EXPIRY_EMAIL_DLX);
        args.put("x-dead-letter-routing-key", SUBSCRIPTION_EXPIRY_EMAIL_DLQ_ROUTING_KEY);
        return new Queue(SUBSCRIPTION_EXPIRY_EMAIL_QUEUE, true, false, false, args);
    }

    @Bean
    public DirectExchange subscriptionExpiryExchange() {
        return new DirectExchange(SUBSCRIPTION_EXPIRY_EXCHANGE);
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
        return new DirectExchange(SUBSCRIPTION_EXPIRY_EMAIL_DLX);
    }

    @Bean
    public Binding subscriptionExpiryDlqBinding(Queue subscriptionExpiryEmailDlq, DirectExchange subscriptionExpiryEmailDlx) {
        return BindingBuilder.bind(subscriptionExpiryEmailDlq)
                .to(subscriptionExpiryEmailDlx)
                .with(SUBSCRIPTION_EXPIRY_EMAIL_DLQ_ROUTING_KEY);
    }

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

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        factory.setConcurrentConsumers(5);
        factory.setMaxConcurrentConsumers(20);
        return factory;
    }
}
