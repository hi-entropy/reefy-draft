package org.reefy.test;

import org.reefy.transportrest.api.store.InMemoryStore;
import org.reefy.transportrest.api.store.Store;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class InMemoryStoreTestOld extends AbstractStoreTestOld {

    public InMemoryStoreTestOld() {
        super(new StoreFactory() {
            @Override
            public Store build() {
                return new InMemoryStore();
            }
        });
    }
}
