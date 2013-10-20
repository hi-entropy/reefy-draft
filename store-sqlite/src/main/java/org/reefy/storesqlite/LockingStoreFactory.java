package org.reefy.storesqlite;

import org.reefy.test.StoreFactory;
import org.reefy.transportrest.api.store.Store;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public abstract class LockingStoreFactory implements StoreFactory {
    @Override
    public Store build() {

        return buildSingleton();
    }

    protected abstract Store buildSingleton();
}
