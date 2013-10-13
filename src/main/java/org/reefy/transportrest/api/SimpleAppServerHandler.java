package org.reefy.transportrest.api;

import org.reefy.transportrest.api.store.Store;
import org.reefy.transportrest.api.store.StoreException;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class SimpleAppServerHandler implements AppServerHandler {
    private final Store store;

    public SimpleAppServerHandler(Store store) {
        this.store = store;
    }

    public void put(Key key, Value value, final PutCallback callback) {
            store.put(key, value, new Store.PutCallback() {
                @Override
                public void succeed() {
                    callback.succeed();
                }

                @Override
                public void fail(StoreException e) {
                    callback.fail(e);
                }
            });
    }

    public void get(Key key, final GetCallback callback) {
        store.get(key, new Store.GetCallback() {
            @Override
            public void succeed(Value value) {
                callback.present(value);
            }

            @Override
            public void notFound() {
                // TODO: maybe redirect
                callback.notFound();
            }

            @Override
            public void fail(StoreException e) {
                callback.fail(e);
            }
        });
    }
}
