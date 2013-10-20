package org.reefy.test.store.inmemory;

import org.reefy.test.store.AbstractStoreTest;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class InMemoryStoreTest extends AbstractStoreTest {

    public static final int TIMEOUT_MILLIS = 100;

    public InMemoryStoreTest() {
        super(new InMemoryStoreFactory(), TIMEOUT_MILLIS);
    }
}
