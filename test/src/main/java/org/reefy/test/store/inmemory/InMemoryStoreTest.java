package org.reefy.test.store.inmemory;

import org.reefy.test.StoreFactory;
import org.reefy.test.store.AbstractStoreTest;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class InMemoryStoreTest extends AbstractStoreTest {
    public InMemoryStoreTest() {
        super(new InMemoryStoreFactory());
    }
}
