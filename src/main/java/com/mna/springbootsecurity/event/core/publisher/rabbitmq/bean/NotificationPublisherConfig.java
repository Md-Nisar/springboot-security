package com.mna.springbootsecurity.event.core.publisher.rabbitmq.bean;

import com.mna.springbootsecurity.base.constant.Profiles;
import com.mna.springbootsecurity.event.core.model.GenericEvent;
import com.mna.springbootsecurity.event.core.publisher.EventPublisher;
import com.mna.springbootsecurity.event.core.publisher.rabbitmq.impl.RabbitMQEventPublisher;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile(Profiles.RABBITMQ)
@Configuration
@Slf4j
public class NotificationPublisherConfig {


    @Value("${event.notification.queue.name}")
    private String notificationQueue;
    private final String notificationExchange = notificationQueue + "-exchange";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Bean
    Queue notificationQueue() {
        return new Queue(notificationQueue, true);
    }

    @Bean
    FanoutExchange notificationExchange() {
        return new FanoutExchange(notificationExchange);
    }

    @Bean
    Binding notificationBinding(Queue notificationQueue, FanoutExchange notificationExchange) {
        return BindingBuilder.bind(notificationQueue).to(notificationExchange);
    }

    @Bean("notificationPublisher")
    public EventPublisher<GenericEvent<String>> notificationPublisher() {
        return new RabbitMQEventPublisher<>(rabbitTemplate, notificationQueue, notificationExchange);
    }

    @PostConstruct
    public void logQueueSetup() {
        log.info("Setting up Notification Queue Publisher: {}", notificationQueue);
    }

}
