package org.reefy.core.app;

import org.junit.Test;
import org.reefy.core.AppServerHandler;
import org.reefy.core.SimpleAppServer;
import org.reefy.core.transport.TransportServer;
import org.reefy.core.transport.TransportServerFactory;

import java.util.concurrent.ExecutionException;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class SimpleAppServerTest {

    @Test
    public void testStartStop() throws ExecutionException, InterruptedException {
        final SimpleAppServer server = new SimpleAppServer(
            new StartStopStore(),
            new TransportServerFactory() {
                @Override
                public TransportServer build(AppServerHandler handler) {
                    return new StartStopTransportServer();
                }
            }
        );

        assertFalse(server.isRunning());
        server.start().get();
        assertTrue(server.isRunning());
        server.stop().get();
        assertFalse(server.isRunning());
    }

}
