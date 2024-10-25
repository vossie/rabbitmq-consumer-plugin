package io.jenkins.plugins.rabbitmqconsumer.mocks;

import com.rabbitmq.client.Channel;
import io.jenkins.plugins.rabbitmqconsumer.extensions.ServerOperator;

import java.text.MessageFormat;

public class ServerOperatorMock extends ServerOperator {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(ServerOperatorMock.class.getName());

    @Override
    public void OnOpen(Channel controlChannel, String serviceUri) {
        LOGGER.info(MessageFormat.format("Open control channel {0} for {1}.", controlChannel.getChannelNumber(), serviceUri));
    }

    @Override
    public void OnCloseCompleted(String serviceUri) {
        LOGGER.info(MessageFormat.format("Closed connection for {0}.", serviceUri));
    }
}
