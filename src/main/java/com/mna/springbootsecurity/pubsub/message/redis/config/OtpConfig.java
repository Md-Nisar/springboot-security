package com.mna.springbootsecurity.pubsub.message.redis.config;

import com.mna.springbootsecurity.base.constant.Profiles;
import com.mna.springbootsecurity.base.property.RedisPubSubProperties;
import com.mna.springbootsecurity.pubsub.message.redis.subscriber.OtpListener;
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
public class OtpConfig {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisPubSubProperties properties;
    private final OtpListener otpListener;

    @Bean
    @Qualifier("otpPublisher")
    public RedisMessagePublisher otpPublisher() {
        return new RedisMessagePublisher(redisTemplate, properties.getOtpChannel());
    }

    @Bean
    @Qualifier("otpListenerAdapter")
    public MessageListenerAdapter otpListenerAdapter() {
        return new MessageListenerAdapter(otpListener);
    }

    @Bean
    @Qualifier("otpChannelTopic")
    public ChannelTopic otpChannelTopic() {
        return new ChannelTopic(properties.getOtpChannel());
    }
}
