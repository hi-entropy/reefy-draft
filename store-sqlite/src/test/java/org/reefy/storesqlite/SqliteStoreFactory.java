package org.reefy.storesqlite;

import org.reefy.core.store.Store;
import org.reefy.core.store.test.StoreFactory;

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
