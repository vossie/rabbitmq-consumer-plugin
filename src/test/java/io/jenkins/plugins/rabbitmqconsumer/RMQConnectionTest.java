package io.jenkins.plugins.rabbitmqconsumer;

import static org.junit.Assert.*;

import io.jenkins.plugins.rabbitmqconsumer.mocks.*;
import io.jenkins.plugins.rabbitmqconsumer.mocks.RMQConnectionListenerMock;
import io.jenkins.plugins.rabbitmqconsumer.mocks.RMQConnectionMock;
import io.jenkins.plugins.rabbitmqconsumer.watchdog.ReconnectTimer;
import io.jenkins.plugins.rabbitmqconsumer.channels.ConsumeRMQChannel;
import io.jenkins.plugins.rabbitmqconsumer.extensions.MessageQueueListener;
import io.jenkins.plugins.rabbitmqconsumer.listeners.RMQConnectionListener;

import mockit.Mocked;
import mockit.Expectations;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Test for RMQConnection class.
 *
 * @author rinrinne a.k.a. rin_ne
 *
 */
public class RMQConnectionTest {

    @Mocked
    ConnectionFactory factory = new ConnectionFactory();

    @Mocked
    ReconnectTimer timer = new ReconnectTimer();

    @Mocked
    MessageQueueListener mqListener = null;     /* dummy */

    @Mocked
    Connection connection;

    RMQConnectionListener connListener = new RMQConnectionListenerMock();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        new RMQConnectionMock();
        new ConsumeRMQChannelMock();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {

        new Expectations() {{
            connection.createChannel(); result = new ChannelMock().getMockInstance();
            MessageQueueListener.all(); minTimes = 0;
            factory.setConnectionTimeout(anyInt);
            factory.setRequestedHeartbeat(anyInt); minTimes = 0;
            factory.setUri(anyString);
            factory.newConnection(); result = connection;
            ReconnectTimer.get(); result = timer;
            timer.start(); minTimes = 0;
            timer.stop(); minTimes = 0;
        }};
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testOpenChannels() {
        RMQConnection conn = new RMQConnection("", "", null);
        conn.addRMQConnectionListener(connListener);
        List<RabbitmqConsumeItem> items = new ArrayList<RabbitmqConsumeItem>();
        items.add(new RabbitmqConsumeItem("app-1-a", "queue-1"));
        items.add(new RabbitmqConsumeItem("app-1-b", "queue-1"));
        items.add(new RabbitmqConsumeItem("app-2", "queue-2"));
        items.add(new RabbitmqConsumeItem("app-3", "queue-3"));

        HashSet<String> queueNameSet = new HashSet<String>();
        queueNameSet.addAll(Arrays.asList("queue-1","queue-2","queue-3"));

        try {
            conn.open();
            conn.updateChannels(items);
            Collection<ConsumeRMQChannel> channels = conn.getConsumeRMQChannels();
            assertEquals(3, channels.size());
            for (ConsumeRMQChannel ch : channels) {
                assertTrue(queueNameSet.contains(ch.getQueueName()));
                if ("queue-1".equals(ch.getQueueName())) {
                    assertEquals(2, ch.getAppIds().size());
                } else {
                    assertEquals(1, ch.getAppIds().size());
                }
            }
            conn.close();
        } catch (Exception ex) {
            fail(ex.toString());
        }
    }

    @Test
    public void testAddChannels() {
        RMQConnection conn = new RMQConnection("", "", null);
        conn.addRMQConnectionListener(connListener);
        List<RabbitmqConsumeItem> items = new ArrayList<RabbitmqConsumeItem>();
        items.add(new RabbitmqConsumeItem("app-1-a", "queue-1"));
        items.add(new RabbitmqConsumeItem("app-1-b", "queue-1"));
        items.add(new RabbitmqConsumeItem("app-2", "queue-2"));
        items.add(new RabbitmqConsumeItem("app-3", "queue-3"));

        try {
            Collection<ConsumeRMQChannel> channels;
            conn.open();
            conn.updateChannels(items);
            channels = conn.getConsumeRMQChannels();
            assertEquals(3, channels.size());

            items.add(new RabbitmqConsumeItem("app-4", "queue-4"));
            conn.updateChannels(items);
            channels = conn.getConsumeRMQChannels();
            assertEquals(4, channels.size());

            conn.close();
        } catch (Exception ex) {
            fail(ex.toString());
        }
    }

    @Test
    public void testDeleteChannels() {
        RMQConnection conn = new RMQConnection("", "", null);
        conn.addRMQConnectionListener(connListener);
        RabbitmqConsumeItem item = new RabbitmqConsumeItem("app-4", "queue-4");
        List<RabbitmqConsumeItem> items = new ArrayList<RabbitmqConsumeItem>();
        items.add(new RabbitmqConsumeItem("app-1-a", "queue-1"));
        items.add(new RabbitmqConsumeItem("app-1-b", "queue-1"));
        items.add(new RabbitmqConsumeItem("app-2", "queue-2"));
        items.add(new RabbitmqConsumeItem("app-3", "queue-3"));
        items.add(item);

        try {
            Collection<ConsumeRMQChannel> channels;
            conn.open();
            conn.updateChannels(items);
            channels = conn.getConsumeRMQChannels();
            assertEquals(4, channels.size());

            items.remove(item);
            conn.updateChannels(items);
            channels = conn.getConsumeRMQChannels();
            assertEquals(3, channels.size());

            conn.close();
        } catch (Exception ex) {
            fail(ex.toString());
        }
    }

    @Test
    public void testDeleteAndAddChannels() {
        RMQConnection conn = new RMQConnection("", "", null);
        conn.addRMQConnectionListener(connListener);
        RabbitmqConsumeItem item3 = new RabbitmqConsumeItem("app-3", "queue-3");
        RabbitmqConsumeItem item4 = new RabbitmqConsumeItem("app-4", "queue-4");
        List<RabbitmqConsumeItem> items = new ArrayList<RabbitmqConsumeItem>();
        items.add(new RabbitmqConsumeItem("app-1-a", "queue-1"));
        items.add(new RabbitmqConsumeItem("app-1-b", "queue-1"));
        items.add(new RabbitmqConsumeItem("app-2", "queue-2"));
        items.add(item3);

        try {
            Collection<ConsumeRMQChannel> channels;
            conn.open();
            conn.updateChannels(items);
            channels = conn.getConsumeRMQChannels();
            assertEquals(3, channels.size());

            items.remove(item3);
            items.add(item4);
            conn.updateChannels(items);
            channels = conn.getConsumeRMQChannels();
            assertEquals(3, channels.size());
            HashSet<String> queueNames = new HashSet<String>();
            for (ConsumeRMQChannel ch : channels) {
                queueNames.add(ch.getQueueName());
            }
            assertFalse(queueNames.contains("queue-3"));
            assertTrue(queueNames.contains("queue-4"));
            conn.close();
        } catch (Exception ex) {
            fail(ex.toString());
        }
    }

}
