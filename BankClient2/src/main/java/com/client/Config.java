package com.client;

/**
 * Server information, used by default by a client.
 */
public interface Config {
    /**
     * IP address of the server.
     */
    String SERVER_ADDRESS = "127.0.0.1";
    /**
     * Listening port of the server.
     */
    int SERVER_PORT = 8888;
}
