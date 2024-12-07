package com.mna.springbootsecurity.pubsub.message.redis.config;

import com.mna.springbootsecurity.base.constant.Profiles;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@Profile(Profiles.REDIS)
@RequiredArgsConstructor
public class RedisMessageConfiguration {

    private final RedisConnectionFactory redisConnectionFactory;

    private final MessageListenerAdapter emailNotificationListenerAdapter;
    private final ChannelTopic emailNotificationChannelTopic;
    private final MessageListenerAdapter otpListenerAdapter;
    private final ChannelTopic otpChannelTopic;

    @Bean
    public RedisMessageListenerContainer container() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(emailNotificationListenerAdapter, emailNotificationChannelTopic);
        container.addMessageListener(otpListenerAdapter, otpChannelTopic);
        // Add more Message listeners and channels as needed

        return container;
    }
}
