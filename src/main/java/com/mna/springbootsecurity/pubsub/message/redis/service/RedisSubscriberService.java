package com.mna.springbootsecurity.pubsub.message.redis.service;

import com.mna.springbootsecurity.base.constant.Profiles;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Profile(Profiles.REDIS)
@RequiredArgsConstructor
public class RedisSubscriberService {

    private final RedisMessageListenerContainer container;

    private final Map<String, MessageListener> subscribers = new HashMap<>();

    public void registerSubscriber(String channel, MessageListener messageListener) {
        subscribers.put(channel, messageListener);
        container.addMessageListener(messageListener, new ChannelTopic(channel));
    }

    public void unregisterSubscriber(String channel) {
        MessageListener messageListener = subscribers.remove(channel);

        if (messageListener != null) {
            container.removeMessageListener(messageListener);
        }
    }
}
