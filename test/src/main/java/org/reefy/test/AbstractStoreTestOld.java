package org.reefy.test;

import org.junit.Test;
import org.reefy.transportrest.api.Key;
import org.reefy.transportrest.api.RawKey;
import org.reefy.transportrest.api.RawValue;
import org.reefy.transportrest.api.store.Store;

import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public abstract class AbstractStoreTestOld {

    public static final int VALUE_SIZE = 8;
    private final StoreFactory storeFactory;

    protected AbstractStoreTestOld(StoreFactory storeFactory) {
        this.storeFactory = storeFactory;
    }

    @Test
    public void testPutGet() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        final Store store = storeFactory.build();
        final Key testKey = RawKey.pseudorandom();
        final RawValue testValue = RawValue.pseudorandom(VALUE_SIZE);

//        testKey.put(store, testValue);
//
//        final ByteBuffer testKey.get(store).wait();


    }
}
