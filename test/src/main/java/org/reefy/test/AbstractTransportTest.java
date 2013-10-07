package org.reefy.test;

import org.junit.Assert;
import org.junit.Test;
import org.reefy.transportrest.api.AppServerHandler;
import org.reefy.transportrest.api.Key;
import org.reefy.transportrest.api.RawKey;
import org.reefy.transportrest.api.RawValue;
import org.reefy.transportrest.api.Value;
import org.reefy.transportrest.api.transport.Contact;
import org.reefy.transportrest.api.transport.TransportClient;
import org.reefy.transportrest.api.transport.TransportException;
import org.reefy.transportrest.api.transport.ValueNotFoundException;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public abstract class AbstractTransportTest<C extends Contact> {

    private final TransportFactory<C> transportFactory;

    // Just use a random key and constant value
    private final Key testKey = RawKey.pseudorandom();
    private final String message = "test";
    private final Value testValue = new RawValue(message.getBytes());

    private class GetPresentAppServerHandler implements AppServerHandler {
        @Override
        public void get(Key key, GetCallback callback) {
            Assert.assertEquals(testKey, key);

            callback.present(testValue);
        }
    }

    protected AbstractTransportTest(TransportFactory<C> transportFactory) {
        this.transportFactory = transportFactory;
    }

    @Test
    public void testGetPresent() {
        final TransportClient<C> client = transportFactory.buildClient();
        final TransportFactory.ServerWhatever<C> serverWhatever =
            transportFactory.buildServer(new GetPresentAppServerHandler());
        serverWhatever.getServer().startAndWait();

        client.get(serverWhatever.getContact(), testKey, new TransportClient.GetCallback() {
            @Override
            public void present(Value value) {
                // Succeed
                Assert.assertEquals(testValue, value);
                client.stopAndWait();
            }

            @Override
            public void redirect(Contact contact) {
                Assert.fail("Redirected to " + contact);
            }

            @Override
            public void notFound() {
                Assert.fail("Key not found.");
            }

            @Override
            public void fail(TransportException exception) {
                Assert.fail("Get failed: " + exception.getMessage());
            }
        });
    }

    private class GetNotFoundAppServerHandler implements AppServerHandler {
        @Override
        public void get(Key key, GetCallback callback) {
            Assert.assertEquals(testKey, key);

            callback.notFound();
        }
    }

    @Test
    public void testGetNotFound() {
        final TransportClient<C> client = transportFactory.buildClient();
        final TransportFactory.ServerWhatever<C> serverWhatever =
            transportFactory.buildServer(new GetNotFoundAppServerHandler());
        serverWhatever.getServer().startAndWait();

        client.get(serverWhatever.getContact(), testKey, new TransportClient.GetCallback() {
            @Override
            public void present(Value value) {
                Assert.fail("Value retrieved: " + value);

            }

            @Override
            public void redirect(Contact contact) {
                Assert.fail("Redirected to " + contact);
            }

            @Override
            public void notFound() {
                // Succeed
                client.stopAndWait();
            }

            @Override
            public void fail(TransportException exception) {
                Assert.fail("Get failed: " + exception.getMessage());
            }
        });
    }
}
