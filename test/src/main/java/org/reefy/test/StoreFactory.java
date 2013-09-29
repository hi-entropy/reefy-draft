package org.reefy.test;

import org.reefy.transportrest.api.store.Store;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public interface StoreFactory {
    public Store build();
}
