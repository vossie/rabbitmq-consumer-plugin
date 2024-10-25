package io.jenkins.plugins.rabbitmqconsumer.mocks;

import io.jenkins.plugins.rabbitmqconsumer.RMQConnection;
import io.jenkins.plugins.rabbitmqconsumer.listeners.RMQConnectionListener;

import java.util.logging.Logger;

public class RMQConnectionListenerMock implements RMQConnectionListener {
    public static final Logger LOGGER = Logger.getLogger(RMQConnectionListenerMock.class.getName());

    public void onOpen(RMQConnection rmqConnection) {
        LOGGER.info("Open RabbitMQ connection.");
    }
    public void onCloseCompleted(RMQConnection rmqConnection) {
        LOGGER.info("Closed RabbitMQ connection.");
    }
}
