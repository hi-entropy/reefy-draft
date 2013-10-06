package org.reefy.test;

import org.junit.Assert;
import org.junit.Test;
import org.reefy.transportrest.api.AppServerHandler;
import org.reefy.transportrest.api.Key;
import org.reefy.transportrest.api.RawKey;
import org.reefy.transportrest.api.RawValue;
import org.reefy.transportrest.api.TransportServer;
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
            if (key.equals(testKey)) {
                callback.succeed(testValue);
                return;
            }

            callback.fail(new ValueNotFoundException());
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

        client.get(serverWhatever.getContact(), testKey, new TransportClient.GetCallback() {
            @Override
            public void succeed(Value value) {
                Assert.assertEquals(testValue, value);
                client.stopAndWait();
            }

            @Override
            public void redirect(Contact contact) {
                Assert.fail("redirected to " + contact);
            }

            @Override
            public void fail(TransportException exception) {
                Assert.fail("get failed " + exception.getMessage());
            }
        });
    }
}
