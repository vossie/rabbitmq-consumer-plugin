package io.jenkins.plugins.rabbitmqconsumer.publishers;

import io.jenkins.plugins.rabbitmqconsumer.RMQManager;

/**
 * A factory class for RabbitMQ publish channel.
 *
 * @author rinrinne a.k.a. rin_ne
 */
public class PublishChannelFactory {

    private PublishChannelFactory() {
    }

    /**
     * Gets {@link PublishChannel}.
     * Note that you should not keep this instance.
     *
     * @return a instance.
     */
    public static PublishChannel getPublishChannel() {
        return RMQManager.getInstance().getPublishChannel();
    }
}
