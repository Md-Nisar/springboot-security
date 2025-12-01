package com.mna.springbootsecurity.pubsub.message.redis.publisher;

import com.mna.springbootsecurity.base.vo.message.EmailNotificationData;
import com.mna.springbootsecurity.pubsub.message.MessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Locale;

@RequiredArgsConstructor
@Slf4j
public class RedisMessagePublisher implements MessagePublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final String channel;

    @Override
    public void publish(Object message) {
        String messageType = message.getClass().getSimpleName();
        String messageSummary = formatMessageSummary(message);

        log.info("Message Publishing:: of type '{}' to channel '{}' {}", messageType, channel, messageSummary);
        redisTemplate.convertAndSend(channel, message);
        log.info("Message published:: of type '{}' to channel '{}'", messageType, channel);
    }

    private String formatMessageSummary(Object message) {
        // Example implementation: log specific properties or a summary
        // Adjust based on the actual structure of your message
        if (message instanceof EmailNotificationData emailMessage) {
            return String.format(Locale.ROOT, "Email to: %s, Type: %s, Data: %s",
                    emailMessage.getEmailAddress(), emailMessage.getType(), emailMessage.getData());
        }
        // Add other message types as needed
        return "Message Summary";
    }
}
