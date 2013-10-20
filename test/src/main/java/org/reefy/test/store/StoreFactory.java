package org.reefy.test.store;

import org.reefy.transportrest.api.store.Store;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public interface StoreFactory {
    public Store build();
}
