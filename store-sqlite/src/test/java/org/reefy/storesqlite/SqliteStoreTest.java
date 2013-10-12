package org.reefy.storesqlite;

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

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class SqliteStoreTest {

    private Store store;

    @Before
    public void setUp() throws StoreException {
        store = new SqliteStore();

        store.startAndWait();
        store.clear();
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


        latch.await(5000, TimeUnit.MILLISECONDS);
    }

    @After
    public void tearDown() {
        store.stopAndWait();
    }
}
