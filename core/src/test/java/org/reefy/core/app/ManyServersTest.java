package org.reefy.core.app;

import com.google.common.collect.Lists;

import org.joda.time.Duration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.reefy.core.transport.local.LocalTransportFactory;
import org.reefy.core.AppClient;
import org.reefy.core.AppServerHandler;
import org.reefy.core.Key;
import org.reefy.core.RawKey;
import org.reefy.core.RawValue;
import org.reefy.core.SimpleAppClient;
import org.reefy.core.SimpleAppServer;
import org.reefy.core.Value;
import org.reefy.core.store.inmemory.InMemoryStore;
import org.reefy.core.store.Store;
import org.reefy.core.transport.TransportServer;
import org.reefy.core.transport.TransportServerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class ManyServersTest {

    public static final int A_MILLION = 1000;
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
            for (int j = 0; j < i; j++) {
                server.addNeighbor(servers.get(j).getContact());
                servers.get(j).addNeighbor(server.getContact());
            }
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
