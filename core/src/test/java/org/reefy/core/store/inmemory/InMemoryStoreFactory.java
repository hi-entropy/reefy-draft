package org.reefy.core.store.inmemory;

import org.reefy.core.store.Store;
import org.reefy.core.store.test.StoreFactory;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class InMemoryStoreFactory implements StoreFactory {
    @Override
    public Store build() {
        return new InMemoryStore();
    }
}
