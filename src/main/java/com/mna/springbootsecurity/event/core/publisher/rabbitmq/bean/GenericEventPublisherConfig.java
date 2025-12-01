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

import java.util.Map;


@Profile(Profiles.RABBITMQ)
@Configuration
@Slf4j
public class GenericEventPublisherConfig {


    @Value("${event.queue.name}")
    private String eventQueue;
    private final String eventExchange = eventQueue + "-exchange";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Bean
    Queue eventQueue() {
        return new Queue(eventQueue, true);
    }

    @Bean
    FanoutExchange eventExchange() {
        return new FanoutExchange(eventExchange);
    }

    @Bean
    Binding eventBinding(Queue eventQueue, FanoutExchange eventExchange) {
        return BindingBuilder.bind(eventQueue).to(eventExchange);
    }

    @Bean("eventPublisher")
    public EventPublisher<GenericEvent<Map<String, Object>>> eventPublisher() {
        return new RabbitMQEventPublisher<>(rabbitTemplate, eventQueue, eventExchange);
    }

    @PostConstruct
    public void logQueueSetup() {
        log.info("Setting up event Queue Publisher: {}", eventQueue);
    }

}
