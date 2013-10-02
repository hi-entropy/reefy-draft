package org.reefy.transportrest.api;

import org.reefy.transportrest.api.store.Store;
import org.reefy.transportrest.api.store.StoreException;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class SimpleAppServer implements AppServer {
    private final Store store;

    public SimpleAppServer(Store store) {
        this.store = store;
    }

    public void get(Key key, final GetCallback callback) {
        store.get(key, new Store.GetCallback() {
            @Override
            public void succeed(Value value) {
                callback.succeed(value);
            }

            @Override
            public void fail(StoreException e) {
                callback.fail(e);
            }
        });
    }
}
