package org.reefy.test.transport;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.reefy.test.TransportFactory;
import org.reefy.transportrest.api.AppServerHandler;
import org.reefy.transportrest.api.Key;
import org.reefy.transportrest.api.RawKey;
import org.reefy.transportrest.api.RawValue;
import org.reefy.transportrest.api.Value;
import org.reefy.transportrest.api.transport.Contact;
import org.reefy.transportrest.api.transport.TransportClient;
import org.reefy.transportrest.api.transport.TransportException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public abstract class AbstractTransportTest<C extends Contact> {

    private final TransportFactory<C> transportFactory;

    // Just use a random key and constant value
    private final Key testKey = RawKey.pseudorandom();
    private final String message = "test";
    private final Value testValue = new RawValue(message.getBytes());
    private final C redirectContact;
    private static final int LATCH_TIMEOUT = 1000;

    private static class NoopAppServerHandler<C extends Contact> implements AppServerHandler<C> {

        @Override
        public void put(Key key, Value value, PutCallback<C> callback) {
            // Do nothing
        }

        @Override
        public void get(Key key, GetCallback<C> callback) {
            // Do nothing
        }
    }

    /**
     * This class can be used by itself, or extended in cases where we want a callback where most results should cause
     * the test to fail.
     *
     * @param <C> the type of Contact to use
     */
    private static class FailTransportClientPutCallback<C extends Contact> implements TransportClient.PutCallback<C> {


        @Override
        public void succeed() {
            Assert.fail("Put unexpectedly succeeded");
        }

        @Override
        public void redirect(C contact) {
            Assert.fail("Put unexpectedly redirected to: " + contact);
        }

        @Override
        public void fail(TransportException exception) {
            Assert.fail("Put unexpectedly failed: " + exception.getMessage() + ExceptionUtils.getStackTrace(exception));
        }
    }

    /**
     * This class can be used by itself, or extended in cases where we want a callback where most results should cause
     * the test to fail.
     *
     * @param <C> the type of Contact to use
     */
    private static class FailTransportClientGetCallback<C extends Contact> implements TransportClient.GetCallback<C> {
        @Override
        public void present(Value value) {
            Assert.fail("Get unexpectedly succeeded: " + value);
        }

        @Override
        public void redirect(C contact) {
            Assert.fail("Unexpectedly redirected to: " + contact);
        }

        @Override
        public void notFound() {
            Assert.fail("Key unexpectedly not found");
        }

        @Override
        public void fail(TransportException exception) {
            Assert.fail("Get unexpectedly failed: " + exception.getMessage() + ExceptionUtils.getStackTrace(exception));
        }
    }

    /**
     * When the server receives a request, count down the latch.
     */
    private class LatchAppServerHandler extends NoopAppServerHandler<C> {

        private final CountDownLatch latch;

        public LatchAppServerHandler(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void get(Key key, GetCallback callback) {
            latch.countDown();
            callback.notFound();
        }
    }

    private class PutFailAppServerHandler extends NoopAppServerHandler<C> {
        @Override
        public void put(Key key, Value value, PutCallback callback) {
            assertThat(key, is(testKey));
            assertThat(value, is(testValue));

            callback.fail(new Exception("mock exception"));
        }
    }

    private class GetPresentAppServerHandler extends NoopAppServerHandler<C> {
        @Override
        public void get(Key key, GetCallback<C> callback) {
            assertThat(key, is(testKey));

            callback.present(testValue);
        }
    }

    private class GetRedirectAppServerHandler extends NoopAppServerHandler<C> {
        @Override
        public void get(Key key, GetCallback<C> callback) {
            assertThat(key, is(testKey));

            callback.redirect(redirectContact);
        }
    }

    private class GetNotFoundAppServerHandler extends NoopAppServerHandler<C> {
        @Override
        public void get(Key key, GetCallback callback) {
            assertThat(key, is(testKey));

            callback.notFound();
        }
    }

    private class GetFailAppServerHandler extends NoopAppServerHandler<C> {
        @Override
        public void get(Key key, GetCallback callback) {
            assertThat(key, is(testKey));

            callback.fail(new Exception("mock exception"));
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

        client.get(serverWhatever.getContact(), testKey, new FailTransportClientGetCallback<C>() {
            @Override
            public void notFound() {
                // Test succeeded
                serverWhatever.getServer().stopAndWait();
            }
        });

        latch.await(LATCH_TIMEOUT, TimeUnit.MILLISECONDS);

        client.stopAndWait();
    }

    @Test
    public void testPutFail() {
        final TransportClient<C> client = transportFactory.buildClient();
        final TransportFactory.ServerWhatever<C> serverWhatever =
                transportFactory.buildServer(new PutFailAppServerHandler());
        serverWhatever.getServer().startAndWait();

        client.put(serverWhatever.getContact(), testKey, testValue, new FailTransportClientPutCallback() {
            @Override
            public void fail(TransportException exception) {
                // Succeed
                client.stopAndWait();
            }
        });
    }

    @Test
    public void testGetPresent() {
        final TransportClient<C> client = transportFactory.buildClient();
        final TransportFactory.ServerWhatever<C> serverWhatever =
            transportFactory.buildServer(new GetPresentAppServerHandler());
        serverWhatever.getServer().startAndWait();

        client.get(serverWhatever.getContact(), testKey, new FailTransportClientGetCallback<C>() {
            @Override
            public void present(Value value) {
                // Succeed
                Assert.assertEquals(testValue, value);
                client.stopAndWait();
            }
        });
    }

    @Test
    public void testGetRedirect() {
        final TransportClient<C> client = transportFactory.buildClient();
        final TransportFactory.ServerWhatever<C> serverWhatever =
                transportFactory.buildServer(new GetRedirectAppServerHandler());
        serverWhatever.getServer().startAndWait();

        client.get(serverWhatever.getContact(), testKey, new FailTransportClientGetCallback<C>() {
            @Override
            public void redirect(C contact) {
                // Succeed
                // TODO: latches
                Assert.assertEquals(redirectContact, contact);
                client.stopAndWait();
            }
        });
    }



    @Test
    public void testGetNotFound() {
        final TransportClient<C> client = transportFactory.buildClient();
        final TransportFactory.ServerWhatever<C> serverWhatever =
            transportFactory.buildServer(new GetNotFoundAppServerHandler());
        serverWhatever.getServer().startAndWait();

        client.get(serverWhatever.getContact(), testKey, new FailTransportClientGetCallback<C>() {
            @Override
            public void notFound() {
                // Succeed
                client.stopAndWait();
            }
        });
    }

    @Test
    public void testGetFail() {
        final TransportClient<C> client = transportFactory.buildClient();
        final TransportFactory.ServerWhatever<C> serverWhatever =
            transportFactory.buildServer(new GetFailAppServerHandler());
        serverWhatever.getServer().startAndWait();

        client.get(serverWhatever.getContact(), testKey, new FailTransportClientGetCallback<C>() {
            @Override
            public void fail(TransportException exception) {
                // Succeed
                client.stopAndWait();
            }
        });
    }
}
