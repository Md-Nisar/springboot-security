package com.mna.springbootsecurity.pubsub.message;

import org.springframework.data.redis.connection.Message;

public interface MessageListener {

    void onMessage(Message message);

}
