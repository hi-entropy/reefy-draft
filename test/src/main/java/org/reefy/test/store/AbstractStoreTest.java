package org.reefy.test.store;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.reefy.transportrest.api.Key;
import org.reefy.transportrest.api.RawKey;
import org.reefy.transportrest.api.RawValue;
import org.reefy.transportrest.api.Value;
import org.reefy.transportrest.api.store.Store;
import org.reefy.transportrest.api.store.StoreException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public abstract class AbstractStoreTest {

    private final StoreFactory storeFactory;
    private final long timeoutMillis;

    private Store store;

    public AbstractStoreTest(StoreFactory storeFactory, long timeoutMillis) {
        this.storeFactory = storeFactory;
        this.timeoutMillis = timeoutMillis;
    }

    @Before
    public void setUp() throws StoreException {
        store = storeFactory.build();

        store.startAndWait();
        store.clear();
    }

    @Test
    public void testPut() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        final Key testKey = RawKey.pseudorandom();
        final Value testValue = RawValue.pseudorandom(5);

        store.put(testKey, testValue, new Store.PutCallback() {
            @Override
            public void succeed() {
                latch.countDown();
            }

            @Override
            public void fail(StoreException e) {
                Assert.fail(ExceptionUtils.getStackTrace(e));
            }
        });

        assertTrue(latch.await(timeoutMillis, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testPutGet() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        final Key testKey = RawKey.pseudorandom();
        final Value testValue = RawValue.pseudorandom(20);

        store.put(testKey, testValue, new Store.PutCallback() {
            @Override
            public void succeed() {
                store.get(testKey, new Store.GetCallback<Object>() {
                    @Override
                    public void succeed(Value<Object> value) {
                        assertThat(value, is(testValue));

                        latch.countDown();
                    }

                    @Override
                    public void notFound() {
                        Assert.fail("Key not found");
                    }

                    @Override
                    public void fail(StoreException e) {
                        Assert.fail(ExceptionUtils.getStackTrace(e));
                    }
                });
            }

            @Override
            public void fail(StoreException e) {
                Assert.fail(ExceptionUtils.getStackTrace(e));
            }
        });

        assertTrue(latch.await(timeoutMillis, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testPutClearGet() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        final Key testKey = RawKey.pseudorandom();
        final Value testValue = RawValue.pseudorandom(20);

        store.put(testKey, testValue, new Store.PutCallback() {
            @Override
            public void succeed() {
                try {
                    store.clear();
                } catch (StoreException e) {
                    Assert.fail(e.getMessage());
                }

                store.get(testKey, new Store.GetCallback<Object>() {
                    @Override
                    public void succeed(Value<Object> value) {
                        assertThat(value, is(testValue));

                        Assert.fail("Key not found");
                    }

                    @Override
                    public void notFound() {
                        latch.countDown();
                    }

                    @Override
                    public void fail(StoreException e) {
                        Assert.fail(ExceptionUtils.getStackTrace(e));
                    }
                });
            }

            @Override
            public void fail(StoreException e) {
                Assert.fail(ExceptionUtils.getStackTrace(e));
            }
        });

        assertTrue(latch.await(timeoutMillis, TimeUnit.MILLISECONDS));
    }



    @Test
    public void testPutOneGetOther() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        final Key testKey1 = RawKey.pseudorandom();
        final Key testKey2 = RawKey.pseudorandom();
        final Value testValue = RawValue.pseudorandom(20);

        store.put(testKey1, testValue, new Store.PutCallback() {
            @Override
            public void succeed() {
                store.get(testKey2, new Store.GetCallback<Object>() {
                    @Override
                    public void succeed(Value<Object> value) {
                        Assert.fail("Key should not have been found");

                    }

                    @Override
                    public void notFound() {
                        latch.countDown();
                    }

                    @Override
                    public void fail(StoreException e) {
                        Assert.fail(ExceptionUtils.getStackTrace(e));
                    }
                });
            }

            @Override
            public void fail(StoreException e) {
                Assert.fail(ExceptionUtils.getStackTrace(e));
            }
        });

        assertTrue(latch.await(timeoutMillis, TimeUnit.MILLISECONDS));
    }

    @After
    public void tearDown() {
        store.stopAndWait();
    }
}
