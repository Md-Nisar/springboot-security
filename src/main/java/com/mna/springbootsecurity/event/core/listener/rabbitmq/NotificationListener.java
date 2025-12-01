package com.mna.springbootsecurity.event.core.listener.rabbitmq;

import com.mna.springbootsecurity.base.constant.Profiles;
import com.mna.springbootsecurity.event.core.listener.EventListener;
import com.mna.springbootsecurity.event.core.model.GenericEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile(Profiles.RABBITMQ)
@Component
public class NotificationListener implements EventListener<GenericEvent<String>> {

    @RabbitListener(queues = "${event.notification.queue.name}")
    @Override
    public void consume(GenericEvent<String> event) {

    }
}
