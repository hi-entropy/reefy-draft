package org.reefy.test.app;

import com.google.common.collect.Lists;

import org.joda.time.Duration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.reefy.test.transport.TransportFactory;
import org.reefy.test.transport.local.LocalTransportFactory;
import org.reefy.transportrest.api.AppClient;
import org.reefy.transportrest.api.AppServerHandler;
import org.reefy.transportrest.api.Key;
import org.reefy.transportrest.api.RawKey;
import org.reefy.transportrest.api.RawValue;
import org.reefy.transportrest.api.SimpleAppClient;
import org.reefy.transportrest.api.SimpleAppServer;
import org.reefy.transportrest.api.Value;
import org.reefy.transportrest.api.store.InMemoryStore;
import org.reefy.transportrest.api.store.Store;
import org.reefy.transportrest.api.transport.TransportServer;
import org.reefy.transportrest.api.transport.TransportServerFactory;
import org.reefy.transportrest.api.transport.local.LocalContact;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class ManyServersTest {

    public static final int A_MILLION = 1;
    public static final long TIMEOUT_MILLIS = 1;

    private final List<SimpleAppServer> servers = Lists.newArrayList();
    private final List<SimpleAppClient> clients = Lists.newArrayList();

    private final LocalTransportFactory transportFactory = new LocalTransportFactory();

    @Before
    public void setUp() throws ExecutionException, InterruptedException {
        for (int i = 0; i < A_MILLION; i++) {
            final Store store = new InMemoryStore();

            final SimpleAppServer server = new SimpleAppServer(store, new TransportServerFactory() {
                @Override
                public TransportServer build(AppServerHandler handler) {
                    return transportFactory.buildServer(handler).getServer();
                }
            });
            server.start().get();
            servers.add(server);

            final SimpleAppClient client = new SimpleAppClient(transportFactory.buildClient());
            client.addContact(server.getContact());
            client.start().get();
            clients.add(client);
        }
    }

    @Test
    public void millionTest() throws InterruptedException {
        //final List<Pair<Key, Value>> testKeyValues = Lists.newArrayList();

        final CountDownLatch latch = new CountDownLatch(A_MILLION);

        for (int i = 0; i < A_MILLION; i++) {
            final int thisI = i;

            final Key testKey = RawKey.pseudorandom();
            final Value testValue = RawValue.pseudorandom(5);
            clients.get(i).put(testKey, testValue, Duration.standardHours(1), new AppClient.PutCallback() {
                @Override
                public void succeed() {
                    clients.get(A_MILLION - thisI - 1).get(testKey, new AppClient.GetCallback() {
                        @Override
                        public void succeed(Value value) {
                            latch.countDown();
                        }

                        @Override
                        public void notFound() {
                            Assert.fail("Key " + testKey + " not found");
                        }

                        @Override
                        public void fail(Throwable e) {
                            Assert.fail(e.getMessage());
                        }
                    });
                }

                @Override
                public void fail(Throwable e) {
                    Assert.fail(e.getMessage());
                }
            });
        }

        Assert.assertTrue(latch.await(TIMEOUT_MILLIS * A_MILLION, TimeUnit.MILLISECONDS));
    }

    @After
    public void tearDown() throws ExecutionException, InterruptedException {
        for (SimpleAppServer server : servers) {
            server.stop().get();
        }
        for (SimpleAppClient client : clients) {
            client.stop().get();
        }
    }
}
