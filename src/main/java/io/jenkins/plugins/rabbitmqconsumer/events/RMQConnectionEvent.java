package io.jenkins.plugins.rabbitmqconsumer.events;

/**
 * Events for {@link org.jenkinsci.plugins.roborabbit.listeners.RMQConnectionListener}.
 *
 * @author nobuhiro
 */
public enum RMQConnectionEvent {
    /**
     * OnOpen event.
     */
    OPEN,
    /**
     * OnCloseCompleted event.
     */
    CLOSE_COMPLETED;
}
