package io.jenkins.plugins.rabbitmqconsumer.mocks;

import io.jenkins.plugins.rabbitmqconsumer.channels.AbstractRMQChannel;
import io.jenkins.plugins.rabbitmqconsumer.channels.ConsumeRMQChannel;
import io.jenkins.plugins.rabbitmqconsumer.listeners.RMQChannelListener;

import java.text.MessageFormat;
import java.util.logging.Logger;

public class RMQChannelListenerMock implements RMQChannelListener {
    private static final Logger LOGGER = Logger.getLogger(RMQChannelListenerMock.class.getName());

    public void onOpen(AbstractRMQChannel rmqChannel) {
        LOGGER.info(MessageFormat.format("Open ConsumeRMQChannelMock channel {0} for {1}.",
                rmqChannel.getChannel().getChannelNumber(), ((ConsumeRMQChannel)rmqChannel).getQueueName()));
    }
    public void onCloseCompleted(AbstractRMQChannel rmqChannel) {
        LOGGER.info(MessageFormat.format("Closed ConsumeRMQChannelMock channel {0} for {1}.",
                rmqChannel.getChannel().getChannelNumber(), ((ConsumeRMQChannel)rmqChannel).getQueueName()));
    }
}
