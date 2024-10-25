package io.jenkins.plugins.rabbitmqconsumer.mocks;

import io.jenkins.plugins.rabbitmqconsumer.extensions.MessageQueueListener;

import java.text.MessageFormat;
import java.util.Map;
import java.util.logging.Logger;

import static io.jenkins.plugins.rabbitmqconsumer.Mocks.responseArray;

public class MessageQueueListenerMock extends MessageQueueListener {

    private static final Logger LOGGER = Logger.getLogger(MessageQueueListenerMock.class.getName());

    private final String name;
    private final String appId;

    public MessageQueueListenerMock(String name, String appId) {
        this.name = name;
        this.appId = appId;
    }

    @Override
    public String getName() {
        return name;
    }
    @Override
    public String getAppId() {
        return appId;
    }
    @Override
    public void onBind(String queueName) {
        LOGGER.info(MessageFormat.format("<{0}> Bind queue: {1}", name, queueName));
    }
    @Override
    public void onUnbind(String queueName) {
        LOGGER.info(MessageFormat.format("<{0}> Unbind queue: {1}",name, queueName));
    }
    @Override
    public void onReceive(String queueName, String contentType, Map<String, Object> headers, byte[] body) {
        LOGGER.info(MessageFormat.format("<{0}> Received: {1}", name, queueName));
        responseArray.add(getName());
    }
}
