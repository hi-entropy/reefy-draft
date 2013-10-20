package org.reefy.test.store.inmemory;

import org.reefy.test.StoreFactory;
import org.reefy.transportrest.api.store.InMemoryStore;
import org.reefy.transportrest.api.store.Store;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class InMemoryStoreFactory implements StoreFactory {
    @Override
    public Store build() {
        return new InMemoryStore();
    }
}
