package com.mna.springbootsecurity.event.common.util;

import java.util.UUID;

public class MessageIdGenerator {

    private MessageIdGenerator() {
        // Prevent instantiation
    }

    /**
     * Generates a unique message ID based on the queue name and a UUID.
     *
     * @param queueName the name of the queue
     * @return a unique message ID in the format: <queueName>_<UUID>
     */
    public static String generate(String queueName) {
        return queueName + "_" + UUID.randomUUID();
    }
}

