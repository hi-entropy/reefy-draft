package org.reefy.storesqlite;

import org.reefy.test.store.StoreFactory;
import org.reefy.transportrest.api.store.Store;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class SqliteStoreFactory implements StoreFactory {
    private static volatile int currentStoreId = 0;

    @Override
    public Store build() {
        return new SqliteStore("test" + currentStoreId++);
    }
}
