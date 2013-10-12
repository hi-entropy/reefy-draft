package org.reefy.storesqlite;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Test;
import org.reefy.transportrest.api.Key;
import org.reefy.transportrest.api.RawKey;
import org.reefy.transportrest.api.RawValue;
import org.reefy.transportrest.api.Value;
import org.reefy.transportrest.api.store.Store;
import org.reefy.transportrest.api.store.StoreException;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class SqliteStoreTest {
    @Test
    public void testPutGet() throws InterruptedException, StoreException {
        final Store store = new SqliteStore();

        store.clear();

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
}
