package org.reefy.test.coretest;

import org.reefy.test.AbstractStoreTestOld;
import org.reefy.test.StoreFactory;
import org.reefy.transportrest.api.store.InMemoryStore;
import org.reefy.transportrest.api.store.Store;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class InMemoryStoreTest extends AbstractStoreTestOld {
    public InMemoryStoreTest() {
        super(new StoreFactory() {
            @Override
            public Store build() {
                return new InMemoryStore();
            }
        });
    }
}
