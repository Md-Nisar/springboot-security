package com.mna.springbootsecurity.event.core.publisher.rabbitmq.impl;

import com.mna.springbootsecurity.event.common.util.MessageIdGenerator;
import com.mna.springbootsecurity.event.core.publisher.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@RequiredArgsConstructor
public class RabbitMQEventPublisher<T> implements EventPublisher<T> {

    private final RabbitTemplate rabbitTemplate;
    private final String queueName;
    private final String exchange;

    @Override
    public void publish(T event) {
        MessagePostProcessor messagePostProcessor = message -> {
            message.getMessageProperties().setMessageId(MessageIdGenerator.generate(queueName));
            return message;
        };

        rabbitTemplate.convertAndSend(exchange, queueName, event, messagePostProcessor);
    }

}
