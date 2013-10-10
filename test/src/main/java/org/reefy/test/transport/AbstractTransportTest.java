package org.reefy.test.transport;

import org.junit.Assert;
import org.junit.Test;
import org.reefy.test.TransportFactory;
import org.reefy.transportrest.api.AbstractContact;
import org.reefy.transportrest.api.AppServerHandler;
import org.reefy.transportrest.api.Key;
import org.reefy.transportrest.api.RawKey;
import org.reefy.transportrest.api.RawValue;
import org.reefy.transportrest.api.Value;
import org.reefy.transportrest.api.transport.Contact;
import org.reefy.transportrest.api.transport.TransportClient;
import org.reefy.transportrest.api.transport.TransportException;
import org.reefy.transportrest.api.transport.ValueNotFoundException;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public abstract class AbstractTransportTest<C extends Contact> {

    private final TransportFactory<C> transportFactory;

    // Just use a random key and constant value
    private final RawKey testKey = RawKey.pseudorandom();
    private final String message = "test";
    private final Value testValue = new RawValue(message.getBytes());
    private final C redirectContact;
    private static final int LATCH_TIMEOUT = 1000;

    /**
     * When the server receives a request, count down the latch.
     */
    private class LatchAppServerHandler implements AppServerHandler<C> {

        private final CountDownLatch latch;

        public LatchAppServerHandler(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void get(Key key, GetCallback callback) {
            latch.countDown();
        }
    }

    private class GetPresentAppServerHandler implements AppServerHandler<C> {
        @Override
        public void get(Key key, GetCallback<C> callback) {
            Assert.assertEquals(testKey, key);

            callback.present(testValue);
        }
    }

    private class GetRedirectAppServerHandler implements AppServerHandler<C> {
        @Override
        public void get(Key key, GetCallback<C> callback) {
            Assert.assertEquals(testKey, key);

            callback.redirect(redirectContact);
        }
    }

    private class GetNotFoundAppServerHandler implements AppServerHandler<C> {
        @Override
        public void get(Key key, GetCallback callback) {
            Assert.assertEquals(testKey, key);

            callback.notFound();
        }
    }

    private class GetFailAppServerHandler implements AppServerHandler<C> {
        @Override
        public void get(Key key, GetCallback callback) {
            Assert.assertEquals(testKey, key);

            callback.fail(new Exception("test exception"));
        }
    }

    protected AbstractTransportTest(TransportFactory<C> transportFactory) {
        this.transportFactory = transportFactory;
        redirectContact = transportFactory.buildMockContact();
    }

    /**
     * Just ensure that the right server receives a request send by the client.
     */
    @Test
    public void testServerReceivesRequest() throws InterruptedException {
        final TransportClient<C> client = transportFactory.buildClient();
        final CountDownLatch latch = new CountDownLatch(1);
        final TransportFactory.ServerWhatever<C> serverWhatever =
            transportFactory.buildServer(new LatchAppServerHandler(latch));
        serverWhatever.getServer().startAndWait();

        client.get(serverWhatever.getContact(), testKey, new TransportClient.GetCallback<C>() {
            @Override
            public void present(Value value) {
                Assert.fail("Get unexpectedly succeeded: " + value);
            }

            @Override
            public void redirect(C contact) {
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

        latch.await(LATCH_TIMEOUT, TimeUnit.MILLISECONDS);

        client.stopAndWait();
    }

    @Test
    public void testGetPresent() {
        final TransportClient<C> client = transportFactory.buildClient();
        final TransportFactory.ServerWhatever<C> serverWhatever =
            transportFactory.buildServer(new GetPresentAppServerHandler());
        serverWhatever.getServer().startAndWait();

        client.get(serverWhatever.getContact(), testKey, new TransportClient.GetCallback<C>() {
            @Override
            public void present(Value value) {
                // Succeed
                Assert.assertEquals(testValue, value);
                client.stopAndWait();
            }

            @Override
            public void redirect(C contact) {
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

    @Test
    public void testGetRedirect() {
        final TransportClient<C> client = transportFactory.buildClient();
        final TransportFactory.ServerWhatever<C> serverWhatever =
                transportFactory.buildServer(new GetRedirectAppServerHandler());
        serverWhatever.getServer().startAndWait();

        client.get(serverWhatever.getContact(), testKey, new TransportClient.GetCallback<C>() {
            @Override
            public void present(Value value) {
                Assert.fail("Get succeeded: " + value);
            }

            @Override
            public void redirect(C contact) {
                // Succeed
                // TODO: latches
                Assert.assertEquals(redirectContact, contact);
                client.stopAndWait();
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



    @Test
    public void testGetNotFound() {
        final TransportClient<C> client = transportFactory.buildClient();
        final TransportFactory.ServerWhatever<C> serverWhatever =
            transportFactory.buildServer(new GetNotFoundAppServerHandler());
        serverWhatever.getServer().startAndWait();

        client.get(serverWhatever.getContact(), testKey, new TransportClient.GetCallback<C>() {
            @Override
            public void present(Value value) {
                Assert.fail("Value retrieved: " + value);

            }

            @Override
            public void redirect(C contact) {
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

    @Test
    public void testGetFail() {
        final TransportClient<C> client = transportFactory.buildClient();
        final TransportFactory.ServerWhatever<C> serverWhatever =
            transportFactory.buildServer(new GetFailAppServerHandler());
        serverWhatever.getServer().startAndWait();

        client.get(serverWhatever.getContact(), testKey, new TransportClient.GetCallback<C>() {
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
                Assert.fail("Key not found");
            }

            @Override
            public void fail(TransportException exception) {
                // Succeed
                client.stopAndWait();
            }
        });
    }
}
