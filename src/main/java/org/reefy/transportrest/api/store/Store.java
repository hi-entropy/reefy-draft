package org.reefy.transportrest.api.store;

import org.reefy.transportrest.api.Key;
import org.reefy.transportrest.api.Value;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public interface Store {
    public void put(Key key, Value value, PutCallback callback);

    public static interface PutCallback {
        public void succeed();

        public void fail(StoreException error);
    }

}
