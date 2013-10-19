package org.reefy.test.transport;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.reefy.transportrest.api.*;
import org.reefy.transportrest.api.transport.Contact;
import org.reefy.transportrest.api.transport.TransportClient;
import org.reefy.transportrest.api.transport.TransportException;
import org.reefy.transportrest.api.transport.ValueNotFoundException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

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
    private final Exception testException = new Exception("mock exception");

    private static class NoopAppServerHandler<C extends Contact> implements AppServerHandler<C> {

        @Override
        public ListenableFuture<PutResponse<C>> put(Key key, Value value) {
            // Do nothing
            return null;
        }

        @Override
        public ListenableFuture<GetResponse<C>> get(Key key) {
            // Do nothing
            return null;
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
        public ListenableFuture<GetResponse<C>> get(Key key) {
            latch.countDown();

            // Arbitrarily return succeeded
            return Futures.immediateFuture(AbstractAppServerHandler.<C>succeedGetResponse(testValue));
        }
    }

    private class PutSucceedAppServerHandler extends NoopAppServerHandler<C> {
        @Override
        public ListenableFuture<PutResponse<C>> put(Key key, Value value) {
            assertThat(key, is(testKey));
            assertThat(value, is(testValue));

            return Futures.immediateFuture(AbstractAppServerHandler.<C>succeedPutResponse());
        }
    }

    private class PutRedirectAppServerHandler extends NoopAppServerHandler<C> {
        @Override
        public ListenableFuture<PutResponse<C>> put(Key key, Value value) {
            assertThat(key, is(testKey));
            assertThat(value, is(testValue));

            return Futures.immediateFuture(AbstractAppServerHandler.<C>redirectPutResponse(redirectContact));
        }
    }

    private class PutFailAppServerHandler extends NoopAppServerHandler<C> {
        @Override
        public ListenableFuture<PutResponse<C>> put(Key key, Value value) {
            assertThat(key, is(testKey));
            assertThat(value, is(testValue));

            return Futures.immediateFuture(AbstractAppServerHandler.<C>failPutResponse(testException));
        }
    }

    private class GetPresentAppServerHandler extends NoopAppServerHandler<C> {
        @Override
        public ListenableFuture<GetResponse<C>> get(Key key) {
            assertThat(key, is(testKey));

            return Futures.immediateFuture(AbstractAppServerHandler.<C>succeedGetResponse(testValue));
        }
    }

    private class GetRedirectAppServerHandler extends NoopAppServerHandler<C> {
        @Override
        public ListenableFuture<GetResponse<C>> get(Key key) {
            assertThat(key, is(testKey));

            return Futures.immediateFuture(AbstractAppServerHandler.<C>redirectGetResponse(redirectContact));
        }
    }

    private class GetNotFoundAppServerHandler extends NoopAppServerHandler<C> {
        @Override
        public ListenableFuture<GetResponse<C>> get(Key key) {
            assertThat(key, is(testKey));

            return Futures.immediateFuture(AbstractAppServerHandler.<C>failGetResponse(new ValueNotFoundException()));
        }
    }

    private class GetFailAppServerHandler extends NoopAppServerHandler<C> {
        @Override
        public ListenableFuture<GetResponse<C>> get(Key key) {
            assertThat(key, is(testKey));

            return Futures.immediateFuture(AbstractAppServerHandler.<C>failGetResponse(new ValueNotFoundException()));
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
        final CountDownLatch latch = new CountDownLatch(1);

        final TransportClient<C> client = transportFactory.buildClient();
        final TransportFactory.ServerWhatever<C> serverWhatever =
            transportFactory.buildServer(new LatchAppServerHandler(latch));
        serverWhatever.getServer().startAndWait();

        @SuppressWarnings("unchecked")
        final TransportClient.GetCallback<C> callback =
                (TransportClient.GetCallback<C>) mock(TransportClient.GetCallback.class);
        client.get(serverWhatever.getContact(), testKey, callback);

        latch.await(LATCH_TIMEOUT, TimeUnit.MILLISECONDS);

        client.stopAndWait();
    }

    @Test
    public void testPutSucceed() throws InterruptedException {
        final TransportClient<C> client = transportFactory.buildClient();
        final TransportFactory.ServerWhatever<C> serverWhatever =
                transportFactory.buildServer(new PutSucceedAppServerHandler());
        serverWhatever.getServer().startAndWait();

        final CountDownLatch latch = new CountDownLatch(1);

        client.put(serverWhatever.getContact(), testKey, testValue, new FailTransportClientPutCallback() {
            @Override
            public void succeed() {
                // Succeed
                latch.countDown();
                client.stopAndWait();
            }
        });

        assertTrue(latch.await(LATCH_TIMEOUT, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testPutRedirect() throws InterruptedException {
        final TransportClient<C> client = transportFactory.buildClient();
        final TransportFactory.ServerWhatever<C> serverWhatever =
                transportFactory.buildServer(new PutRedirectAppServerHandler());
        serverWhatever.getServer().startAndWait();

        final CountDownLatch latch = new CountDownLatch(1);

        client.put(serverWhatever.getContact(), testKey, testValue, new FailTransportClientPutCallback<C>() {
            @Override
            public void redirect(C redirect) {
                assertEquals(redirect, redirectContact);

                // Succeed
                latch.countDown();
                client.stopAndWait();
            }
        });

        assertTrue(latch.await(LATCH_TIMEOUT, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testPutFail() throws InterruptedException {
        final TransportClient<C> client = transportFactory.buildClient();
        final TransportFactory.ServerWhatever<C> serverWhatever =
                transportFactory.buildServer(new PutFailAppServerHandler());
        serverWhatever.getServer().startAndWait();

        final CountDownLatch latch = new CountDownLatch(1);

        client.put(serverWhatever.getContact(), testKey, testValue, new FailTransportClientPutCallback() {
            @Override
            public void fail(TransportException exception) {
                // Succeed
                latch.countDown();
                client.stopAndWait();
            }
        });

        assertTrue(latch.await(LATCH_TIMEOUT, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testGetPresent() throws InterruptedException {
        final TransportClient<C> client = transportFactory.buildClient();
        final TransportFactory.ServerWhatever<C> serverWhatever =
            transportFactory.buildServer(new GetPresentAppServerHandler());
        serverWhatever.getServer().startAndWait();

        final CountDownLatch latch = new CountDownLatch(1);

        client.get(serverWhatever.getContact(), testKey, new FailTransportClientGetCallback<C>() {
            @Override
            public void present(Value value) {
                // Succeed
                Assert.assertEquals(testValue, value);
                latch.countDown();
                client.stopAndWait();
            }
        });

        assertTrue(latch.await(LATCH_TIMEOUT, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testGetRedirect() throws InterruptedException {
        final TransportClient<C> client = transportFactory.buildClient();
        final TransportFactory.ServerWhatever<C> serverWhatever =
                transportFactory.buildServer(new GetRedirectAppServerHandler());
        serverWhatever.getServer().startAndWait();

        final CountDownLatch latch = new CountDownLatch(1);

        client.get(serverWhatever.getContact(), testKey, new FailTransportClientGetCallback<C>() {
            @Override
            public void redirect(C contact) {
                // Succeed
                // TODO: latches
                Assert.assertEquals(redirectContact, contact);
                latch.countDown();
                client.stopAndWait();
            }
        });

        assertTrue(latch.await(LATCH_TIMEOUT, TimeUnit.MILLISECONDS));
    }



    @Test
    public void testGetNotFound() throws InterruptedException {
        final TransportClient<C> client = transportFactory.buildClient();
        final TransportFactory.ServerWhatever<C> serverWhatever =
            transportFactory.buildServer(new GetNotFoundAppServerHandler());
        serverWhatever.getServer().startAndWait();

        final CountDownLatch latch = new CountDownLatch(1);

        client.get(serverWhatever.getContact(), testKey, new FailTransportClientGetCallback<C>() {
            @Override
            public void fail(TransportException exception) {
                assertThat(exception.getCause(), instanceOf(ValueNotFoundException.class));

                // Succeed
                latch.countDown();
                client.stopAndWait();
            }
        });

        assertTrue(latch.await(LATCH_TIMEOUT, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testGetFail() throws InterruptedException {
        final TransportClient<C> client = transportFactory.buildClient();
        final TransportFactory.ServerWhatever<C> serverWhatever =
            transportFactory.buildServer(new GetFailAppServerHandler());
        serverWhatever.getServer().startAndWait();

        final CountDownLatch latch = new CountDownLatch(1);

        client.get(serverWhatever.getContact(), testKey, new FailTransportClientGetCallback<C>() {
            @Override
            public void fail(TransportException exception) {
                // Succeed
                latch.countDown();
                client.stopAndWait();
            }
        });

        assertTrue(latch.await(LATCH_TIMEOUT, TimeUnit.MILLISECONDS));
    }
}
