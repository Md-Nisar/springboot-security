package com.mna.springbootsecurity.pubsub.message.redis.subscriber;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mna.springbootsecurity.base.vo.message.EmailNotificationData;
import com.mna.springbootsecurity.pubsub.message.redis.handler.EmailNotificationHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final EmailNotificationHandler emailNotificationHandler;

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        String messageBody = new String(message.getBody());
        try {
            EmailNotificationData emailNotification = objectMapper.readValue(messageBody, new TypeReference<EmailNotificationData>() {});
            log.info("Message Received:: of type 'EmailNotification' {}", formatMessageSummary(emailNotification));
            emailNotificationHandler.handleEmailNotification(emailNotification);
        } catch (JsonProcessingException e) {
            log.error("Failed to process EmailNotification message", e);
        }
    }

    private String formatMessageSummary(EmailNotificationData emailNotification) {
        // Example implementation: log specific properties or a summary
        return String.format("Email to: %s, Type: %s, Data: %s",
                emailNotification.getEmailAddress(), emailNotification.getType(), emailNotification.getData());
    }
}
