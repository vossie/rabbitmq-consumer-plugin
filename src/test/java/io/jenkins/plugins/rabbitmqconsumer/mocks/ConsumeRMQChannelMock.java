package io.jenkins.plugins.rabbitmqconsumer.mocks;

import io.jenkins.plugins.rabbitmqconsumer.channels.ConsumeRMQChannel;
import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;

public class ConsumeRMQChannelMock extends MockUp<ConsumeRMQChannel> {

    @Mock
    public boolean isEnableDebug() {
        return false;
    }

    @Mock
    public void close(Invocation invocation) {
        invocation.proceed();
        ConsumeRMQChannel ch = invocation.getInvokedInstance();
        ch.shutdownCompleted(null);
    }
}
