package io.jenkins.plugins.rabbitmqconsumer.mocks;

import io.jenkins.plugins.rabbitmqconsumer.RMQConnection;
import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;

public class RMQConnectionMock extends MockUp<RMQConnection> {
    @Mock
    public void close(Invocation invocation) {
        invocation.proceed();
        RMQConnection conn = invocation.getInvokedInstance();
        conn.shutdownCompleted(null);
    }
}
