package org.reefy.test;

import org.junit.Assert;
import org.reefy.transportrest.api.Key;
import org.reefy.transportrest.api.RawKey;
import org.reefy.transportrest.api.RawValue;
import org.reefy.transportrest.api.Value;
import org.reefy.transportrest.api.store.Store;
import org.reefy.transportrest.api.store.StoreException;

import java.util.concurrent.CountDownLatch;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public abstract class AbstractStoreTest {

    public static final int VALUE_SIZE = 8;
    private final StoreFactory storeFactory;

    protected AbstractStoreTest(StoreFactory storeFactory) {
        this.storeFactory = storeFactory;
    }

    public void testPutGet() {
        final CountDownLatch latch = new CountDownLatch(1);

        final Store store = storeFactory.build();
        final Key testKey = RawKey.pseudorandom();
        final Value testValue = RawValue.pseudorandom(VALUE_SIZE);
        store.put(testKey, testValue, new Store.PutCallback() {
            @Override
            public void succeed() {
                latch.countDown();
            }

            @Override
            public void fail(StoreException error) {
                Assert.fail();
            }
        });
    }
}
