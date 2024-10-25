package io.jenkins.plugins.rabbitmqconsumer.channels;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import com.rabbitmq.client.*;
import io.jenkins.plugins.rabbitmqconsumer.mocks.*;
import io.jenkins.plugins.rabbitmqconsumer.extensions.MessageQueueListener;
import io.jenkins.plugins.rabbitmqconsumer.listeners.RMQChannelListener;
import io.jenkins.plugins.rabbitmqconsumer.mocks.ChannelMock;
import mockit.Mocked;
import mockit.Expectations;

import io.jenkins.plugins.rabbitmqconsumer.Mocks;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test for ConsumeRMQChannel class.
 *
 * @author rinrinne a.k.a. rin_ne
 */
public class ConsumeRMQChannelTest {

    @Mocked
    Connection connection;

    @Mocked
    MessageQueueListener mqListener = null;     /* dummy */

    RMQChannelListener chListener = new RMQChannelListenerMock();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        new ConsumeRMQChannelMock();

        MessageQueueListener listener;
        listener = new MessageQueueListenerMock("listener-1", "app-1");
        Mocks.mqListenerSet.add(listener);

        listener = new MessageQueueListenerMock("listener-2", "app-2");
        Mocks.mqListenerSet.add(listener);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
        new Expectations() {{
            connection.createChannel();
            result = new ChannelMock().getMockInstance();

            MessageQueueListener.fireOnBind((Collection<String>) any, anyString);
            result = new Mocks.OnBindDelegation();

            MessageQueueListener.fireOnUnbind((Collection<String>) any, anyString);
            result = new Mocks.OnUnbindDelegation();

            MessageQueueListener.fireOnReceive(anyString,
                    anyString,
                    anyString,
                    (Map<String, Object>) any,
                    (byte[]) any);
            result = new Mocks.OnReceiveDelegation();
        }};
    }

    @After
    public void tearDown() throws Exception {
    }

    //@Test
    public void testConsume() {
        HashSet<String> appIds = new HashSet<String>();
        appIds.addAll(Arrays.asList("app-1" ,"app-2", "app-3"));
        Envelope envelope = new Envelope(0L, false, "exchange-1", "test.app");

        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
        builder.appId("app-1");
        builder.contentEncoding(StandardCharsets.UTF_8.name());
        builder.contentType("application/json");

        AMQP.BasicProperties props = builder.build();

        ConsumeRMQChannel channel = new ConsumeRMQChannel("theQueue", appIds);
        channel.addRMQChannelListener(chListener);
        try {
            channel.open(connection);
            channel.consume();

            Consumer consumer = Mocks.consumerPool.pop();
            consumer.handleDelivery("consumerTag", envelope, props, "Test message".getBytes());

            assertEquals("Unmatched response size", 1, Mocks.responseArray.size());
            assertThat("Unmatch consumed queue.", Mocks.responseArray.get(0), is("listener-1"));
            channel.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            fail(ex.toString());
        }
    }

}
