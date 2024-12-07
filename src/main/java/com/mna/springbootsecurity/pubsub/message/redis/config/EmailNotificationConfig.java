package com.mna.springbootsecurity.pubsub.message.redis.config;

import com.mna.springbootsecurity.base.constant.Profiles;
import com.mna.springbootsecurity.base.property.RedisPubSubProperties;
import com.mna.springbootsecurity.pubsub.message.redis.subscriber.EmailNotificationListener;
import com.mna.springbootsecurity.pubsub.message.redis.publisher.RedisMessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@Profile(Profiles.REDIS)
@RequiredArgsConstructor
public class EmailNotificationConfig {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisPubSubProperties properties;
    private final EmailNotificationListener emailNotificationListener;

    @Bean
    @Qualifier("emailNotificationPublisher")
    public RedisMessagePublisher emailNotificationPublisher() {
        return new RedisMessagePublisher(redisTemplate, properties.getEmailNotificationChannel());
    }

    @Bean
    @Qualifier("emailNotificationListenerAdapter")
    public MessageListenerAdapter emailNotificationListenerAdapter() {
        return new MessageListenerAdapter(emailNotificationListener);
    }

    @Bean
    @Qualifier("emailNotificationChannelTopic")
    public ChannelTopic emailNotificationChannelTopic() {
        return new ChannelTopic(properties.getEmailNotificationChannel());
    }

}
