package io.jenkins.plugins.rabbitmqconsumer.mocks;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;

import static io.jenkins.plugins.rabbitmqconsumer.Mocks.consumerPool;

public class ChannelMock extends MockUp<Channel> {

    @Mock
    public void close() {
    }

    @Mock
    public void basicAck(long deliveryTag, boolean multiple) {
    }

    @Mock
    public String basicConsume(Invocation invocation, String queue, boolean autoAck, Consumer callback) {
        consumerPool.push(callback);
        return "consumerTag";
    }

    public Object getMockInstance() {
        return null;
    }
}
