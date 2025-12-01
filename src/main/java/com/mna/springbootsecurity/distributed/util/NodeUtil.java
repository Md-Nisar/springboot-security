package com.mna.springbootsecurity.distributed.util;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class NodeUtil {

    private NodeUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Retrieves the hostname of the current node.
     *
     * @return The hostname or "unknown-node" if an error occurs.
     */
    public static String getNodeName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.error("Failed to retrieve hostname, using default node name.", e);
            return "unknown-node";
        }
    }
}
