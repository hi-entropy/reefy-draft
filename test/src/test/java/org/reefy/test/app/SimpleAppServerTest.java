package org.reefy.test.app;

import org.junit.Test;
import org.reefy.transportrest.api.AppServerHandler;
import org.reefy.transportrest.api.SimpleAppServer;
import org.reefy.transportrest.api.transport.TransportServer;
import org.reefy.transportrest.api.transport.TransportServerFactory;

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
