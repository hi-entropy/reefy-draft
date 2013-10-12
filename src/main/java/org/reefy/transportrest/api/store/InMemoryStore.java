package org.reefy.transportrest.api.store;

import org.reefy.transportrest.api.Key;
import org.reefy.transportrest.api.Value;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class InMemoryStore implements Store {

    @Override
    public void clear() throws StoreException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <V> void put(Key key, Value<V> value, PutCallback<V> callback) {
    }

    @Override
    public <T> void get(Key key, GetCallback<T> callback) {

    }

    @Override
    public void close() {
        // We don't need to do anything
    }
}
