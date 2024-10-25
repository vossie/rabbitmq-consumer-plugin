package io.jenkins.plugins.rabbitmqconsumer;

import io.jenkins.plugins.rabbitmqconsumer.extensions.ServerOperator;
import io.jenkins.plugins.rabbitmqconsumer.extensions.MessageQueueListener;

import mockit.Delegate;
import java.util.logging.Logger;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.Stack;
import java.util.HashSet;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A utility class to declare mock.
 *
 * @author rinrinne a.k.a. rin_ne
 */
public class Mocks {

    private static final Logger LOGGER = Logger.getLogger(Mocks.class.getName());

    public static final Stack<Consumer> consumerPool = new Stack<>();
    public static final List<String> responseArray = new CopyOnWriteArrayList<>();
    public static final Set<MessageQueueListener> mqListenerSet = new CopyOnWriteArraySet<>();
    public static final Set<ServerOperator> operatorSet = new CopyOnWriteArraySet<>();

    public static final class OnBindDelegation implements Delegate<MessageQueueListener> {
        void fireOnBind(HashSet<String> appIds, String queueName) {
            for (MessageQueueListener l : mqListenerSet) {
                if (appIds.contains(l.getAppId())) {
                    l.onBind(queueName);
                }
            }
        }
    }

    public static final class OnUnbindDelegation implements Delegate<MessageQueueListener> {
        void fireOnUnbind(HashSet<String> appIds, String queueName) {
            for (MessageQueueListener l : mqListenerSet) {
                if (appIds.contains(l.getAppId())) {
                    l.onUnbind(queueName);
                }
            }
        }
    }

    public static final class OnReceiveDelegation implements Delegate<MessageQueueListener> {
        void fireOnReceive(String appId,
                String queueName,
                String contentType,
                Map<String, Object> headers,
                byte[] body) {
            for (MessageQueueListener l : mqListenerSet) {
                if (appId.equals(l.getAppId())) {
                    l.onReceive(queueName, contentType, headers, body);
                }
            }
        }
    }

    public static final class OnOpenDelegation implements Delegate<ServerOperator> {
        void fireOnOpen(RMQConnection rmqConnection) throws IOException {
            if (rmqConnection.getConnection() != null) {
                for (ServerOperator l : operatorSet) {
                    try {
                        Channel ch = rmqConnection.getConnection().createChannel();
                        l.OnOpen(ch, rmqConnection.getServiceUri());
                        ch.close();
                    } catch (Exception ex) {
                        LOGGER.warning("Caught exception from OnOpen().");
                    }
                }
            }
        }
    }

    public static final class OnCloseCompletedDelegation implements Delegate<ServerOperator> {
        void fireOnCloseCompleted(RMQConnection rmqConnection) throws IOException {
            for (ServerOperator l : operatorSet) {
                l.OnCloseCompleted(rmqConnection.getServiceUri());
            }
        }
    }
}
